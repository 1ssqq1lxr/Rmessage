package io.rector.netty.core.session;

import io.rector.netty.transport.connction.RConnection;
import io.rector.netty.transport.distribute.OfflineMessageDistribute;
import io.rector.netty.transport.group.GroupCollector;
import io.rector.netty.transport.socket.ServerSocketAdapter;
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
public class TcpServerSession<T extends NettyConnector< ? extends NettyInbound,? extends NettyOutbound>> implements ServerSession<T> {

    private ServerSocketAdapter<T> rsocket;

    public TcpServerSession(ServerSocketAdapter<T> rsocket) {
        this.rsocket =rsocket;
    }

    public ServerSocketAdapter<T> getRsocket() {
        return rsocket;
    }


    private Supplier<OfflineMessageDistribute> offlineMessageDistribute;

    private GroupCollector groupCollector;




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
    public Mono<Void> addOfflineHandler(Supplier<OfflineMessageDistribute> offlineMessageDistribute) {
        this.offlineMessageDistribute=offlineMessageDistribute;
        if(offlineMessageDistribute.get() == null){
            throw  new RuntimeException("offlineMessageDistribute is not null");
        }
        return offlineMessageDistribute.get().storageOfflineMessage(rsocket.reciveOffline());
    }

    @Override
    public Mono<Void> groupHandler(GroupCollector collector) {
        return rsocket.setGroupCollector(collector);
    }

    @Override
    public Mono<Void> closeServer() {
        return Mono.fromRunnable(()->rsocket.getConnections().forEach(rConnection -> rConnection.dispose())).then(rsocket.closeServer());
    }
}
