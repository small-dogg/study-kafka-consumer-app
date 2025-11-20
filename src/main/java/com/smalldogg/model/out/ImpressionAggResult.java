package com.smalldogg.model.out;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ImpressionAggResult {
    private Long pid;
    private String userId;
    private Integer totalAmount;
    private String itemId;
    private LocalDateTime startTimestamp;
    private LocalDateTime endTimestamp;
}
