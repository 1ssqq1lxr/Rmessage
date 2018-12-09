package io.rector.netty.transport.connction;

import reactor.core.publisher.Mono;

import java.io.Closeable;

/**
 * @Auther: lxr
 * @Date: 2018/12/9 12:30
 * @Description:
 */
@FunctionalInterface
public interface ConnectionAcceptor<T extends Closeable> {

    Mono<T> start();


}
