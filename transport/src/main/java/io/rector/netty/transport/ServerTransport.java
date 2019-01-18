package io.rector.netty.transport;

import io.reactor.netty.api.ReflectUtil;
import io.reactor.netty.api.exception.NotSupportException;
import io.rector.netty.config.Config;
import io.rector.netty.config.ServerConfig;
import io.rector.netty.transport.connction.RConnection;
import io.rector.netty.transport.method.MethodExtend;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.ipc.netty.NettyConnector;
import reactor.ipc.netty.NettyContext;
import reactor.ipc.netty.NettyInbound;
import reactor.ipc.netty.NettyOutbound;
import reactor.ipc.netty.tcp.TcpServer;

/**
 * @Auther: lxr
 * @Date: 2018/12/7 16:46
 * @Description:
 */
public class ServerTransport<T extends NettyConnector< ? extends NettyInbound,? extends NettyOutbound>> implements Transport {


    private Class<T> classT;

    private  NettyContext context;


    public ServerTransport(Class<T> classT) {
        this.classT=classT;
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
    public Flux<RConnection> start(MethodExtend methodExtend) {
       return Flux.create(fluxSink -> this.context=ReflectUtil.staticMethod(classT,methodExtend.getServerOptions()).newHandler((in, out)->{
           RConnection duplexConnection = new RConnection(in,out,out.context());
           fluxSink.next(duplexConnection);
           return out.context().onClose();
       }).block());
    }


    @Override
    public Mono<RConnection> connect(MethodExtend methodExtend) {
       throw  new NotSupportException("server not support connect operation");
    }



}
