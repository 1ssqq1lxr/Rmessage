package io.rector.netty.transport.socket;

import io.reactor.netty.api.codec.Protocol;
import io.reactor.netty.api.frame.Frame;
import io.rector.netty.flow.plugin.PluginRegistry;
import io.rector.netty.transport.Transport;
import io.rector.netty.transport.codec.Rdocoder;
import io.rector.netty.transport.codec.ServerDecoderAcceptor;
import io.rector.netty.transport.connction.RConnection;
import lombok.Data;
import reactor.core.publisher.Mono;
import reactor.ipc.netty.NettyConnector;
import reactor.ipc.netty.NettyInbound;
import reactor.ipc.netty.NettyOutbound;

import java.io.Closeable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @Auther: lxr
 * @Date: 2018/12/9 22:53
 * @Description:
 */
@Data
public class ServerSocketAdapter<T extends NettyConnector< ? extends NettyInbound,? extends NettyOutbound>>  extends Rsocket<T> implements Closeable {

    private List<RConnection> connections ; // all channel

    private PluginRegistry pluginRegistry;

    private Map<String , RConnection> ids = new ConcurrentHashMap<>(); // id -> channel

    private Map<String , List<RConnection>> keys = new ConcurrentHashMap<>(); // group -> channel

    public ServerSocketAdapter(Supplier<Transport<T>> transport, PluginRegistry pluginRegistry) {
        this.transport = transport;
        this.connections = new CopyOnWriteArrayList<>();
        this.pluginRegistry =pluginRegistry;
    }



    @Override
    public Supplier<Protocol> getPrptocol() {
        return ()->Protocol.TCP;
    }

    @Override
    public Function<Transport<T>,Consumer<RConnection>> next() {
        return transport->{
            Consumer<RConnection> rConnectionConsumer =rConnection -> {
                connections.add(rConnection);// 维护客户端列表
                rConnection.onReadIdle(transport.config().getReadIdle(),()->{
                    connections.remove(rConnection);
                    rConnection.dispose();
                    transport.config().getReadEvent().get().run();
                });
                rConnection.onWriteIdle(transport.config().getWriteIdle(),()->{
                    connections.remove(rConnection);
                    rConnection.dispose();
                    transport.config().getWriteEvent().get().run();
                });
                rConnection.receiveMsg()
                        .map(this::apply)
                        .subscribe(frame -> decoder().decoder(this,frame,rConnection).transportMessage());
                rConnection.onClose(()->connections.remove(rConnection)); // 关闭时删除连接
            };
            return  rConnectionConsumer;
        };
    }

    @Override
    public Mono<Void> removeConnection(RConnection duplexConnection) {
       return duplexConnection.dispose();
    }

    private Frame apply(Frame frame) {
        return pluginRegistry.applyServer(frame);
    }

    private Rdocoder decoder(){
        return ServerDecoderAcceptor::new;
    }
}
