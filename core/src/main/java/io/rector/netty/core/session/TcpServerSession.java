package io.rector.netty.core.session;

import io.rector.netty.transport.connction.RConnection;
import io.rector.netty.transport.distribute.OffMessageHandler;
import io.rector.netty.transport.group.GroupCollector;
import io.rector.netty.transport.socket.ServerSocketAdapter;
import reactor.core.publisher.Mono;

import java.util.List;

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
    public Mono<List<RConnection>> listConnection() {
        return Mono.just(rsocket.getConnections());
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
    public Mono<List<RConnection>> keys(String key) {
        return  Mono.just(rsocket.getConnections());
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
    public void dispose() {
        Mono.fromRunnable(()->rsocket.getConnections().forEach(rConnection -> rConnection.dispose())).then(rsocket.closeServer()).subscribe();
    }
}
