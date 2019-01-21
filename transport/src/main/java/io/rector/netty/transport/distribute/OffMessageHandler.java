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


     // 存贮离线消息
     Mono<Void> storageOfflineMessage(Flux<OfflineMessage> message);

     // 获取离线消息
     Flux<TransportMessage> getToMessages(String to, ClientType clientType);





}
