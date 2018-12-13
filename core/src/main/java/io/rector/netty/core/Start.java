package io.rector.netty.core;


import reactor.core.publisher.Mono;
import reactor.ipc.netty.NettyConnector;
import reactor.ipc.netty.NettyInbound;
import reactor.ipc.netty.NettyOutbound;
import reactor.ipc.netty.options.ServerOptions;

import java.util.function.Consumer;

public interface Start {

    Start  udp();

    Start  tcp();

    Start  ip(String ip);

    Start  port(int port);

    Start  mqtt();

    Start  websocket();

    Start options(Consumer<ServerOptions.Builder<?>> options);

    <T extends NettyConnector< ? extends NettyInbound,? extends NettyOutbound>> Mono<PersistSession<T>> connect(Class<T> classT);

}
