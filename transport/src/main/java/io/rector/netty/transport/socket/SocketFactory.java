package io.rector.netty.transport.socket;

import io.reactor.netty.api.codec.Protocol;
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
public class SocketFactory  {

    private static Map<Protocol,Class<? extends NettyConnector>> sockets = new HashMap<>();

    public SocketFactory(Consumer<Map<Protocol,Class<? extends NettyConnector>>> consumer) {
        consumer.accept(sockets);
    }

    public Optional<Class<? extends NettyConnector>> getSocket(Protocol protocol){
        return  Optional.ofNullable(sockets.get(protocol));
    }



}
