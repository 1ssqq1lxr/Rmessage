package io.rector.netty.transport.socket;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.ipc.netty.NettyConnector;
import reactor.ipc.netty.NettyInbound;
import reactor.ipc.netty.NettyOutbound;
import reactor.ipc.netty.tcp.TcpServer;

/**
 * @Auther: lxr
 * @Date: 2018/12/9 15:55
 * @Description:
 */
public class Rsocket<T extends NettyConnector< ? extends NettyInbound,? extends NettyOutbound>> {

    private NettyConnector nettyConnector;

    public Mono<Void> close(Runnable runnable){
        return  Mono.empty();
    }

}
