package io.rector.netty.transport.socket;

import io.rector.netty.config.Config;
import io.rector.netty.config.ServerConfig;
import io.rector.netty.flow.plugin.PluginRegistry;
import io.rector.netty.transport.Transport;
import io.rector.netty.transport.method.MethodExtend;
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

    Rsocket accept(Supplier<Transport> transport, PluginRegistry pluginRegistry, Config config, MethodExtend methodExtend);

}
