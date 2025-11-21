package com.smalldogg.application.stream.domain.cpm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CpmChunk {

    private Long userId;
    private String itemId;

    private long impressionCount;
    private long totalAmount;

    private long firstImpressionTimestamp;
    private long lastImpressionTimestamp;
}