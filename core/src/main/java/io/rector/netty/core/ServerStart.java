package io.rector.netty.core;

import io.rector.netty.config.Protocol;
import io.rector.netty.config.ServerConfig;
import io.rector.netty.transport.socket.SocketAdapter;
import io.rector.netty.transport.ServerTransport;
import io.rector.netty.transport.socket.Rsocket;
import io.rector.netty.transport.socket.RsocketAcceptor;
import io.rector.netty.transport.socket.SocketFactory;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.publisher.UnicastProcessor;
import reactor.ipc.netty.NettyConnector;
import reactor.ipc.netty.NettyInbound;
import reactor.ipc.netty.NettyOutbound;

import java.util.Objects;


/**
 * @Auther: lxr
 * @Date: 2018/12/7 17:27
 * @Description:
 */
@Data
@Slf4j
public class ServerStart{

    private ServerConfig config;

    private static class Start{
        private static ServerStart start = new ServerStart();
    }
    private ServerStart(){

    }
    public static ServerStart builder(){
        return Start.start;
    }

    public  ServerStart config(ServerConfig config){
        this.config=config;
        return this;
    }

    private static UnicastProcessor<Operation> operations =UnicastProcessor.create();

    private SocketFactory socketFactory = new SocketFactory(sockets->{
        RsocketAcceptor rs= SocketAdapter::new;
        sockets.put(Protocol.TCP,Mono.just(rs));
    } );

    public <T extends NettyConnector< ? extends NettyInbound,? extends NettyOutbound>> Mono<PersistSession<T>> connect(T t){
        Objects.requireNonNull(config.getOptions(),"请配置options");
        ServerTransport<T> serverTransport =new ServerTransport(t,config);
        return socketFactory.getSocket(config.getProtocol())
                .map(rsocketAcceptor -> {
                         SocketAdapter<T> rsocket= rsocketAcceptor.accept(() -> serverTransport);
                         return   rsocket.start()
                                 .map(socket->new PersistSession<>(rsocket))
                                 .doOnError(ex-> log.error("connect error:",ex))
                                 .retry()
                                 .block();
                });
    }


}
