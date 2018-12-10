package io.rector.netty.transport.socket;

import io.rector.netty.config.Protocol;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.ipc.netty.NettyConnector;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;


/**
 * @Auther: lxr
 * @Date: 2018/12/6 19:20
 * @Description:
 */
public class SocketFactory {
    private Map<Protocol,Mono<RsocketAcceptor<? extends  NettyConnector>>> sockets = new HashMap<>();


    public SocketFactory(Consumer< Map<Protocol,Mono<RsocketAcceptor<? extends  NettyConnector>>>> consumer){
        consumer.accept(sockets);
    }


    public  Mono<Void> register(Protocol protocol,Mono<RsocketAcceptor<? extends  NettyConnector>> rsocket){
       return Mono.defer(()->{
            sockets.put(protocol,rsocket);
            return Mono.empty();
        });
    }
    public Mono<RsocketAcceptor<? extends  NettyConnector>> getSocket(Protocol protocol){
      return   Optional.ofNullable(sockets.get(protocol))
                .orElse(Mono.empty());

    }



}
