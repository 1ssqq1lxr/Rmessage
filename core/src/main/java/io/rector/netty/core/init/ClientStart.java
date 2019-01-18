package io.rector.netty.core.init;

import io.reactor.netty.api.codec.Protocol;
import io.reactor.netty.api.exception.NotFindConfigException;
import io.reactor.netty.api.exception.NotSupportException;
import io.rector.netty.config.ClientConfig;
import io.rector.netty.config.ServerConfig;
import io.rector.netty.core.session.ServerSession;
import io.rector.netty.core.session.TcpServerSession;
import io.rector.netty.flow.plugin.FrameInterceptor;
import io.rector.netty.transport.ClientTransport;
import io.rector.netty.transport.ServerTransport;
import io.rector.netty.transport.method.ReactorMethodExtend;
import io.rector.netty.transport.socket.ClientSocketAdapter;
import io.rector.netty.transport.socket.RsocketAcceptor;
import io.rector.netty.transport.socket.ServerSocketAdapter;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.ipc.netty.NettyConnector;
import reactor.ipc.netty.NettyInbound;
import reactor.ipc.netty.NettyOutbound;
import reactor.ipc.netty.tcp.TcpClient;
import reactor.ipc.netty.tcp.TcpServer;

import java.util.Map;
import java.util.function.Consumer;

/**
 * @Auther: luxurong
 * @Date: 2019/1/18 13:39
 * @Description: 客户端start
 **/
@Slf4j
public class ClientStart extends  AbstractStart {



    private Consumer<Map<Protocol,Class<? extends NettyConnector>>> consumer = classes-> classes.put(Protocol.TCP, TcpClient.class);

    private static class StartBuilder{
        private static ClientStart start = new ClientStart();
    }


    public static ClientStart builder(){
        return ClientStart.StartBuilder.start;
    }


    private ClientStart() {
        super(ClientConfig.builder().build(), ReactorMethodExtend.builder().build());
    }


    @Override
    public Start interceptor(FrameInterceptor... frameInterceptor) {
        throw  new NotSupportException(" client not support frameInterceptor");
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends NettyConnector<? extends NettyInbound, ? extends NettyOutbound>> Mono<ServerSession<T>> connect() {
//        ClientTransport<T> clientTransport =new ClientTransport(socketFactory()
//                .accept(consumer)
//                .getSocket(config.getProtocol())
//                .orElseThrow(()->new NotFindConfigException("协议不存在")));
//        return rsocketAcceptor()
//                .map(rsocketAcceptor -> {
//                    ClientSocketAdapter<T> rsocket= (ClientSocketAdapter<T> )rsocketAcceptor.accept(() -> clientTransport,(ServerConfig)config,methodExtend);
//                    return   rsocket.start()
//                            .map(socket->new TcpServerSession(rsocket))
//                            .doOnError(ex->log.error("connect error:",ex))
//                            .log("server")
//                            .block();
//                });

        return null;
    }



//    private  Mono<RsocketAcceptor>  rsocketAcceptor(){
//        return Mono.just(ClientSocketAdapter::new);
//    }

}
