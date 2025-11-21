package com.smalldogg.application.stream.serde;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

@RequiredArgsConstructor
public class JsonSerde<T> implements Serde<T> {

    private final Class<T> targetType;
    private final ObjectMapper objectMapper;

    @Override
    public Serializer<T> serializer() {
        return new Serializer<>() {
            @Override
            public byte[] serialize(String topic, T data) {
                try {
                    if (data == null) return null;
                    return objectMapper.writeValueAsBytes(data);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void configure(Map<String, ?> configs, boolean isKey) { }

            @Override
            public void close() { }
        };
    }

    @Override
    public Deserializer<T> deserializer() {
        return new Deserializer<>() {
            @Override
            public T deserialize(String topic, byte[] data) {
                try {
                    if (data == null || data.length == 0) return null;
                    return objectMapper.readValue(data, targetType);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void configure(Map<String, ?> configs, boolean isKey) { }

            @Override
            public void close() { }
        };
    }
}

