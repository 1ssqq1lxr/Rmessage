package io.rector.netty.transport;

import io.rector.netty.config.Config;
import io.rector.netty.transport.connction.RConnection;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface Transport{

    Flux<RConnection> connect(Config config);

    Mono<Void> close();

}
