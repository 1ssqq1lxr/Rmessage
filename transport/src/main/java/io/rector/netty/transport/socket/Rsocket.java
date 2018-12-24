package io.rector.netty.transport.socket;

import io.rector.netty.config.Protocol;
import io.rector.netty.transport.Transport;
import io.rector.netty.transport.connction.Connection;
import io.rector.netty.transport.connction.RConnection;
import reactor.core.publisher.Mono;
import reactor.ipc.netty.NettyConnector;
import reactor.ipc.netty.NettyInbound;
import reactor.ipc.netty.NettyOutbound;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @Auther: lxr
 * @Date: 2018/12/9 15:55
 * @Description:
 */
public abstract class Rsocket<T extends NettyConnector< ? extends NettyInbound,? extends NettyOutbound>> {


    protected Supplier<Transport<T>> transport;

    public abstract Supplier<Protocol> getPrptocol();

    public Mono<Rsocket<T>> start() {
        return  Mono.defer(this::get);
    }

    public void close() {
       transport.get().close().subscribe();
    }


    private Mono<? extends Rsocket<T>> get() {
        Transport<T> tTransport=transport.get();
        return  transport.get()
                .connect().doOnNext(next().apply(tTransport)).then(Mono.just(this));
    }

    public abstract Function<Transport<T>,Consumer<RConnection>>  next();


    public abstract void removeConnection(RConnection duplexConnection);
}
