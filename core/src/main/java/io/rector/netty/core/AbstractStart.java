package io.rector.netty.core;

import io.rector.netty.config.Config;
import io.rector.netty.config.Protocol;
import reactor.ipc.netty.options.ServerOptions;

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
    public Start options(Consumer<ServerOptions.Builder<?>> options) {
        config.setOptions(options);
        return this;
    }
}
