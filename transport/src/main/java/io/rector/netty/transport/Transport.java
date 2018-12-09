package io.rector.netty.transport;

import io.rector.netty.config.Config;
import io.rector.netty.transport.connction.DuplexConnection;
import reactor.core.publisher.Mono;

import java.util.function.Supplier;

public interface Transport {


    Mono<DuplexConnection> connect();


}
