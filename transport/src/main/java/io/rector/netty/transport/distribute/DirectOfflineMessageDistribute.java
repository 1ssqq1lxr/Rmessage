package io.rector.netty.transport.distribute;


import io.reactor.netty.api.codec.TransportMessage;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 离线通知
 */
public class DirectOfflineMessageDistribute implements OfflineMessageDistribute {

    @Override
    public Mono<Void> storageOfflineMessage(Flux<TransportMessage> message) {
        return null;
    }

    @Override
    public Mono<List<TransportMessage>> getToMessages(String to) {
        return null;
    }
}
