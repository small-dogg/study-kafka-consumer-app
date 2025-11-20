package com.smalldogg.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smalldogg.config.properties.KafkaProperties;
import com.smalldogg.model.in.ImpressionEvent;
import com.smalldogg.model.out.ImpressionAggResult;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;

import java.util.HashMap;
import java.util.Map;


@EnableKafkaStreams
@RequiredArgsConstructor
@Configuration
public class KafkaStreamConfig {

    private final KafkaProperties properties;

    @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    public KafkaStreamsConfiguration kStreamsConfig() {
        Map<String, Object> props = new HashMap<>();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, properties.getStreams().getApplicationId());
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, properties.getBootstrapServers());
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.StringSerde.class);

        KafkaProperties.Streams streams = properties.getStreams();
        if (streams.getProperties() != null) {
            props.putAll(streams.getProperties());
        }

        return new KafkaStreamsConfiguration(props);
    }

    @Bean
    public Serde<ImpressionEvent> impressionEventSerde(ObjectMapper objectMapper) {
        return new JsonSerde<>(ImpressionEvent.class, objectMapper);
    }

    @Bean
    public Serde<ImpressionAggResult> impressionAggResultSerde(ObjectMapper objectMapper) {
        return new JsonSerde<>(ImpressionAggResult.class, objectMapper);
    }
}
