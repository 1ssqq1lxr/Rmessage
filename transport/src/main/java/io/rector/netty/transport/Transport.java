package io.rector.netty.transport;

import io.rector.netty.transport.connction.RConnection;
import io.rector.netty.transport.method.MethodExtend;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface Transport{

    Flux<RConnection> start(MethodExtend methodExtend);

    Mono<RConnection> connect(MethodExtend methodExtend);

    Mono<Void> close();

}
