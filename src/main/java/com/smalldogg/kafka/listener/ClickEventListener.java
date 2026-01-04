package com.smalldogg.kafka.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smalldogg.infrastructure.service.LogService;
import com.smalldogg.kafka.message.ClickEvent;
import com.smalldogg.kafka.message.ImpressionEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ClickEventListener {
    private final LogService logService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = {"partner-billing-aggregation"})
    public void receive(ConsumerRecord<String, String> consumerRecord, Acknowledgment ack) throws JsonProcessingException {
        log.debug("now offset : {}", consumerRecord.offset());

        try {
            ClickEvent clickEvent = objectMapper.readValue(consumerRecord.value(), ClickEvent.class);
            log.info("event received : {}", clickEvent.toString());
            logService.bill(clickEvent);
            log.info("event saved : {}", clickEvent.toString());

            ack.acknowledge();
        } catch (Exception e) {
            log.error("failed to process event. offset : {}", consumerRecord.offset(), e);
        }
    }
}
