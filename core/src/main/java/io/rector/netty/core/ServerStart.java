package io.rector.netty.core;

import io.reactor.netty.api.exception.NotFindConfigException;
import io.rector.netty.config.Config;
import io.rector.netty.config.Protocol;
import io.rector.netty.config.ServerConfig;
import io.rector.netty.transport.socket.SocketAdapter;
import io.rector.netty.transport.ServerTransport;
import io.rector.netty.transport.socket.RsocketAcceptor;
import io.rector.netty.transport.socket.SocketFactory;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.publisher.UnicastProcessor;
import reactor.ipc.netty.NettyConnector;
import reactor.ipc.netty.NettyInbound;
import reactor.ipc.netty.NettyOutbound;
import reactor.ipc.netty.options.ServerOptions;
import reactor.ipc.netty.tcp.TcpClient;
import reactor.ipc.netty.tcp.TcpServer;
import reactor.ipc.netty.udp.UdpServer;

import java.util.Map;
import java.util.Objects;
import java.util.ServiceLoader;
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

    private Consumer<Map<Protocol,Class<? extends NettyConnector>>> consumer = classes->{
        classes.put(Protocol.TCP,TcpServer.class);
        classes.put(Protocol.UDP, UdpServer.class);
    };

    public ServerStart() {
        super(ServerConfig.builder().build());
    }

    private static class StartBuilder{
        private static ServerStart start = new ServerStart();
    }

    public static ServerStart builder(){
        return StartBuilder.start;
    }

    public <T extends NettyConnector< ? extends NettyInbound,? extends NettyOutbound>> Mono<PersistSession<T>> connect(){
//        Objects.requireNonNull(config.getOptions(),"请配置options");
        Class<? extends NettyConnector> s=  socketFactory()
                .accept(consumer)
                .getSocket(config.getProtocol())
                .orElseThrow(()->new NotFindConfigException("协议不存在"));
        ServerTransport<T> serverTransport =new ServerTransport(s
                ,(ServerConfig)config);
        return rsocketAcceptor()
                .map(rsocketAcceptor -> {
                         SocketAdapter<T> rsocket= rsocketAcceptor.accept(() -> serverTransport);
                         return   rsocket.start()
                                 .map(socket->new PersistSession<>(rsocket))
                                 .doOnError(ex-> log.error("connect error:",ex))
                                 .retry()
                                 .block();
                });
    }

    private  Mono<RsocketAcceptor>  rsocketAcceptor(){
        return Mono.just(SocketAdapter::new);
    }



    public static void  main(String[] a) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
       Mono<PersistSession<TcpClient>>mono=
            ServerStart
                .builder()
                .tcp()
                .ip("127.0.0.1")
                .port(1884)
//                .options(operations->{
//
//                })
                .connect();
        mono.subscribe(tcpClientPersistSession -> {
            tcpClientPersistSession.getRsocket().get();
            System.out.println("123");
        });

//        ServerStart
//                .builder()
//                .config(ServerConfig.builder().ip("127.0.0.1").port(8886).protocol(Protocol.TCP).options(operations-> operations.host("192.168.91.1")
//                        .port(1884)).build())
//                .connect(TcpServer.class)
//                .subscribe();
//        TcpServer.builder().options(cu->{}).build();
        countDownLatch.await();
    }







}
