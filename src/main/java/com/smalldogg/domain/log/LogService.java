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

        ImpressionLog impressionLog = logRepository.findByUserIdAndStartTimestampAndEndTimestamp(agg.getUserId(), agg.getStartTimestamp(), agg.getEndTimestamp())
                .orElseGet(() -> {
                    ImpressionLog newImpressionLog = new ImpressionLog();
                    newImpressionLog.setUserId(agg.getUserId());
                    newImpressionLog.setStartTimestamp(agg.getStartTimestamp());
                    newImpressionLog.setEndTimestamp(agg.getEndTimestamp());
                    return newImpressionLog;
                });

        impressionLog.setTotalAmount(agg.getTotalAmount());
        logRepository.save(impressionLog);
    }

}
