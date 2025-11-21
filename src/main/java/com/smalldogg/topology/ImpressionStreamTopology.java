package com.smalldogg.topology;


import com.smalldogg.domain.log.LogService;
import com.smalldogg.model.in.ImpressionEvent;
import com.smalldogg.model.out.ImpressionAggResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.time.ZoneId;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class ImpressionStreamTopology {

    private final LogService logService;

    private static final String INPUT_TOPIC = "impression-events";
    private static final String OUTPUT_TOPIC = "impression-events-agg-5m";

    @Bean
    public KStream<String, ImpressionEvent> impressionKStream(StreamsBuilder builder, Serde<ImpressionEvent> impressionEventSerde, Serde<ImpressionAggResult> impressionAggResultSerde) {

        KStream<String, ImpressionEvent> source = builder.stream(INPUT_TOPIC, Consumed.with(Serdes.String(), impressionEventSerde));

        // 1. uuidv7방식의 id로 key 설정
        KStream<String, ImpressionEvent> keyedByUserId = source
                .selectKey((key, event) -> event.getId());

        // 2. ImpressionEvent -> ImpressionAggResult 1건짜리로 변환
        KStream<String, ImpressionAggResult> mapped = keyedByUserId
                .mapValues(event -> {

                    return new ImpressionAggResult(
                            event.getUserId(),
                            event.getAmount(),
                            event.getTimestamp(),
                            event.getTimestamp()
                    );
                });

        // 윈도우 정의
        TimeWindows windows = TimeWindows.ofSizeWithNoGrace(Duration.ofSeconds(3));

        // 3. userId + window 기준으로 reduce
        KTable<Windowed<String>, ImpressionAggResult> aggTable = mapped
                .groupByKey(Grouped.with(Serdes.String(), impressionAggResultSerde))
                .windowedBy(windows)
                .reduce(
                        // agg: 지금까지 누적된 값
                        // curr: 새로 들어온 값
                        (agg, curr) -> new ImpressionAggResult(
                                curr.getUserId(),
                                agg.getTotalAmount() + curr.getTotalAmount(),
                                agg.getStartTimestamp().isBefore(curr.getStartTimestamp())?agg.getStartTimestamp():curr.getStartTimestamp(),
                                agg.getStartTimestamp().isAfter(curr.getStartTimestamp())?agg.getStartTimestamp():curr.getStartTimestamp()
                        )
                );

        aggTable
                .toStream()
                .foreach((key, result) -> logService.saveLog(result));

        return null;
    }
}
