package io.rector.netty.transport.distribute;


import io.reactor.netty.api.codec.TransportMessage;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 离线通知
 */
public interface OfflineMessageDistribute {


     Mono<Void> storageOfflineMessage(TransportMessage message);


     Mono<List<TransportMessage>> getToMessages(String to);







}
