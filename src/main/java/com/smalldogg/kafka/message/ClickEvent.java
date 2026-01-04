package com.smalldogg.kafka.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
public class ClickEvent {
    @JsonProperty("partnerId")
    private Long partnerId;

    @JsonProperty("aggregatedAmount")
    private Long aggregatedAmount;  // 정상 클릭 집계 금액

    @JsonProperty("fraudulentAmount")
    private Long fraudulentAmount;  // 부정클릭 집계 금액 (10회 초과)

    @JsonProperty("totalBillingAmount")
    private Long totalBillingAmount;  // 총 과금액 (aggregatedAmount)

    @JsonProperty("windowStart")
    private LocalDateTime windowStart;

    @JsonProperty("windowEnd")
    private LocalDateTime windowEnd;
}
