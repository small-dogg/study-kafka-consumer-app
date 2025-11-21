package com.smalldogg.application.stream.processor;

import com.smalldogg.infrastructure.service.LogService;
import com.smalldogg.application.stream.domain.impression.ImpressionEvent;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class RawImpressionSinkProcessor {

    private final LogService service;

    public void attach(KStream<String, ImpressionEvent> source) {
        source.peek((k, v) -> service.save(v));
    }
}
