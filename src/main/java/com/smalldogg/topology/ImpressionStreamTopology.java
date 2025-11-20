package com.smalldogg.topology;


import com.smalldogg.model.in.ImpressionEvent;
import com.smalldogg.model.out.ImpressionAggResult;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class ImpressionStreamTopology {
    private static final String INPUT_TOPIC = "impression-events";
    private static final String OUTPUT_TOPIC = "impression-events-agg-5m";

    @Bean
    public KStream<String, ImpressionEvent> impressionKStream(StreamsBuilder builder, Serde<ImpressionEvent> impressionEventSerde, Serde<ImpressionAggResult> resultSerde) {

        KStream<String, ImpressionEvent> source = builder.stream(INPUT_TOPIC, Consumed.with(Serdes.String(), impressionEventSerde));

        TimeWindows windows = TimeWindows.ofSizeWithNoGrace(Duration.ofSeconds(5));

        KStream<String, ImpressionEvent> filter = source.filter(
                        ((key, event) -> event != null && event.getAmount() > 150))
                .peek((k, v) -> System.out.println("Filtered::" + v.getUserId()));


        KStream<String, ImpressionEvent> keyedByUserId = filter.selectKey((key, event) -> event.getUserId());
        KTable<Windowed<String>, Long> amountByUserId = keyedByUserId
                .mapValues(ImpressionEvent::getAmount)
                .groupByKey(Grouped.with(Serdes.String(), Serdes.Long()))
                .windowedBy(windows)
                .reduce(Long::sum);
        KStream<String, ImpressionAggResult> resultStreamByUserId =
                amountByUserId
                        .toStream()
                        .map((windowedKey, amount) -> {
                            String userId = windowedKey.key();
                            long start = windowedKey.window().start();
                            long end = windowedKey.window().end();

                            System.out.println(
                                    "[AGG] userId=" + userId + ", start=" + start + ", end=" + end + ", amount=" + amount
                            );

                            ImpressionAggResult result = new ImpressionAggResult(
                                    userId, amount, start, end
                            );
                            return KeyValue.pair(userId, result);
                        });

        KStream<String, ImpressionEvent> keyedByItemId = filter.selectKey((key, event) -> event.getItemId());
        KTable<Windowed<String>, Long> amountByItemId = keyedByItemId
                .mapValues(ImpressionEvent::getAmount)
                .groupByKey(Grouped.with(Serdes.String(), Serdes.Long()))
                .windowedBy(windows)
                .reduce(Long::sum);
        KStream<String, ImpressionAggResult> resultStreamByItemId =
                amountByItemId
                        .toStream()
                        .map((windowedKey, amount) -> {
                            String itemId = windowedKey.key();
                            long start = windowedKey.window().start();
                            long end = windowedKey.window().end();

                            System.out.println(
                                    "[AGG] itemId=" + itemId + ", start=" + start + ", end=" + end + ", amount=" + amount
                            );

                            ImpressionAggResult result = new ImpressionAggResult(
                                    itemId, amount, start, end
                            );
                            return KeyValue.pair(itemId, result);
                        });


        return null;
    }
}
