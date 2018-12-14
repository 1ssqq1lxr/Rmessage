package io.rector.netty.core;


import io.netty.channel.Channel;
import io.rector.netty.core.session.TcpSession;
import reactor.core.publisher.Mono;
import reactor.ipc.netty.NettyConnector;
import reactor.ipc.netty.NettyContext;
import reactor.ipc.netty.NettyInbound;
import reactor.ipc.netty.NettyOutbound;

import java.util.function.Consumer;

public interface Start {

    Start  udp();

    Start  tcp();

    Start  ip(String ip);

    Start  port(int port);

    Start  mqtt();

    Start  websocket();

    Start setAfterNettyContextInit(Consumer<? super NettyContext> afterNettyContextInit);

    Start setAfterChannelInit(Consumer<? super Channel> afterChannelInit);


    <T extends NettyConnector< ? extends NettyInbound,? extends NettyOutbound>> Mono<TcpSession<T>> connect();

}
