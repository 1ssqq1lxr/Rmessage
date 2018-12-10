package io.rector.netty.core;

import io.rector.netty.transport.socket.Rsocket;
import reactor.ipc.netty.NettyConnector;
import reactor.ipc.netty.NettyInbound;
import reactor.ipc.netty.NettyOutbound;

import java.util.function.Supplier;

/**
 * @Auther: lxr
 * @Date: 2018/12/7 17:33
 * @Description:
 */
public class ApiOperation <T extends NettyConnector< ? extends NettyInbound,? extends NettyOutbound>>{

    private Supplier<Rsocket<T>>  rsocket;

    public ApiOperation(Rsocket<T> rsocket) {
        this.rsocket = ()->rsocket;
    }

    public Supplier<Rsocket<T>> getRsocket() {
        return rsocket;
    }
}
