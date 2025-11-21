package com.smalldogg.config;

import com.smalldogg.application.stream.processor.CpmAggregatingProcessor;
import com.smalldogg.application.stream.processor.RawImpressionSinkProcessor;
import com.smalldogg.application.stream.processor.UserBillingAggregationProcessor;
import com.smalldogg.application.stream.domain.impression.ImpressionEvent;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;

@Configuration
@EnableKafkaStreams
public class ImpressionTopologyConfig {

    @Bean
    public Topology impressionTopology(
            StreamsBuilder builder,
            RawImpressionSinkProcessor rawImpressionSinkProcessor,
            UserBillingAggregationProcessor billingAggregationProcessor,
            CpmAggregatingProcessor cpmAggregatingProcessor
    ) {

        KStream<String, ImpressionEvent> stream = builder.stream("impression_events");

        rawImpressionSinkProcessor.attach(stream);
        billingAggregationProcessor.attach(stream);
        cpmAggregatingProcessor.attach(builder, stream);

        return builder.build();
    }
}
