package io.rector.netty.transport.distribute;

import io.reactor.netty.api.codec.TransportMessage;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.function.Function;

public interface ServerMessageHandler {

     Mono<Void> sendOne(TransportMessage message, Mono<Void> offline);

     Mono<Void>  sendGroup(TransportMessage message, Function<String, Mono<Void>> consumer);

     Mono<Void>  sendPong(TransportMessage message);

     Mono<Void>  sendOffline(TransportMessage transportMessage,Set<String> users);

}
