package io.rector.netty.core.socket;

import io.rector.netty.transport.Transport;
import io.rector.netty.transport.socket.Rsocket;
import reactor.ipc.netty.tcp.TcpServer;

import java.util.function.Supplier;

/**
 * @Auther: lxr
 * @Date: 2018/12/9 22:53
 * @Description:
 */
public class TcpSocket extends Rsocket<TcpServer> {

    private Supplier<Transport> transport;

    public TcpSocket(Supplier<Transport> transport) {
        this.transport = transport;
    }



}
