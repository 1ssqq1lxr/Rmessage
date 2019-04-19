package io.rector.netty.core.init;

import io.reactor.netty.api.codec.ClientType;
import io.reactor.netty.api.codec.Protocol;
import io.reactor.netty.api.exception.NotFindConfigException;
import io.reactor.netty.api.exception.NotSupportException;
import io.rector.netty.config.ClientConfig;
import io.rector.netty.core.session.TcpClientSession;
import io.rector.netty.flow.plugin.FrameInterceptor;
import io.rector.netty.transport.ClientTransport;
import io.rector.netty.transport.method.ReactorMethodExtend;
import io.rector.netty.transport.socket.ClientSocketAdapter;
import io.rector.netty.transport.socket.RsocketAcceptor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;
import reactor.ipc.netty.NettyConnector;
import reactor.ipc.netty.NettyInbound;
import reactor.ipc.netty.NettyOutbound;
import reactor.ipc.netty.tcp.TcpClient;

import java.util.Map;
import java.util.function.Consumer;

/**
 * @Auther: luxurong
 * @Date: 2019/1/18 13:39
 * @Description: 客户端start
 **/
@Slf4j
public class ClientStart   extends  AbstractStart {


    private Consumer<Map<Protocol,Class<? extends NettyConnector< ? extends NettyInbound,? extends NettyOutbound>> >> consumer = classes-> classes.put(Protocol.TCP, TcpClient.class);

    private static class StartBuilder{
        private static ClientStart start = new ClientStart();
    }


    public static ClientStart builder(){
        return ClientStart.StartBuilder.start;
    }

    @Override
    public  Start setClientType(ClientType type){
        ((ClientConfig)config).setClientType(type);
        return  this;
    }

    @Override
    public  Start userId(String userId){
        ((ClientConfig)config).setUserId(userId);
        return  this;
    }


    public  Start password(String password){
        ((ClientConfig)config).setPassword(password);
        return  this;
    }


    private ClientStart() {
        super(ClientConfig.builder().build(), ReactorMethodExtend.builder().build());
    }


    @Override
    public Start interceptor(FrameInterceptor... frameInterceptor) {
        throw  new NotSupportException(" client not support frameInterceptor");
    }


    @Override
    public   Mono<Disposable> connect() {
        config.check();
        ClientTransport clientTransport =new ClientTransport(socketFactory()
                .accept(consumer)
                .getSocket(config.getProtocol())
                .orElseThrow(()->new NotFindConfigException("协议不存在")));
        return rsocketAcceptor()
                .map(rsocketAcceptor -> {
                    ClientSocketAdapter rsocket= (ClientSocketAdapter )rsocketAcceptor.accept(() -> clientTransport,null,config,methodExtend);
                    return   rsocket.start()
                            .map(socket->new TcpClientSession(rsocket))
                            .doOnError(ex->log.error("connect error:",ex))
                            .retry(10)
                            .log("server")
                            .block();
                });

    }

    @Override
    public Mono<Disposable> start() {
        throw  new NotSupportException(" client not support start method");
    }


    private  Mono<RsocketAcceptor>  rsocketAcceptor(){
        return Mono.just(ClientSocketAdapter::new);
    }

}
