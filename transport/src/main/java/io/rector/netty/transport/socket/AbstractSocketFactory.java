package io.rector.netty.transport.socket;

import io.rector.netty.config.Config;
import io.rector.netty.transport.Transport;
import reactor.ipc.netty.NettyConnector;
import reactor.ipc.netty.tcp.TcpServer;

/**
 * @Auther: lxr
 * @Date: 2018/12/7 16:22
 * @Description:
 */
public class AbstractSocketFactory<T extends  NettyConnector> implements SocketFactory {

    @Override
    public TcpServer newFactory(Config config, Transport transport) {
        return  TcpServer.builder().listenAddress(config.getInetSocketAddress())
                .options(transport.nettyOptions())
                .build();
    }






}
