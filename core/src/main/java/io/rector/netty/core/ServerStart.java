package io.rector.netty.core;

import io.rector.netty.config.Protocol;
import io.rector.netty.config.ServerConfig;
import io.rector.netty.core.socket.TcpSocket;
import io.rector.netty.transport.ServerTransport;
import io.rector.netty.transport.connction.DuplexConnection;
import io.rector.netty.transport.socket.Rsocket;
import io.rector.netty.transport.socket.RsocketAcceptor;
import io.rector.netty.transport.socket.SocketFactory;
import lombok.Data;
import reactor.core.publisher.Mono;
import reactor.core.publisher.UnicastProcessor;
import reactor.ipc.netty.NettyConnector;
import reactor.ipc.netty.NettyInbound;
import reactor.ipc.netty.NettyOutbound;
import reactor.ipc.netty.tcp.TcpServer;

import java.io.Closeable;
import java.io.IOException;

/**
 * @Auther: lxr
 * @Date: 2018/12/7 17:27
 * @Description:
 */
@Data
public class ServerStart implements Closeable {

    private static UnicastProcessor<Operation> operations =UnicastProcessor.create();

    private SocketFactory socketFactory = new SocketFactory(sockets->{
        sockets.put(
                Protocol.TCP,Mono.defer(()->{
                         RsocketAcceptor<TcpServer> rsocketAcceptor= TcpSocket::new;
                         return Mono.just(rsocketAcceptor); })
        );
    });

    public <T extends NettyConnector< ? extends NettyInbound,? extends NettyOutbound>> Mono<ApiOperation<T>> connect(ServerConfig config){
        TcpServer tcpServer = TcpServer.create();
        ServerTransport serverTransport =new ServerTransport(tcpServer,config);
        return socketFactory.getSocket(config.getProtocol())
                .map(rsocketAcceptor -> {
                         Rsocket<T> rsocket= (Rsocket<T>) rsocketAcceptor.accept(() -> serverTransport);
                         return new ApiOperation<>(rsocket);
                });
    }

    @Override
    public void close() throws IOException {
//        rsocket.close(()->{});
    }

}
