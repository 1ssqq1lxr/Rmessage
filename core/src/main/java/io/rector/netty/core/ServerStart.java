package io.rector.netty.core;

import io.reactor.netty.api.exception.NotFindConfigException;
import io.rector.netty.config.Protocol;
import io.rector.netty.config.ServerConfig;
import io.rector.netty.core.session.TcpSession;
import io.rector.netty.transport.socket.Rsocket;
import io.rector.netty.transport.socket.ServerSocketAdapter;
import io.rector.netty.transport.ServerTransport;
import io.rector.netty.transport.socket.RsocketAcceptor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.publisher.UnicastProcessor;
import reactor.ipc.netty.NettyConnector;
import reactor.ipc.netty.NettyInbound;
import reactor.ipc.netty.NettyOutbound;
import reactor.ipc.netty.tcp.TcpServer;
import reactor.ipc.netty.udp.UdpServer;

import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;


/**
 * @Auther: lxr
 * @Date: 2018/12/7 17:27
 * @Description:
 */
@Data
@Slf4j
public class ServerStart extends AbstractStart {

    private static UnicastProcessor<Operation> operations =UnicastProcessor.create();

    private Consumer<Map<Protocol,Class<? extends NettyConnector>>> consumer = classes-> classes.put(Protocol.TCP,TcpServer.class);

    public ServerStart() {
        super(ServerConfig.builder().build());
    }

    private static class StartBuilder{
        private static ServerStart start = new ServerStart();
    }

    public static ServerStart builder(){
        return StartBuilder.start;
    }

    public <T extends NettyConnector< ? extends NettyInbound,? extends NettyOutbound>> Mono<TcpSession<T>> connect(){
        ServerTransport<T> serverTransport =new ServerTransport(socketFactory()
                .accept(consumer)
                .getSocket(config.getProtocol())
                .orElseThrow(()->new NotFindConfigException("协议不存在")),(ServerConfig)config);
        return rsocketAcceptor()
                .map(rsocketAcceptor -> {
                      ServerSocketAdapter<T> rsocket= (ServerSocketAdapter<T> )rsocketAcceptor.accept(() -> serverTransport);
                         return   rsocket.start()
                                 .map(socket->new TcpSession(rsocket))
                                 .doOnError(ex-> log.error("connect error:",ex))
                                 .retry()
                                 .block();
                });
    }

    private  Mono<RsocketAcceptor>  rsocketAcceptor(){
        return Mono.just(ServerSocketAdapter::new);
    }



    public static void  main(String[] a) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
            ServerStart
                .builder()
                .tcp()
                .ip("127.0.0.1")
                .port(1884)
                .setAfterNettyContextInit(nettyContext -> {
                        // netty连接设置
                })
                .setAfterChannelInit(channel -> {
                    //  channel设置
                })
                .connect()
                .subscribe();
        countDownLatch.await();
    }







}
