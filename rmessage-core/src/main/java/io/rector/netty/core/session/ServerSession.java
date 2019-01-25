package io.rector.netty.core.session;

import io.reactor.netty.api.codec.RConnection;
import io.rector.netty.transport.connection.ConnectionManager;
import io.rector.netty.transport.distribute.OffMessageHandler;
import io.rector.netty.transport.distribute.UserTransportHandler;
import io.rector.netty.transport.group.GroupCollector;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;

import java.util.Set;

/**
 * @Auther: lxr
 * @Date: 2018/12/6 19:21
 * @Description:
 */
public interface ServerSession extends Disposable {

    //查看所有连接
    Mono<Set<RConnection>> listConnection();

    //删除连接cache
    Mono<Void> removeConnection(RConnection duplexConnection);

    // 设置key绑定duplexConnection
    Mono<Void> attr(String key, RConnection duplexConnection);

    //删除key绑定duplexConnection
    Mono<Void> rmAttr(String key, RConnection duplexConnection);

    //根据key取出duplexConnection
    Mono<Set<RConnection>> keys(String key);

    // 离线处理
    Mono<Void>   addOfflineHandler(OffMessageHandler offMessageHandler);

    // 群组用户关系管理
    Mono<Void>   addGroupHandler(GroupCollector collector);

    //
    Mono<Void>   addUserHandler(UserTransportHandler userHandler);

    Mono<Void>   addConnectionManager(ConnectionManager manager);




}
