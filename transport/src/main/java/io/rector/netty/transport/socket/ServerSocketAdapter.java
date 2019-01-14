package io.rector.netty.transport.socket;

import io.reactor.netty.api.codec.Protocol;
import io.reactor.netty.api.codec.TransportMessage;
import io.rector.netty.config.ServerConfig;
import io.rector.netty.flow.plugin.PluginRegistry;
import io.rector.netty.transport.Transport;
import io.rector.netty.transport.codec.DecoderAcceptor;
import io.rector.netty.transport.codec.Rdocoder;
import io.rector.netty.transport.codec.ServerDecoderAcceptor;
import io.rector.netty.transport.connction.RConnection;
import io.rector.netty.transport.distribute.DirectServerMessageDistribute;
import io.rector.netty.transport.distribute.OfflineMessageDistribute;
import io.rector.netty.transport.method.MethodExtend;
import lombok.Data;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.UnicastProcessor;
import reactor.core.scheduler.Schedulers;
import reactor.ipc.netty.NettyConnector;
import reactor.ipc.netty.NettyInbound;
import reactor.ipc.netty.NettyOutbound;

import java.io.Closeable;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
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

    private ServerConfig config;

    private DirectServerMessageDistribute distribute;

    private UnicastProcessor<TransportMessage>  offlineMessagePipeline=  UnicastProcessor.create();

    private MethodExtend methodExtend;

    public ServerSocketAdapter(Supplier<Transport> transport, PluginRegistry pluginRegistry,ServerConfig config, MethodExtend methodExtend) {
        this.transport = transport;
        this.connections = new CopyOnWriteArrayList<>();
        this.pluginRegistry =pluginRegistry;
        this.config=config;
        this.methodExtend=methodExtend;
        this.distribute= new DirectServerMessageDistribute(this);
    }


    @Override
    public Supplier<Protocol> getPrptocol() {
        return ()->Protocol.TCP;
    }

    @Override
    public Supplier<Consumer<RConnection>> next() {
        return ()-> rConnection -> {
                connections.add(rConnection);// 维护客户端列表
                rConnection.onReadIdle(methodExtend.getReadIdle().getTime(),()->{
                    connections.remove(rConnection);
                    rConnection.dispose();
                    methodExtend.getReadIdle().getEvent().get().run();
                }).subscribe();
                rConnection.onWriteIdle(methodExtend.getWriteIdle().getTime(),()->{
                    connections.remove(rConnection);
                    rConnection.dispose();
                    methodExtend.getWriteIdle().getEvent().get().run();
                }).subscribe();
                Disposable disposable=Mono.defer(()-> rConnection.dispose())
                    .delaySubscription(Duration.ofSeconds(5))
                    .subscribe();
                DecoderAcceptor decoderAcceptor= decoder().decode(offlineMessagePipeline,distribute,disposable);
                rConnection.receiveMsg()
                        .map(this::apply)
                        .subscribeOn(Schedulers.elastic())
                        .map(message ->decoderAcceptor.transportMessage(message).subscribe());
                rConnection.onClose(()->connections.remove(rConnection)); // 关闭时删除连接
            };
    }



    @Override
    public Mono<Void> removeConnection(RConnection duplexConnection) {
       return duplexConnection.dispose();
    }

    private TransportMessage apply(TransportMessage message) {
        return pluginRegistry.applyServer(message);
    }

    private Rdocoder decoder(){
        return ServerDecoderAcceptor::new;
    }

    public Flux<TransportMessage> reciveOffline() {
      return   Flux.from(offlineMessagePipeline);
    }
}
