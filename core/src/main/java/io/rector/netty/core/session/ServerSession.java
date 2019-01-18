package io.rector.netty.core.session;

import io.rector.netty.transport.connction.RConnection;
import io.rector.netty.transport.distribute.OffMessageHandler;
import io.rector.netty.transport.group.GroupCollector;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;
import reactor.ipc.netty.NettyConnector;
import reactor.ipc.netty.NettyInbound;
import reactor.ipc.netty.NettyOutbound;

import java.util.List;

/**
 * @Auther: lxr
 * @Date: 2018/12/6 19:21
 * @Description:
 */
public interface ServerSession  <T extends NettyConnector< ? extends NettyInbound,? extends NettyOutbound>> extends Disposable {

    //查看所有连接
    Mono<List<RConnection>> listConnection();

    //删除连接cache
    Mono<Void> removeConnection(RConnection duplexConnection);

    // 设置key绑定duplexConnection
    Mono<Void> attr(String key, RConnection duplexConnection);

    //删除key绑定duplexConnection
    Mono<Void> rmAttr(String key, RConnection duplexConnection);

    //根据key取出duplexConnection
    Mono<List<RConnection>> keys(String key);


    Mono<Void>   addOfflineHandler(OffMessageHandler offMessageHandler);

    Mono<Void>   addGroupHandler(GroupCollector collector);




}
