package com.smalldogg.kafka.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smalldogg.infrastructure.service.LogService;
import com.smalldogg.kafka.model.ImpressionEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.kafka.support.Acknowledgment;

@Component
@RequiredArgsConstructor
@Slf4j
public class ImpressionEventListener {

    private final LogService logService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = {"impression-events"})
    public void receive(ConsumerRecord<String, String> consumerRecord, Acknowledgment ack) throws JsonProcessingException {

        try {
            ImpressionEvent impressionEvent = objectMapper.readValue(consumerRecord.value(), ImpressionEvent.class);
            log.info("event received : {}", impressionEvent.getId());
            logService.save(impressionEvent);
            log.info("event saved : {}", impressionEvent.getId());

            ack.acknowledge();
        } catch (Exception e) {
            log.error("failed to process event. offset : {}", consumerRecord.offset(), e);
        }
    }
}
