package com.smalldogg.model.in;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ImpressionEvent {
    private String id;
    private Long pid;
    private Long userId;
    private Long amount;
    private String itemId;
    private LocalDateTime timestamp;
}
