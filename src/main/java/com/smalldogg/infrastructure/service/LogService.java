package com.smalldogg.infrastructure.service;

import com.smalldogg.infrastructure.aggregate.ImpressionRawLog;
import com.smalldogg.infrastructure.repository.ImpressionRawLogRepository;
import com.smalldogg.kafka.message.ClickEvent;
import com.smalldogg.kafka.message.ImpressionEvent;
import com.smalldogg.store.CashStore;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class LogService {
    private final ImpressionRawLogRepository ImpressionRawLogRepository;
    private final CashStore cashStore;

    @Transactional
    public void save(ImpressionEvent event) {
        ImpressionRawLog impressionRawLog = ImpressionRawLog.of(event);
        ImpressionRawLogRepository.save(impressionRawLog);
    }

    @Transactional
    public void bill(ClickEvent event) {



        cashStore.deductCash(
                new DeductCashCommand()
        );

        new ApplicationEventPublisher().publishEvent();
    }
}
