package com.smalldogg.infrastructure.aggregate;

import com.smalldogg.kafka.message.ImpressionEvent;
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
public class ImpressionRawLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    private String id;
    private Long pid;
    private Long userId;
    private Long itemId;
    private Long amount;
    private LocalDateTime timestamp;

    public static ImpressionRawLog of(ImpressionEvent event) {
        ImpressionRawLog log = new ImpressionRawLog();
        log.setId(event.getId());
        log.setPid(event.getPid());
        log.setUserId(event.getUserId());
        log.setItemId(event.getItemId());
        log.setAmount(event.getAmount());
        log.setTimestamp(event.getTimestamp());
        return log;
    }
}
