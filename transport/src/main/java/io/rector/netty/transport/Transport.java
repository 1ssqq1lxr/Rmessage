package io.rector.netty.transport;

import io.rector.netty.transport.connction.RConnection;
import reactor.core.publisher.Flux;

import java.io.Closeable;

public interface Transport extends Closeable {


    Flux<RConnection> connect();


}
