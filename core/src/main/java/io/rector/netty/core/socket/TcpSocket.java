package io.rector.netty.core.socket;

import io.rector.netty.core.ApiOperation;
import io.rector.netty.transport.Transport;
import io.rector.netty.transport.socket.Rsocket;
import reactor.core.publisher.Mono;
import reactor.ipc.netty.tcp.TcpServer;

import java.util.function.Supplier;

/**
 * @Auther: lxr
 * @Date: 2018/12/9 22:53
 * @Description:
 */
public class TcpSocket extends Rsocket<TcpServer> {



    public TcpSocket(Supplier<Transport> transport) {
        this.transport = transport;
    }




}
