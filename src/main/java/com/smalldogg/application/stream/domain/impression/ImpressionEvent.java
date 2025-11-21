package com.smalldogg.application.stream.domain.impression;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ImpressionEvent {
    private String id;
    private Long pid;
    private Long userId;
    private Long itemId;
    private Long amount;
    private LocalDateTime timestamp;
}
