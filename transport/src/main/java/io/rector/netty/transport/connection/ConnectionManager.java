package io.rector.netty.transport.connection;

import io.reactor.netty.api.codec.RConnection;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @Auther: lxr
 * @Date: 2019/1/21 11:53
 * @Description:
 */
public interface ConnectionManager {

    Mono<Void>   acceptConnection(ConnectionAcceptor acceptor);

    Mono<Void>   removeConnection(ConnectionAcceptor acceptor);

    Flux<RConnection>  getUserMultiConnection(String user);

}
