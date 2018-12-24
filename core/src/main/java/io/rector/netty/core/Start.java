package io.rector.netty.core;


import io.netty.channel.Channel;
import io.rector.netty.core.session.TcpServerSession;
import io.rector.netty.flow.plugin.FrameInterceptor;
import reactor.core.publisher.Mono;
import reactor.ipc.netty.NettyConnector;
import reactor.ipc.netty.NettyContext;
import reactor.ipc.netty.NettyInbound;
import reactor.ipc.netty.NettyOutbound;

import java.util.function.Consumer;
import java.util.function.Supplier;

public interface Start {

    Start  tcp();

    Start  ip(String ip);

    Start  port(int port);

    Start  interceptor(FrameInterceptor... frameInterceptor);

    Start  websocket();

    Start setAfterNettyContextInit(Consumer<? super NettyContext> afterNettyContextInit);

    Start setAfterChannelInit(Consumer<? super Channel> afterChannelInit);

    Start onReadIdle(Long l);

    Start onReadIdle(Long l, Supplier<Runnable> readLe);

    Start onWriteIdle(Long l);

    Start onWriteIdle(Long l, Supplier<Runnable> writeLe);


    <T extends NettyConnector< ? extends NettyInbound,? extends NettyOutbound>> Mono<TcpServerSession<T>> connect();

}
