package io.rector.netty.transport;

import io.rector.netty.transport.connction.DuplexConnection;
import reactor.core.publisher.Flux;

import java.io.Closeable;
import java.util.List;

public interface Transport extends Closeable {


    Flux<DuplexConnection> connect();


}
