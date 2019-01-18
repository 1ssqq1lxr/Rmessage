package io.rector.netty.transport.socket;

import io.reactor.netty.api.codec.Protocol;
import io.rector.netty.config.ClientConfig;
import io.rector.netty.config.Config;
import io.rector.netty.transport.ClientTransport;
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


    private RConnection rConnection;

    private Supplier<ClientTransport> transport;

    private ClientConfig config;

    private MethodExtend methodExtend;


    public ClientSocketAdapter(Supplier<ClientTransport> transport, ClientConfig config, MethodExtend methodExtend) {
        this.transport = transport;
        this.config=config;
        this.methodExtend=methodExtend;
    }


    @Override
    public Config getConfig() {
        return this.config;
    }

    @Override
    public Protocol getPrptocol() {
        return this.config.getProtocol();
    }

    @Override
    public Supplier<Consumer<RConnection>> next() {
        return ()->rConnection -> {
            this.rConnection=rConnection;
            rConnection.onReadIdle(methodExtend.getReadIdle().getTime(),()->{
                methodExtend.getReadIdle().getEvent().get().run();
            }).then(
                    rConnection.onWriteIdle(methodExtend.getWriteIdle().getTime(),()->{

                        methodExtend.getWriteIdle().getEvent().get().run();
                    })).subscribe();

        };
    }

    @Override
    public MethodExtend getMethodExtend() {
        return null;
    }

}
