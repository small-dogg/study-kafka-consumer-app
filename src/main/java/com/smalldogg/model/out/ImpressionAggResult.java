package com.smalldogg.model.out;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ImpressionAggResult {
    private String userId;
    private Long totalAmount;
    private long startTimestamp;
    private long endTimestamp;
}
