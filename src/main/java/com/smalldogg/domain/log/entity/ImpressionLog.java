package com.smalldogg.domain.log.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_impression_log_user_window",
                        columnNames = {"userId", "startTimestamp", "endTimestamp"}
                )
        }
)
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
