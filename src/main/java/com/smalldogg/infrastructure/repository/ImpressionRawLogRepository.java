package com.smalldogg.infrastructure.repository;

import com.smalldogg.infrastructure.aggregate.ImpressionRawLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImpressionRawLogRepository extends JpaRepository<ImpressionRawLog, Long> {
}
