package io.rector.netty.core;

import io.rector.netty.transport.connction.Connection;
import io.rector.netty.transport.socket.Rsocket;
import io.rector.netty.transport.socket.SocketFactory;
import reactor.core.publisher.Mono;
import reactor.ipc.netty.NettyConnector;
import reactor.ipc.netty.NettyInbound;
import reactor.ipc.netty.NettyOutbound;

import java.util.List;
import java.util.function.Supplier;

/**
 * @Auther: lxr
 * @Date: 2018/12/7 17:33
 * @Description:
 */
public class PersistSession<T extends NettyConnector< ? extends NettyInbound,? extends NettyOutbound>> implements Session{

    private Supplier<Rsocket<T>>  rsocket;

    public PersistSession(Rsocket<T> rsocket) {
        this.rsocket = ()->rsocket;
    }

    public Supplier<Rsocket<T>> getRsocket() {
        return rsocket;
    }


    @Override
    public Mono<List<Connection>> listConnection() {
        return null;
    }

    @Override
    public Mono<Void> removeConnection(Connection duplexConnection) {
        return null;
    }

    @Override
    public Mono<Void> attr(String key, Connection duplexConnection) {
        return null;
    }

    @Override
    public Mono<Void> rmAttr(String key, Connection duplexConnection) {
        return null;
    }

    @Override
    public Mono<List<Connection>> keys(String key) {
        return null;
    }
}
