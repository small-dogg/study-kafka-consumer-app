package com.smalldogg.application.stream.domain.cpm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CpmBucket {

    private List<CpmPendingImpression> pending;

    public static CpmBucket empty() {
        return CpmBucket.builder()
                .pending(new ArrayList<>())
                .build();
    }

    public void add(long amount, long timestamp) {
        pending.add(new CpmPendingImpression(amount, timestamp));
    }

    public int size() {
        return pending.size();
    }

    public CpmChunk popThousandAndBuild(Long userId, Long itemId) {
        if (pending.size() < 1000) {
            return null;
        }

        long totalAmount = 0L;
        long firstTs = Long.MAX_VALUE;
        long lastTs = Long.MIN_VALUE;

        for (int i = 0; i < 1000; i++) {
            CpmPendingImpression p = pending.get(i);
            totalAmount += p.getAmount();
            firstTs = Math.min(firstTs, p.getTimestamp());
            lastTs = Math.max(lastTs, p.getTimestamp());
        }

        pending = new ArrayList<>(pending.subList(1000, pending.size()));

        return CpmChunk.builder()
                .userId(userId)
                .itemId(itemId)
                .impressionCount(1000)
                .totalAmount(totalAmount)
                .firstImpressionTimestamp(firstTs)
                .lastImpressionTimestamp(lastTs)
                .build();
    }
}
