package io.rector.netty.transport.socket;

import io.rector.netty.config.Protocol;
import io.rector.netty.transport.Transport;
import io.rector.netty.transport.connction.DuplexConnection;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.ipc.netty.NettyConnector;
import reactor.ipc.netty.NettyInbound;
import reactor.ipc.netty.NettyOutbound;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Supplier;

/**
 * @Auther: lxr
 * @Date: 2018/12/9 15:55
 * @Description:
 */
public abstract class Rsocket<T extends NettyConnector< ? extends NettyInbound,? extends NettyOutbound>> {

    protected List<DuplexConnection> connections ;

    protected Supplier<Transport> transport;

    public abstract Supplier<Protocol> getPrptocol();

    public abstract Mono<? extends Rsocket<T>> start();



}
