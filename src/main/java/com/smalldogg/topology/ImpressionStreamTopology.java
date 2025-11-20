package com.smalldogg.topology;


import com.smalldogg.model.in.ImpressionEvent;
import com.smalldogg.model.out.ImpressionAggResult;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
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

        KStream<String, ImpressionEvent> filter = source.filter(
                ((key, event) -> event != null && event.getAmount() > 150)
        );

        KStream<String, ImpressionEvent> keyedByUserId = filter.selectKey((key, event) -> event.getUserId());

        TimeWindows windows = TimeWindows.ofSizeWithNoGrace(Duration.ofMinutes(5));

        KTable<Windowed<String>, Long> counts = keyedByUserId
                .groupByKey(Grouped.with(Serdes.String(), impressionEventSerde))
                .windowedBy(windows)
                .count(Materialized.as("impression-counts-5m"));

        System.out.println();
//        counts
//                .toStream()
//                .map((windowedKey, count) -> {
//                    String userId = windowedKey.key();
//                    long start = windowedKey.window().start();
//                    long end = windowedKey.window().end();
//                });
        return null;
    }
}
