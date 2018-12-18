package io.rector.netty.transport;

import io.rector.netty.config.Config;
import io.rector.netty.config.ServerConfig;
import io.rector.netty.transport.connction.RConnection;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.ipc.netty.NettyConnector;
import reactor.ipc.netty.NettyInbound;
import reactor.ipc.netty.NettyOutbound;
import reactor.ipc.netty.tcp.TcpServer;

import java.io.Closeable;
import java.util.Optional;
import java.util.function.Supplier;

public interface Transport<T extends NettyConnector< ? extends NettyInbound,? extends NettyOutbound>>{

    Flux<RConnection> connect();

    Mono<Void> close();

    Config config();

}
