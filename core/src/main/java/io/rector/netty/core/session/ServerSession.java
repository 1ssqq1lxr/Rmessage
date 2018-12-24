package io.rector.netty.core.session;

import io.rector.netty.transport.connction.Connection;
import io.rector.netty.transport.connction.RConnection;
import io.rector.netty.transport.socket.SocketFactory;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Supplier;

/**
 * @Auther: lxr
 * @Date: 2018/12/6 19:21
 * @Description:
 */
public interface ServerSession {

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


}
