package io.rector.netty.transport;

import io.rector.netty.config.ServerConfig;
import io.rector.netty.transport.connction.RConnection;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.ipc.netty.NettyConnector;
import reactor.ipc.netty.NettyContext;
import reactor.ipc.netty.NettyInbound;
import reactor.ipc.netty.NettyOutbound;

import java.util.function.Supplier;

/**
 * @Auther: lxr
 * @Date: 2018/12/7 16:46
 * @Description:
 */
public class ServerTransport<T extends NettyConnector< ? extends NettyInbound,? extends NettyOutbound>> implements Transport<T> {

    private Supplier<T> server;

    private Supplier<ServerConfig> config;

    private   NettyContext context;


    public ServerTransport(T server, ServerConfig config) {
        this.server =()->server;
        this.config = ()->config;
    }

    @Override
    public Mono<Void> close() {
     return Mono.defer(()->{
            if(!context.isDisposed()){
                context.dispose();
            }
            return  Mono.empty();
        });
    }

    @Override
    public Supplier<ServerConfig> config() {
        return this.config;
    }

    @Override
    public Flux<RConnection> connect() {

       return Flux.create(fluxSink -> this.context =server.get().newHandler((in, out)->{
             RConnection duplexConnection = new RConnection(in,out,out.context());
             fluxSink.next(duplexConnection);
             return out.context().onClose();
         }).block());
    }
}
