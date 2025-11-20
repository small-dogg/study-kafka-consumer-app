package com.smalldogg.model.out;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ImpressionAggResult {
    private Long userId;
    private Long totalAmount;
    private LocalDateTime startTimestamp;
    private LocalDateTime endTimestamp;
}
