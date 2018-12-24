package io.rector.netty.core.session;

import io.rector.netty.transport.connction.RConnection;
import io.rector.netty.transport.socket.ServerSocketAdapter;
import reactor.core.publisher.Mono;
import reactor.ipc.netty.NettyConnector;
import reactor.ipc.netty.NettyInbound;
import reactor.ipc.netty.NettyOutbound;

import java.util.List;

/**
 * @Auther: lxr
 * @Date: 2018/12/7 17:33
 * @Description:
 */
public class TcpServerSession<T extends NettyConnector< ? extends NettyInbound,? extends NettyOutbound>> implements ServerSession {

    private ServerSocketAdapter<T> rsocket;

    public TcpServerSession(ServerSocketAdapter<T> rsocket) {
        this.rsocket =rsocket;
    }

    public ServerSocketAdapter<T> getRsocket() {
        return rsocket;
    }


    @Override
    public Mono<List<RConnection>> listConnection() {
        return Mono.just(rsocket.getConnections());
    }

    @Override
    public Mono<Void> removeConnection(RConnection duplexConnection) {
        return Mono.defer(()->{
            rsocket.removeConnection(duplexConnection);
            return Mono.empty();
        });
    }



    @Override
    public Mono<Void> attr(String key, RConnection duplexConnection) {
        return null;
    }

    @Override
    public Mono<Void> rmAttr(String key, RConnection duplexConnection) {
        return null;
    }

    @Override
    public Mono<List<RConnection>> keys(String key) {
        return null;
    }
}
