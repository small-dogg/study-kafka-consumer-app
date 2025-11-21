package com.smalldogg.application.stream.domain.billing;

import com.smalldogg.application.stream.domain.impression.ImpressionEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserBillingAggregate {
    private Long userId;
    private long totalAmount;
    private long impressionCount;
    private long firstEventTimestamp;
    private long lastEventTimestamp;

    public static UserBillingAggregate empty(Long userId) {
        return UserBillingAggregate.builder()
                .userId(userId)
                .totalAmount(0L)
                .firstEventTimestamp(0L)
                .lastEventTimestamp(0L)
                .build();
    }

    public UserBillingAggregate add(ImpressionEvent event, long recordTimestamp) {
        long newTotal = this.totalAmount + event.getAmount();
        long newCount = this.impressionCount + 1;

        long newFirst = (this.impressionCount == 0)
                ? recordTimestamp
                : Math.min(this.firstEventTimestamp, recordTimestamp);

        long newLast = Math.max(this.lastEventTimestamp, recordTimestamp);

        return UserBillingAggregate.builder()
                .userId(event.getUserId())
                .totalAmount(newTotal)
                .impressionCount(newCount)
                .firstEventTimestamp(newFirst)
                .lastEventTimestamp(newLast)
                .build();
    }
}
