package io.rector.netty.transport.socket;

import io.reactor.netty.api.codec.Protocol;
import io.rector.netty.config.Config;
import io.rector.netty.transport.Transport;
import io.rector.netty.transport.connction.RConnection;
import io.rector.netty.transport.method.MethodExtend;
import reactor.core.publisher.Mono;
import reactor.ipc.netty.NettyConnector;
import reactor.ipc.netty.NettyInbound;
import reactor.ipc.netty.NettyOutbound;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @Auther: lxr
 * @Date: 2018/12/9 15:55
 * @Description:
 */
public abstract class Rsocket {


    public abstract Config getConfig();


    protected Supplier<Transport> transport;

    public abstract Protocol getPrptocol();

    public Mono<Rsocket> start() {
        return Mono.defer(()->{
            if(this instanceof ClientSocketAdapter){
                return  transport.get().connect(getMethodExtend()).doOnNext(next().get()::accept).then(Mono.just(this));
            }
            else {
                transport.get().start(getMethodExtend()).doOnNext(next().get()::accept).subscribe();
                return Mono.just(this);
            }
        });
    }


    public abstract Supplier<Consumer<RConnection>>  next();


    public abstract MethodExtend getMethodExtend();


    public  Mono<Void> closeServer(){
       return transport.get().close();
    }

}
