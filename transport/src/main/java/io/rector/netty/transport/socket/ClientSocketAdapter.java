package io.rector.netty.transport.socket;

import io.reactor.netty.api.codec.Protocol;
import io.rector.netty.config.Config;
import io.rector.netty.transport.connction.RConnection;
import io.rector.netty.transport.method.MethodExtend;
import reactor.ipc.netty.NettyConnector;
import reactor.ipc.netty.NettyInbound;
import reactor.ipc.netty.NettyOutbound;

import java.io.Closeable;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @Auther: luxurong
 * @Date: 2019/1/18 14:37
 * @Description:
 **/
public class ClientSocketAdapter<T extends NettyConnector< ? extends NettyInbound,? extends NettyOutbound>>  extends Rsocket<T> implements Closeable {


    @Override
    public Config getConfig() {
        return null;
    }

    @Override
    public Protocol getPrptocol() {
        return null;
    }

    @Override
    public Supplier<Consumer<RConnection>> next() {
        return null;
    }

    @Override
    public MethodExtend getMethodExtend() {
        return null;
    }

}
