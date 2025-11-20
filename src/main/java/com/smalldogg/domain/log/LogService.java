package com.smalldogg.domain.log;

import com.smalldogg.domain.log.entity.ImpressionLog;
import com.smalldogg.model.out.ImpressionAggResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class LogService {
    private final LogRepository logRepository;

    @Transactional
    public void saveLog(ImpressionAggResult agg) {
        ImpressionLog impressionLog = new ImpressionLog();
        impressionLog.setUserId(agg.getUserId());
        impressionLog.setStartTimestamp(agg.getStartTimestamp());
        impressionLog.setEndTimestamp(agg.getEndTimestamp());
        impressionLog.setTotalAmount(agg.getTotalAmount());
        logRepository.save(impressionLog);
    }

}
