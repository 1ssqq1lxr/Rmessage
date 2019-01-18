package io.rector.netty.transport.distribute;


import io.reactor.netty.api.codec.ClientType;
import io.reactor.netty.api.codec.OfflineMessage;
import io.reactor.netty.api.codec.TransportMessage;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 离线通知
 */
public interface OffMessageHandler {


     Mono<Void> storageOfflineMessage(Flux<OfflineMessage> message);


     Flux<TransportMessage> getToMessages(String to, ClientType clientType);





}
