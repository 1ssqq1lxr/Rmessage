package io.rector.netty.transport.socket;

import io.reactor.netty.api.ChannelAttr;
import io.reactor.netty.api.codec.OfflineMessage;
import io.reactor.netty.api.codec.Protocol;
import io.reactor.netty.api.codec.TransportMessage;
import io.rector.netty.config.Config;
import io.rector.netty.config.ServerConfig;
import io.rector.netty.flow.plugin.PluginRegistry;
import io.rector.netty.transport.Transport;
import io.rector.netty.transport.codec.DecoderAcceptor;
import io.rector.netty.transport.codec.ReactorDecoder;
import io.rector.netty.transport.codec.ServerDecoderAcceptor;
import io.rector.netty.transport.connction.RConnection;
import io.rector.netty.transport.distribute.ConnectionStateDistribute;
import io.rector.netty.transport.distribute.DirectServerMessageHandler;
import io.rector.netty.transport.distribute.OffMessageHandler;
import io.rector.netty.transport.group.GroupCollector;
import io.rector.netty.transport.method.MethodExtend;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.UnicastProcessor;
import reactor.core.scheduler.Schedulers;
import reactor.ipc.netty.NettyConnector;
import reactor.ipc.netty.NettyInbound;
import reactor.ipc.netty.NettyOutbound;

import java.io.Closeable;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @Auther: lxr
 * @Date: 2018/12/9 22:53
 * @Description: 核心处理socket 连接
 */
@Data
@Slf4j
public class ServerSocketAdapter extends Rsocket  {

    private List<RConnection> connections ; // all channel

    private PluginRegistry pluginRegistry;

    private Map<String , RConnection> ids = new ConcurrentHashMap<>(); // id -> channel


    private ServerConfig config;

    private DirectServerMessageHandler directServerMessageHandler;

    private UnicastProcessor<OfflineMessage>  offlineMessagePipeline=  UnicastProcessor.create();

    private MethodExtend methodExtend;

    private ConnectionStateDistribute connectionStateDistribute;

    private GroupCollector groupCollector;

    private OffMessageHandler offMessageHandler;




    public Mono<Void> setGroupCollector(GroupCollector groupCollector){
        return Mono.fromRunnable(()->this.groupCollector=groupCollector);
    }

    public Mono<Void> setOffMessageHandler(final OffMessageHandler offMessageHandler){
        return Mono.fromRunnable(()->{
            this.offMessageHandler = offMessageHandler;
            offMessageHandler.storageOfflineMessage(reciveOffline());
        });
    }


    public ServerSocketAdapter(Supplier<Transport> transport, PluginRegistry pluginRegistry, Config config, MethodExtend methodExtend) {
        this.transport = transport;
        this.connections = new CopyOnWriteArrayList<>();
        this.pluginRegistry =pluginRegistry;
        this.config=(ServerConfig) config;
        this.methodExtend=methodExtend;
        this.directServerMessageHandler = new DirectServerMessageHandler(this);
        this.connectionStateDistribute= new ConnectionStateDistribute(this);
    }


    @Override
    public Protocol getPrptocol() {
        return config.getProtocol();
    }

    @Override
    public Supplier<Consumer<RConnection>> next() {
        return ()-> rConnection -> {
            connections.add(rConnection);// 维护客户端列表
            Optional.ofNullable(methodExtend.getReadIdle())
                    .ifPresent(read-> rConnection.onReadIdle(read.getTime(), () -> read.getEvent().get().run()).subscribe());
            Optional.ofNullable(methodExtend.getWriteIdle())
                    .ifPresent(write-> rConnection.onWriteIdle(methodExtend.getWriteIdle().getTime(),()-> methodExtend.getWriteIdle().getEvent().get().run()).subscribe());
            Disposable disposable=Mono.defer(()-> rConnection.dispose())
                    .delaySubscription(Duration.ofSeconds(5))
                    .subscribe();
            DecoderAcceptor decoderAcceptor= decoder().decode(offlineMessagePipeline, directServerMessageHandler,connectionStateDistribute,disposable,user->ids.put(user,rConnection));
            rConnection.receiveMsg()
                    .doOnError(throwable -> log.error("receiveMsg url{} error {}",rConnection.address().block().getHostString(),throwable))
                    .map(this::apply)
                    .subscribeOn(Schedulers.elastic())
                    .subscribe(decoderAcceptor::transportMessage);
            rConnection.onClose(()->{
                InetSocketAddress socketAddres=rConnection.address().block();
                log.info(" connection host:{} port {} closed", socketAddres.getHostString(),socketAddres.getPort());
                ids.remove(ChannelAttr.getUserId(rConnection.getInbound()));
                connections.remove(rConnection);
            }); // 关闭时删除连接
        };
    }



    public Mono<Void> removeConnection(RConnection duplexConnection) {
        return duplexConnection.dispose();
    }

    private TransportMessage apply(TransportMessage message) {
        return pluginRegistry.applyServer(message);
    }

    private ReactorDecoder decoder(){
        return ServerDecoderAcceptor::new;
    }

    public Flux<OfflineMessage> reciveOffline() {
        return   Flux.from(offlineMessagePipeline);
    }
}
