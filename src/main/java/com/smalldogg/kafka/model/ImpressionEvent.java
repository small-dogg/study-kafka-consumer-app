package com.smalldogg.kafka.model;

import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class ImpressionEvent {
    private String id;
    private Long pid;
    private Long userId;
    private Long amount;
    private Long itemId;
    private LocalDateTime timestamp;
}
