package io.rector.netty.core.session;

import io.reactor.netty.api.codec.RConnection;
import io.rector.netty.transport.connection.ConnectionManager;
import io.rector.netty.transport.distribute.UserTransportHandler;
import io.rector.netty.transport.distribute.OffMessageHandler;
import io.rector.netty.transport.group.GroupCollector;
import io.rector.netty.transport.socket.ServerSocketAdapter;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;

/**
 * @Auther: lxr
 * @Date: 2018/12/7 17:33
 * @Description:
 */
public class TcpServerSession implements ServerSession {

    private ServerSocketAdapter rsocket;

    public TcpServerSession(ServerSocketAdapter rsocket) {
        this.rsocket =rsocket;
    }

    public ServerSocketAdapter getRsocket() {
        return rsocket;
    }


    @Override
    public Mono<Set<RConnection>> listConnection() {
        return Mono.just(rsocket.getConnectionManager().getConnections());
    }

    @Override
    public Mono<Void> removeConnection(RConnection duplexConnection) {
        return rsocket.removeConnection(duplexConnection);
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
    public Mono<Set<RConnection>> keys(String user) {
        return  Mono.just(rsocket.getConnectionManager().getUserMultiConnection(user));
    }

    @Override
    public Mono<Void> addOfflineHandler(OffMessageHandler offMessageHandler) {
        return  rsocket.setOffMessageHandler(offMessageHandler);
    }

    @Override
    public Mono<Void> addGroupHandler(GroupCollector collector) {
        return rsocket.setGroupCollector(collector);
    }

    @Override
    public Mono<Void> addUserHandler(UserTransportHandler userHandler) {
        return rsocket.setUserHandler(userHandler);
    }

    @Override
    public Mono<Void> addConnectionManager(ConnectionManager manager) {
        return rsocket.setConnectionManager(manager);
    }

    @Override
    public void dispose() {
        Mono.fromRunnable(()->rsocket.getConnectionManager().getConnections().forEach(rConnection -> rConnection.dispose())).then(rsocket.closeServer()).subscribe();
    }
}
