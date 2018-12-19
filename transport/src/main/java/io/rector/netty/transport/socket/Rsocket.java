package io.rector.netty.transport.socket;

import io.netty.buffer.ByteBuf;
import io.rector.netty.config.Protocol;
import io.rector.netty.transport.ServerTransport;
import io.rector.netty.transport.Transport;
import io.rector.netty.transport.connction.RConnection;
import reactor.core.publisher.Mono;
import reactor.ipc.netty.NettyConnector;
import reactor.ipc.netty.NettyInbound;
import reactor.ipc.netty.NettyOutbound;
import reactor.ipc.netty.tcp.TcpServer;

import java.util.List;
import java.util.function.Supplier;

/**
 * @Auther: lxr
 * @Date: 2018/12/9 15:55
 * @Description:
 */
public abstract class Rsocket<T extends NettyConnector< ? extends NettyInbound,? extends NettyOutbound>> {

    protected List<RConnection> connections ;

    protected Supplier<Transport<T>> transport;

    public abstract Supplier<Protocol> getPrptocol();

    public Mono<Rsocket<T>> start() {
        return  Mono.defer(this::get);
    }

    public void close() {
       transport.get().close().subscribe();
    }


    private Mono<? extends Rsocket<T>> get() {
        Transport<T> tTransport=transport.get();
        return  tTransport
                .connect().doOnNext(rConnection -> {
                    connections.add(rConnection);// 维护客户端列表
                    rConnection.onReadIdle(tTransport.config().getReadIdle(),()->{
                        connections.remove(rConnection);
                        rConnection.dispose();
                        tTransport.config().getReadEvent().get().run();
                    });
                    rConnection.onWriteIdle(tTransport.config().getWriteIdle(),()->{
                        connections.remove(rConnection);
                        rConnection.dispose();
                        tTransport.config().getWriteEvent().get().run();
                    });
                    rConnection.receiveMsg();
                    rConnection.onClose(()->connections.remove(rConnection)); // 关闭时删除连接
                }).then(Mono.just(this));
    }





}
