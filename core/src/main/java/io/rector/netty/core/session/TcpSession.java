package io.rector.netty.core.session;

import io.rector.netty.transport.connction.Connection;
import io.rector.netty.transport.connction.RConnection;
import io.rector.netty.transport.socket.Rsocket;
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
public class TcpSession<T extends NettyConnector< ? extends NettyInbound,? extends NettyOutbound>> implements Session {

    private Rsocket<T>  rsocket;

    public TcpSession(Rsocket<T> rsocket) {
        this.rsocket =rsocket;
    }

    public Rsocket<T> getRsocket() {
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
