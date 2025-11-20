package com.smalldogg.config.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@Getter
//@AllArgsConstructor
@ConfigurationProperties(prefix = "spring.kafka")
public class KafkaProperties {

    private String bootstrapServers;
    private Streams streams;

    public KafkaProperties(String bootstrapServers, Streams streams) {
        this.bootstrapServers = bootstrapServers;
        this.streams = streams;
    }

    @Getter
    public static class Streams {
        private String applicationId;
        private String clientId;
        private Map<String, Object> properties;

        public Streams(String applicationId, String clientId, Map<String, Object> properties) {
            this.applicationId = applicationId;
            this.clientId = clientId;
            this.properties = properties;
        }
    }
}
