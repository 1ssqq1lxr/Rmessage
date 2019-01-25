package io.rector.netty.transport.connection;

import io.reactor.netty.api.codec.RConnection;
import reactor.core.publisher.Mono;

import java.util.function.BiConsumer;

/**
 * @Auther: lxr
 * @Date: 2019/1/21 14:12
 * @Description:
 */
public interface ConnectionAcceptor {

    Mono<Void> accept(BiConsumer<String, RConnection> consumer);

}
