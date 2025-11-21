package com.smalldogg.domain.log;

import com.smalldogg.domain.log.entity.ImpressionLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface LogRepository extends JpaRepository<ImpressionLog, Long> {
    Optional<ImpressionLog> findByUserIdAndStartTimestampAndEndTimestamp(Long userId, LocalDateTime startTimestamp, LocalDateTime endTimestamp);
}
