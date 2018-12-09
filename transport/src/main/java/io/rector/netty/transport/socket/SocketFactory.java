package io.rector.netty.transport.socket;

import io.rector.netty.config.Config;
import io.rector.netty.transport.Transport;
import reactor.ipc.netty.NettyConnector;
import reactor.ipc.netty.NettyInbound;
import reactor.ipc.netty.NettyOutbound;


/**
 * @Auther: lxr
 * @Date: 2018/12/6 19:20
 * @Description:
 */
@FunctionalInterface
public interface SocketFactory<IN extends NettyInbound,OUT extends NettyOutbound> {

    NettyConnector<IN,OUT> newFactory(Config config,Transport transport);


}
