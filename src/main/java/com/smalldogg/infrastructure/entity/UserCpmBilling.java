package com.smalldogg.infrastructure.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class UserCpmBilling {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    private Long userId;
    private Long itemId;
    private Long impressionCount;
    private LocalDateTime startTimestamp;
    private LocalDateTime endTimeStamp;
}
