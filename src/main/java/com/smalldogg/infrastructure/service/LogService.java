package com.smalldogg.infrastructure.service;

import com.smalldogg.infrastructure.entity.ImpressionRawLog;
import com.smalldogg.infrastructure.repository.ImpressionRawLogRepository;
import com.smalldogg.kafka.model.ImpressionEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class LogService {
    private final ImpressionRawLogRepository ImpressionRawLogRepository;

    @Transactional
    public void save(ImpressionEvent event) {
        ImpressionRawLog impressionRawLog = ImpressionRawLog.of(event);
        ImpressionRawLogRepository.save(impressionRawLog);
    }
}
