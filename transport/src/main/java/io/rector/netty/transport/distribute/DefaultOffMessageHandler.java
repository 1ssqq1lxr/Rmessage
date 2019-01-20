package io.rector.netty.transport.distribute;


import io.reactor.netty.api.codec.ClientType;
import io.reactor.netty.api.codec.OfflineMessage;
import io.reactor.netty.api.codec.TransportMessage;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 默认离线消息
 */
public class DefaultOffMessageHandler implements OffMessageHandler {

    /**
     *
     * @param message 离线消息 针对个人 from 来自哪里的 example 【 point -> point 】【 point -> group】
     * @return
     */
    @Override
    public Mono<Void> storageOfflineMessage(Flux<OfflineMessage> message) {

        return message.doOnNext(s->{}).then();
    }

    @Override
    public Flux<TransportMessage> getToMessages(String to, ClientType clientType) {
        return Flux.empty();
    }
}
