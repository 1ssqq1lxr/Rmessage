package io.rector.netty.transport.socket;

import io.rector.netty.transport.Transport;
import reactor.ipc.netty.NettyConnector;
import reactor.ipc.netty.NettyInbound;
import reactor.ipc.netty.NettyOutbound;

import java.util.function.Supplier;

/**
 * @Auther: lxr
 * @Date: 2018/12/9 15:55
 * @Description:
 */
@FunctionalInterface
public interface RsocketAcceptor {

    Rsocket<? extends  NettyConnector< ? extends NettyInbound,? extends NettyOutbound>> accept(Supplier<Transport> transport);

}
