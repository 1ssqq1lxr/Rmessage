package io.rector.netty.transport;

import io.rector.netty.config.ServerConfig;
import io.rector.netty.transport.connction.DuplexConnection;
import reactor.core.publisher.Flux;
import reactor.ipc.netty.NettyConnector;
import reactor.ipc.netty.NettyInbound;
import reactor.ipc.netty.NettyOutbound;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.function.Supplier;

/**
 * @Auther: lxr
 * @Date: 2018/12/7 16:46
 * @Description:
 */
public class ServerTransport<T extends NettyConnector< ? extends NettyInbound,? extends NettyOutbound>> implements Transport,Closeable {

    private Supplier<T> server;

    private Supplier<ServerConfig> config;


    public ServerTransport(T server, ServerConfig config) {
        this.server =()->server;
        this.config = ()->config;
    }

    @Override
    public void close() throws IOException {

    }

    @Override
    public Flux<DuplexConnection> connect() {
       return Flux.create(fluxSink -> server.get().newHandler((in, out)->{
           DuplexConnection duplexConnection = new DuplexConnection(in,out,out.context(),this);
           fluxSink.next(duplexConnection);
           return out.context().onClose();
       }).block());
    }
}
