package com.smalldogg.domain.log.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
public class ImpressionLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long seq;
    private Long userId;
    private Long totalAmount;
    private LocalDateTime startTimestamp;
    private LocalDateTime endTimestamp;
    private Boolean isCharged = false;
}
