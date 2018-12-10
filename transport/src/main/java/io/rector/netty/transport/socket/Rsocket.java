package io.rector.netty.transport.socket;

import io.rector.netty.config.Protocol;
import io.rector.netty.transport.Transport;
import reactor.core.publisher.Mono;
import reactor.ipc.netty.NettyConnector;
import reactor.ipc.netty.NettyInbound;
import reactor.ipc.netty.NettyOutbound;

import java.util.function.Supplier;

/**
 * @Auther: lxr
 * @Date: 2018/12/9 15:55
 * @Description:
 */
public abstract class Rsocket<T extends NettyConnector< ? extends NettyInbound,? extends NettyOutbound>> {

    protected Supplier<Transport> transport;

    public abstract Supplier<Protocol> getPrptocol();

}
