package com.smalldogg.application.stream.domain.cpm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CpmPendingImpression {
    private long amount;
    private long timestamp;
}