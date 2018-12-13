package io.rector.netty.transport.socket;

import io.rector.netty.config.Protocol;
import reactor.core.publisher.Mono;
import reactor.ipc.netty.NettyConnector;
import reactor.ipc.netty.NettyInbound;
import reactor.ipc.netty.NettyOutbound;
import reactor.ipc.netty.tcp.TcpClient;
import reactor.ipc.netty.tcp.TcpServer;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;


/**
 * @Auther: lxr
 * @Date: 2018/12/6 19:20
 * @Description:
 */
public class SocketFactory  {

    private static Map<Protocol,Class<? extends NettyConnector>> sockets = new HashMap<>();

    public SocketFactory(Consumer<Map<Protocol,Class<? extends NettyConnector>>> consumer) {
        consumer.accept(sockets);
    }

    public  Mono<Void> register(Protocol protocol, Class<NettyConnector> tClass){
       return Mono.defer(()->{
            sockets.put(protocol,tClass);
            return Mono.empty();
        });
    }
    public Optional<Class<? extends NettyConnector>> getSocket(Protocol protocol){
        return  Optional.ofNullable(sockets.get(protocol));
    }



}
