package io.rector.netty.transport.socket;

import io.reactor.netty.api.codec.Protocol;
import io.rector.netty.config.Config;
import io.rector.netty.config.ServerConfig;
import io.rector.netty.transport.Transport;
import io.rector.netty.transport.connction.RConnection;
import io.rector.netty.transport.distribute.OfflineMessageDistribute;
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


    public abstract Config getConfig();


    protected Supplier<Transport> transport;

    public abstract Supplier<Protocol> getPrptocol();

    public Mono<Rsocket<T>> start(Supplier<OfflineMessageDistribute> offlineMessage) {
        return  Mono.defer(this::get);
    }

    public void close() {
       transport.get().close().subscribe();
    }


    private Mono<? extends Rsocket<T>> get() {
        return  transport.get()
                .connect(getConfig()).doOnNext(next().get()::accept).then(Mono.just(this));
    }

    public abstract Supplier<Consumer<RConnection>>  next();


    public abstract Mono<Void>  removeConnection(RConnection duplexConnection);
}
