package com.smalldogg.application.stream.serde;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smalldogg.application.stream.domain.billing.UserBillingAggregate;
import org.apache.kafka.common.serialization.Serde;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class UserBillingAggregateSerde {
    @Bean
    public Serde<UserBillingAggregate> userBillingAggregateSerde(ObjectMapper objectMapper) {
        return new JsonSerde<>(UserBillingAggregate.class, objectMapper);
    }
}
