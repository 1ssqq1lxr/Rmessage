package io.rector.netty.core.socket;

import io.rector.netty.config.Protocol;
import io.rector.netty.transport.Transport;
import io.rector.netty.transport.socket.Rsocket;
import reactor.core.publisher.Mono;
import reactor.core.publisher.UnicastProcessor;
import reactor.ipc.netty.tcp.TcpServer;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Supplier;

/**
 * @Auther: lxr
 * @Date: 2018/12/9 22:53
 * @Description:
 */
public class TcpSocket extends Rsocket<TcpServer> implements Closeable {

    public TcpSocket(Supplier<Transport> transport) {
        this.transport = transport;
        this.connections = new CopyOnWriteArrayList<>();
    }

    UnicastProcessor<String> s=UnicastProcessor.create();

    @Override
    public Supplier<Protocol> getPrptocol() {
        return ()->Protocol.TCP;
    }


    public Mono<Rsocket<TcpServer>> start() {
        return  Mono.defer(()->{
            transport.get()
                    .connect()
                    .doOnNext(duplexConnection -> {
                        duplexConnection.onClose(()->connections.remove(duplexConnection));
                        connections.add(duplexConnection);
                    });
              return Mono.just(this);
          });
    }

    @Override
    public void close() throws IOException {
        transport.get().close();
    }
}
