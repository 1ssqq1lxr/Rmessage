package io.rector.netty.core;

import io.netty.channel.Channel;
import io.rector.netty.config.Config;
import io.rector.netty.config.Protocol;
import io.rector.netty.transport.socket.SocketFactory;
import reactor.ipc.netty.NettyConnector;
import reactor.ipc.netty.NettyContext;
import reactor.ipc.netty.options.ServerOptions;

import java.util.Map;
import java.util.function.Consumer;

/**
 * @Auther: lxr
 * @Date: 2018/12/13 17:51
 * @Description:
 */
abstract class AbstractStart implements Start {

    protected final Config config;

    public AbstractStart(Config config) {
        this.config = config;
    }

    @Override
    public Start udp() {
        config.setProtocol(Protocol.UDP);
        return this;
    }

    @Override
    public Start tcp() {
        config.setProtocol(Protocol.TCP);
        return this;
    }

    @Override
    public Start mqtt() {
        config.setProtocol(Protocol.MQTT);
        return this;
    }

    @Override
    public Start websocket() {
        config.setProtocol(Protocol.WS);
        return this;
    }



    @Override
    public Start ip(String ip) {
        config.setIp(ip);
        return this;
    }

    @Override
    public Start port(int port) {
        config.setPort(port);
        return this;
    }

    @Override
    public Start setAfterNettyContextInit(Consumer<? super NettyContext> afterNettyContextInit) {
        config.setAfterNettyContextInit(afterNettyContextInit);
        return this;
    }

    @Override
    public Start setAfterChannelInit(Consumer<? super Channel> afterChannelInit) {
        config.setAfterChannelInit(afterChannelInit);
        return this;
    }

    protected  SocketFactoryAcceptor socketFactory(){
        return SocketFactory::new;
    }


    protected @FunctionalInterface interface SocketFactoryAcceptor{

        SocketFactory accept(Consumer<Map<Protocol,Class<? extends NettyConnector>>> consumer);

    }
}
