package io.rector.netty.transport.socket;

import io.netty.buffer.ByteBuf;
import io.rector.netty.config.Protocol;
import io.rector.netty.flow.frame.Frame;
import io.rector.netty.flow.plugin.PluginRegistry;
import io.rector.netty.flow.plugin.Plugins;
import io.rector.netty.transport.ServerTransport;
import io.rector.netty.transport.Transport;
import io.rector.netty.transport.connction.RConnection;
import reactor.core.publisher.Mono;
import reactor.ipc.netty.NettyConnector;
import reactor.ipc.netty.NettyInbound;
import reactor.ipc.netty.NettyOutbound;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @Auther: lxr
 * @Date: 2018/12/9 15:55
 * @Description:
 */
public abstract class Rsocket<T extends NettyConnector< ? extends NettyInbound,? extends NettyOutbound>> {

    protected   PluginRegistry registry = Plugins.defaultPlugins();

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




}
