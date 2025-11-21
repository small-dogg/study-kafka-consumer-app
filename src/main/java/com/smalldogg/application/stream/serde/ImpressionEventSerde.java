package com.smalldogg.application.stream.serde;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smalldogg.application.stream.domain.impression.ImpressionEvent;
import org.apache.kafka.common.serialization.Serde;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ImpressionEventSerde {
    @Bean
    public Serde<ImpressionEvent> impressionEventSerde(ObjectMapper objectMapper) {
        return new JsonSerde<>(ImpressionEvent.class, objectMapper);
    }
}
