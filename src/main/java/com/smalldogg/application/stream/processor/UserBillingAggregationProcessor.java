package com.smalldogg.application.stream.processor;

import com.smalldogg.application.stream.domain.billing.UserBillingAggregate;
import com.smalldogg.infrastructure.service.UserBillingService;
import com.smalldogg.application.stream.domain.impression.ImpressionEvent;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.kstream.*;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.ZoneId;

@RequiredArgsConstructor
@Component
public class UserBillingAggregationProcessor {

    private final UserBillingService billingService;
    private final Serde<ImpressionEvent> impressionEventSerde;
    private final Serde<UserBillingAggregate> userBillingAggregateSerde;

    public void attach(KStream<String, ImpressionEvent> stream) {
        KStream<String, ImpressionEvent> byUserId = stream
                .selectKey((key, event) -> String.valueOf((event.getUserId())));

        TimeWindows windows = TimeWindows.ofSizeWithNoGrace(Duration.ofSeconds(10));

        KTable<Windowed<String>, UserBillingAggregate> aggregated = byUserId
                .groupByKey(Grouped.with(Serdes.String(), impressionEventSerde))
                .windowedBy(windows)
                .aggregate(
                        () -> UserBillingAggregate.empty(null),
                        (userId, event, agg) -> {
                            long recordTs = event.getTimestamp()
                                    .atZone(ZoneId.systemDefault())
                                    .toInstant()
                                    .toEpochMilli();
                            return agg.add(event, recordTs);
                        },
                        Materialized.with(Serdes.String(), userBillingAggregateSerde)
                );

        aggregated
                .suppress(Suppressed.untilWindowCloses(Suppressed.BufferConfig.unbounded()))
                .toStream()
                .foreach((key, aggregate) -> {
                    long windowStart = key.window().start();
                    long windowEnd = key.window().end();

                    if (aggregate.getUserId() == null) {
                        aggregate = UserBillingAggregate.builder()
                                .userId(aggregate.getUserId())
                                .totalAmount(aggregate.getTotalAmount())
                                .impressionCount(aggregate.getImpressionCount())
                                .firstEventTimestamp(aggregate.getFirstEventTimestamp())
                                .lastEventTimestamp(aggregate.getLastEventTimestamp())
                                .build();
                    }

                    billingService.save(aggregate, windowStart, windowEnd);
                });
    }
}
