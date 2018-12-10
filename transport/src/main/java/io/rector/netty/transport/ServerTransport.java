package io.rector.netty.transport;

import io.rector.netty.config.ServerConfig;
import io.rector.netty.transport.connction.DuplexConnection;
import reactor.core.publisher.Flux;
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
public class ServerTransport<T extends NettyConnector< ? extends NettyInbound,? extends NettyOutbound>> implements Transport {

    private Supplier<T> server;

    private Supplier<ServerConfig> config;

    private   NettyContext context;


    public ServerTransport(T server, ServerConfig config) {
        this.server =()->server;
        this.config = ()->config;
    }

    @Override
    public void close() {
        if(!context.isDisposed()){
            context.dispose();
        }
    }

    @Override
    public Flux<DuplexConnection> connect() {
       return Flux.create(fluxSink -> {
         this.context =server.get().newHandler((in, out)->{
               DuplexConnection duplexConnection = new DuplexConnection(in,out,out.context(),this);
               fluxSink.next(duplexConnection);
               return out.context().onClose();
           }).block();
       });
    }
}
