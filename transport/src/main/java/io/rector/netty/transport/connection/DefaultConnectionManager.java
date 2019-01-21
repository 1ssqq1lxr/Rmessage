package io.rector.netty.transport.connection;

import io.reactor.netty.api.codec.RConnection;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @Auther: lxr
 * @Date: 2019/1/21 14:22
 * @Description:
 */
public class DefaultConnectionManager implements  ConnectionManager {
    @Override
    public Mono<Void> acceptConnection(ConnectionAcceptor acceptor) {
        return null;
    }

    @Override
    public Mono<Void> removeConnection(ConnectionAcceptor acceptor) {
        return null;
    }

    @Override
    public Flux<RConnection> getUserMultiConnection(String user) {
        return null;
    }
}
