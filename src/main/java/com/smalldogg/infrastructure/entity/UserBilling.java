package com.smalldogg.infrastructure.entity;

import com.smalldogg.application.stream.domain.billing.UserBillingAggregate;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;

@Entity
@Getter
@Setter
public class UserBilling {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    private Long userId;
    private Long totalAmount;
    private Long impressionCount;
    private LocalDateTime startTimestamp;
    private LocalDateTime endTimestamp;

    public static UserBilling of(UserBillingAggregate aggregate) {
        UserBilling userBilling = new UserBilling();
        userBilling.setUserId(aggregate.getUserId());
        userBilling.setTotalAmount(aggregate.getTotalAmount());
        userBilling.setImpressionCount(aggregate.getImpressionCount());
        userBilling.setStartTimestamp(getLocalDateTimeByEpoch(aggregate.getFirstEventTimestamp()));
        userBilling.setEndTimestamp(getLocalDateTimeByEpoch(aggregate.getLastEventTimestamp()));
        return userBilling;
    }

    private static LocalDateTime getLocalDateTimeByEpoch(long epoch) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(epoch),
                TimeZone.getDefault().toZoneId());
    }
}
