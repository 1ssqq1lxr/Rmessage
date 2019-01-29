package io.rector.netty.transport.distribute.wrapper;

import io.reactor.netty.api.codec.TransportMessage;
import io.rector.netty.transport.distribute.ServerMessageHandler;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.function.Function;

/**
 * @Auther: lxr
 * @Date: 2019/1/29 14:37
 * @Description:
 */
public class WrapperServerMessageHandler implements ServerMessageHandler {

    private final  ServerMessageHandler serverMessageHandler;

    public WrapperServerMessageHandler(ServerMessageHandler serverMessageHandler) {
        this.serverMessageHandler = serverMessageHandler;
    }


    @Override
    public Mono<Void> sendOne(TransportMessage message, Mono<Void> offline) {
        return null;
    }

    @Override
    public Mono<Void> sendGroup(TransportMessage message, Function<String, Mono<Void>> consumer) {
        return null;
    }

    @Override
    public Mono<Void> sendPong(TransportMessage message) {
        return serverMessageHandler.sendPong(message);
    }

    @Override
    public Mono<Void> sendOffline(TransportMessage transportMessage, Set<String> users) {
        return serverMessageHandler.sendOffline(transportMessage,users);
    }
}
