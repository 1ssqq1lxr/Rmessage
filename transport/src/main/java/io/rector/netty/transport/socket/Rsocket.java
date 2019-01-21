package io.rector.netty.transport.socket;

import io.reactor.netty.api.codec.Protocol;
import io.rector.netty.config.Config;
import io.rector.netty.transport.Transport;
import io.rector.netty.transport.connection.RConnection;
import io.rector.netty.transport.method.MethodExtend;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @Auther: lxr
 * @Date: 2018/12/9 15:55
 * @Description:
 */
@Slf4j
public abstract class Rsocket {


    public abstract Config getConfig();


    protected Supplier<Transport> transport;

    public abstract Protocol getPrptocol();

    public Mono<Rsocket> start() {
        return Mono.defer(()->{
            if(this instanceof ClientSocketAdapter){
                return  transport.get().connect(getMethodExtend()).doOnNext(next().get()::accept).doOnError(throwable -> log.error("Rsocket {}",throwable)).then(Mono.just(this));
            }
            else {
                transport.get().start(getMethodExtend()).doOnNext(next().get()::accept).doOnError(throwable -> log.error("Rsocket {}",throwable)).subscribe();
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
