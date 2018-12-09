package io.rector.netty.config;

import reactor.ipc.netty.options.NettyOptions;
import reactor.ipc.netty.options.ServerOptions;

import java.net.InetSocketAddress;


/**
 * @Auther: lxr
 * @Date: 2018/12/7 16:09
 * @Description:
 */
public interface Config {

    InetSocketAddress getInetSocketAddress();

    void setProtocol(Protocol protocol);





}
