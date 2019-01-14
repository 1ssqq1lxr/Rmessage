package io.rector.netty.config;

import io.netty.channel.Channel;
import io.reactor.netty.api.codec.Protocol;
import reactor.ipc.netty.NettyContext;
import reactor.ipc.netty.options.ServerOptions;

import java.util.function.Consumer;
import java.util.function.Supplier;


/**
 * @Auther: lxr
 * @Date: 2018/12/7 16:09
 * @Description:
 */
public interface Config {

    void setIp(String ip);

    void setPort(int port);

    String getIp();

    int getPort();

    Protocol getProtocol();

    void setProtocol(Protocol protocol);


}
