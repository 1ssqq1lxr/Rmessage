package io.rector.netty.transport.socket;

import io.rector.netty.config.Protocol;
import io.rector.netty.transport.ServerTransport;
import io.rector.netty.transport.Transport;
import io.rector.netty.transport.connction.RConnection;
import reactor.core.publisher.Mono;
import reactor.ipc.netty.NettyConnector;
import reactor.ipc.netty.NettyInbound;
import reactor.ipc.netty.NettyOutbound;
import reactor.ipc.netty.tcp.TcpServer;

import java.util.List;
import java.util.function.Supplier;

/**
 * @Auther: lxr
 * @Date: 2018/12/9 15:55
 * @Description:
 */
public abstract class Rsocket<T extends NettyConnector< ? extends NettyInbound,? extends NettyOutbound>> {

    protected List<RConnection> connections ;

    protected Supplier<Transport<T>> transport;

    public abstract Supplier<Protocol> getPrptocol();

    public Mono<Rsocket<T>> start() {
        return  Mono.defer(()->{
            transport.get()
                    .connect()
                    .doOnNext(duplexConnection -> {
                        duplexConnection.onClose(()->connections.remove(duplexConnection));
                        connections.add(duplexConnection);
                    });
            return Mono.just(this);
        });
    }

    public void close() {
       transport.get().close();
    }


}
