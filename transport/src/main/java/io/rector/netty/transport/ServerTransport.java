package io.rector.netty.transport;

import io.rector.netty.transport.connction.Connection;
import io.rector.netty.config.Config;
import reactor.core.publisher.Mono;
import reactor.ipc.netty.options.ServerOptions;

import java.io.Closeable;
import java.io.IOException;
import java.util.function.Consumer;

/**
 * @Auther: lxr
 * @Date: 2018/12/7 16:46
 * @Description:
 */
public class ServerTransport implements Transport,Closeable {



    @Override
    public Consumer<? super ServerOptions.Builder<?>> nettyOptions() {
        return null;
    }

    @Override
    public Mono<Connection> connect(Config config, Transport transport) {

        return  Mono.empty();
    }


    @Override
    public void close() throws IOException {

    }
}
