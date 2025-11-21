package com.smalldogg.application.stream.processor;

import com.smalldogg.application.stream.domain.cpm.CpmBucket;
import com.smalldogg.application.stream.domain.cpm.CpmChunk;
import com.smalldogg.application.stream.domain.impression.ImpressionEvent;
import com.smalldogg.infrastructure.service.CpmService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.ValueTransformerWithKey;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.StoreBuilder;
import org.apache.kafka.streams.state.Stores;
import org.springframework.stereotype.Component;

import java.time.ZoneId;

@RequiredArgsConstructor
@Component
public class CpmAggregatingProcessor {

    private static final String CPM_STATE_STORE = "cpm-bucket-store";

    private final CpmService cpmService;
    private final Serde<CpmBucket> cpmBucketSerde;

    public void attach(StreamsBuilder builder, KStream<String, ImpressionEvent> stream) {
        StoreBuilder<KeyValueStore<String, CpmBucket>> storeBuilder = Stores.keyValueStoreBuilder(
                Stores.persistentKeyValueStore(CPM_STATE_STORE),
                Serdes.String(),
                cpmBucketSerde
        );

        builder.addStateStore(storeBuilder);

        KStream<String, ImpressionEvent> byUserAndItem = stream
                .selectKey((k, v) -> v.getUserId() + ":" + v.getItemId());

        byUserAndItem
                .transformValues(
                        () -> new CpmValueTransformer(CPM_STATE_STORE),
                        CPM_STATE_STORE
                )
                .filter((key, chunk) -> chunk != null) // 1000개 안 찼으면 null
                .foreach((key, chunk) -> cpmService.save(chunk));

    }


    @RequiredArgsConstructor
    static class CpmValueTransformer implements ValueTransformerWithKey<String, ImpressionEvent, CpmChunk> {
        private final String stateStoreName;
        private KeyValueStore<String, CpmBucket> stateStore;

        @Override
        public void init(ProcessorContext context) {
            this.stateStore = context.getStateStore(stateStoreName);
        }

        @Override
        public CpmChunk transform(String readOnlyKey, ImpressionEvent value) {
            if (value == null) return null;

            CpmBucket bucket = stateStore.get(readOnlyKey);

            if(bucket == null) bucket = CpmBucket.empty();

            Long userId = value.getUserId();
            Long itemId = value.getItemId();

            long timestamp = value.getTimestamp()
                    .atZone(ZoneId.systemDefault())
                    .toInstant()
                    .toEpochMilli();

            bucket.add(value.getAmount(), timestamp);

            CpmChunk chunk = null;
            if (bucket.size() >= 1000) {
                chunk = bucket.popThousandAndBuild(userId, itemId);
            }

            stateStore.put(readOnlyKey, bucket);

            return chunk;
        }

        @Override
        public void close() {}
    }
}
