package io.rector.netty.transport.socket;

import io.rector.netty.config.Protocol;
import io.rector.netty.transport.ServerTransport;
import io.rector.netty.transport.Transport;
import io.rector.netty.transport.socket.Rsocket;
import reactor.core.publisher.Mono;
import reactor.core.publisher.UnicastProcessor;
import reactor.ipc.netty.NettyConnector;
import reactor.ipc.netty.NettyInbound;
import reactor.ipc.netty.NettyOutbound;
import reactor.ipc.netty.tcp.TcpServer;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Supplier;

/**
 * @Auther: lxr
 * @Date: 2018/12/9 22:53
 * @Description:
 */
public class SocketAdapter<T extends NettyConnector< ? extends NettyInbound,? extends NettyOutbound>>  extends Rsocket<T> implements Closeable {

    public SocketAdapter(Supplier<Transport<T>> transport) {
        this.transport = transport;
        this.connections = new CopyOnWriteArrayList<>();
    }


    @Override
    public Supplier<Protocol> getPrptocol() {
        return ()->Protocol.TCP;
    }

}
