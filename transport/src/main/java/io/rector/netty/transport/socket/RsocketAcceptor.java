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
public interface RsocketAcceptor<T extends NettyConnector< ? extends NettyInbound,? extends NettyOutbound>> {

    Rsocket<T> accept(Supplier<Transport> transport);

}
