package io.rector.netty.transport.socket;

import io.reactor.netty.api.ChannelAttr;
import io.reactor.netty.api.codec.*;
import io.rector.netty.config.Config;
import io.rector.netty.config.ServerConfig;
import io.rector.netty.flow.plugin.PluginRegistry;
import io.rector.netty.transport.Transport;
import io.rector.netty.transport.codec.DecoderAcceptor;
import io.rector.netty.transport.codec.ReactorDecoder;
import io.rector.netty.transport.codec.ServerDecoderAcceptor;
import io.rector.netty.transport.connection.ConnectionFactory;
import io.rector.netty.transport.connection.ConnectionManager;
import io.rector.netty.transport.distribute.ConnectionStateDistribute;
import io.rector.netty.transport.distribute.DirectServerMessageHandler;
import io.rector.netty.transport.distribute.UserTransportHandler;
import io.rector.netty.transport.distribute.OffMessageHandler;
import io.rector.netty.transport.distribute.def.DefaultOffMessageHandler;
import io.rector.netty.transport.distribute.def.DefaultUserTransportHandler;
import io.rector.netty.transport.group.GroupCollector;
import io.rector.netty.transport.method.MethodExtend;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.platform.commons.util.CollectionUtils;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.UnicastProcessor;
import reactor.core.scheduler.Schedulers;
import reactor.ipc.netty.options.ServerOptions;

import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
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

    private PluginRegistry pluginRegistry;

    private ServerConfig config;

    private DirectServerMessageHandler directServerMessageHandler;

    private UnicastProcessor<OfflineMessage>  offlineMessagePipeline=  UnicastProcessor.create();

    private MethodExtend methodExtend;

    private ConnectionStateDistribute connectionStateDistribute;

    private GroupCollector groupCollector;

    private OffMessageHandler offMessageHandler ;

    private ConnectionManager connectionManager ;

    private UserTransportHandler userHandler  ;

    private AtomicBoolean atomicBoolean  = new AtomicBoolean(false);


    public Mono<Void> setUserHandler(UserTransportHandler userHandler) {
        return Mono.fromRunnable(()->this.userHandler=userHandler);
    }

    public Mono<Void> setGroupCollector(GroupCollector groupCollector){
        return Mono.fromRunnable(()->this.groupCollector=groupCollector);
    }


    public Mono<Void> setConnectionManager(ConnectionManager manager){
        return Mono.fromRunnable(()->this.connectionManager=manager);
    }

    public Mono<Void> setOffMessageHandler(final OffMessageHandler offMessageHandler){
        return Mono.fromRunnable(()->{
            this.offMessageHandler = offMessageHandler;
            offMessageHandler.storageOfflineMessage(reciveOffline());
        });
    }


    public ServerSocketAdapter(Supplier<Transport> transport, PluginRegistry pluginRegistry, Config config, MethodExtend methodExtend) {
        this.transport = transport;
        this.pluginRegistry =pluginRegistry;
        this.config=(ServerConfig) config;
        this.connectionStateDistribute= new ConnectionStateDistribute(this);
        this.directServerMessageHandler = new DirectServerMessageHandler(this);
        this.methodExtend=methodExtend;
        this.connectionManager = new ConnectionFactory().getDefaultConnectionManager();
        this.offMessageHandler = new DefaultOffMessageHandler();
        this.userHandler= new DefaultUserTransportHandler();
    }


    @Override
    public Protocol getPrptocol() {
        return config.getProtocol();
    }

    @Override
    public Supplier<Consumer<RConnection>> next() {
        return ()-> rConnection -> {
            Optional.ofNullable(methodExtend.getReadIdle())
                    .ifPresent(read-> rConnection.onReadIdle(read.getTime(), () -> Optional.ofNullable(read.getEvent()).ifPresent(eve->eve.get().run())).subscribe());
            Optional.ofNullable(methodExtend.getWriteIdle())
                    .ifPresent(write-> rConnection.onWriteIdle(write.getTime(),()-> Optional.ofNullable(write.getEvent()).ifPresent(eve->eve.get().run())).subscribe());
            Disposable disposable=Mono.defer(()-> rConnection.dispose())
                    .delaySubscription(Duration.ofSeconds(5))
                    .subscribe();
            DecoderAcceptor decoderAcceptor= decoder().decode(offlineMessagePipeline, directServerMessageHandler,connectionStateDistribute,disposable,connectionManager,userHandler,atomicBoolean);
            rConnection.receiveMsg()
                    .doOnError(throwable -> log.error("receiveMsg url{} error {}",rConnection.address().block().getHostString(),throwable))
                    .map(this::apply)
                    .subscribeOn(Schedulers.elastic())
                    .subscribe(decoderAcceptor::transportMessage);
            rConnection.onClose(()->{
                InetSocketAddress socketAddres=rConnection.address().block();
                log.info(" connection host:{} port {} closed", socketAddres.getHostString(),socketAddres.getPort());
                Optional<String> c =ChannelAttr.getUserId(rConnection.getInbound());
                c.ifPresent(user->connectionManager.removeConnection(user,rConnection)); // 关闭时删除连接
                Set<RConnection> connections=connectionManager.getUserMultiConnection(c.get());
                if(atomicBoolean.get() && (connections == null || connections.size()==0)){ // 发送离线消息
                    String user=c.get();
                    directServerMessageHandler.sendOffline(TransportMessage.builder()
                            .connection(rConnection)
                            .clientType(ChannelAttr.getClientType(rConnection.getInbound()).get())
                            .type(ProtocolCatagory.OFFLINE)
                            .messageBody(ConnectionState.builder().userId(user).build())
                            .build(),userHandler.getFriends(user));
                }
            });
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
