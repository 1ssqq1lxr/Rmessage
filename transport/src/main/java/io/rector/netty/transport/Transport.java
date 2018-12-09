package io.rector.netty.transport;

import io.rector.netty.transport.connction.Connection;
import io.rector.netty.config.Config;
import reactor.core.publisher.Mono;
import reactor.ipc.netty.options.ServerOptions;

import java.util.function.Consumer;

public interface Transport {

    Consumer<? super ServerOptions.Builder<?>> nettyOptions();


    Mono<Connection> connect(Config config, Transport transport);


}
