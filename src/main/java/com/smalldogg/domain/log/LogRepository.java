package com.smalldogg.domain.log;

import com.smalldogg.domain.log.entity.ImpressionLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<ImpressionLog, Long> {
}
