package com.smalldogg.application.stream.serde;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smalldogg.application.stream.domain.billing.UserBillingAggregate;
import com.smalldogg.application.stream.domain.cpm.CpmBucket;
import com.smalldogg.application.stream.domain.cpm.CpmChunk;
import com.smalldogg.application.stream.domain.impression.ImpressionEvent;
import org.apache.kafka.common.serialization.Serde;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SerdeConfig {
    @Bean
    public Serde<CpmChunk> cpmChunkSerde(ObjectMapper objectMapper) {
        return new JsonSerde<>(CpmChunk.class, objectMapper);
    }
    @Bean
    public Serde<CpmBucket> cpmBucketSerde(ObjectMapper objectMapper) {
        return new JsonSerde<>(CpmBucket.class, objectMapper);
    }

    @Bean
    public Serde<ImpressionEvent> impressionEventSerde(ObjectMapper objectMapper) {
        return new JsonSerde<>(ImpressionEvent.class, objectMapper);
    }
    
    @Bean
    public Serde<UserBillingAggregate> userBillingAggregateSerde(ObjectMapper objectMapper) {
        return new JsonSerde<>(UserBillingAggregate.class, objectMapper);
    }
}
