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
 * @Auther: luxurong
 * @Date: 2019/1/18 13:59
 * @Description:
 **/
public class ClientTransport implements Transport {

    private Class<? extends NettyConnector< ? extends NettyInbound,? extends NettyOutbound>>  classT;

    private NettyContext context;


    public ClientTransport(Class<? extends NettyConnector< ? extends NettyInbound,? extends NettyOutbound>>  classT) {
        this.classT=classT;
    }

    @Override
    public Flux<RConnection> start(MethodExtend methodExtend) {
        throw  new NotSupportException("client not support start operation");
    }

    @Override
    public Mono<RConnection> connect(MethodExtend methodExtend) {
        return Mono.create(sink -> this.context= ReflectUtil.staticMethod(classT,methodExtend.getClientOptions()).newHandler((in, out)->{
            RConnection duplexConnection = new RConnection(in,out,out.context());
            sink.success(duplexConnection);
            return out.context().onClose();
        }).block());
    }

    @Override
    public Mono<Void> close() {
        return Mono.fromRunnable(()->context.dispose());
    }

    
    
}
