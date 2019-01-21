package io.rector.netty.transport;

import io.reactor.netty.api.ReflectUtil;
import io.reactor.netty.api.codec.RConnection;
import io.reactor.netty.api.exception.NotSupportException;
import io.rector.netty.transport.method.MethodExtend;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.ipc.netty.NettyConnector;
import reactor.ipc.netty.NettyContext;
import reactor.ipc.netty.NettyInbound;
import reactor.ipc.netty.NettyOutbound;

/**
 * @Auther: lxr
 * @Date: 2018/12/7 16:46
 * @Description:
 */
public class ServerTransport implements Transport {


    private Class<? extends NettyConnector< ? extends NettyInbound,? extends NettyOutbound>>  classT;

    private  NettyContext context;


    public ServerTransport(Class<? extends NettyConnector< ? extends NettyInbound,? extends NettyOutbound>> classT) {
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
