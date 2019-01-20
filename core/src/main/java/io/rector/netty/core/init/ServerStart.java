package io.rector.netty.core.init;

import io.reactor.netty.api.codec.Protocol;
import io.reactor.netty.api.exception.NotFindConfigException;
import io.rector.netty.config.ServerConfig;
import io.rector.netty.core.session.TcpServerSession;
import io.rector.netty.flow.plugin.FrameInterceptor;
import io.rector.netty.flow.plugin.PluginRegistry;
import io.rector.netty.flow.plugin.Plugins;
import io.rector.netty.transport.ServerTransport;
import io.rector.netty.transport.method.ReactorMethodExtend;
import io.rector.netty.transport.socket.RsocketAcceptor;
import io.rector.netty.transport.socket.ServerSocketAdapter;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;
import reactor.ipc.netty.NettyConnector;
import reactor.ipc.netty.NettyInbound;
import reactor.ipc.netty.NettyOutbound;
import reactor.ipc.netty.tcp.TcpServer;

import java.util.Map;
import java.util.function.Consumer;


/**
 * @Auther: lxr
 * @Date: 2018/12/7 17:27
 * @Description:
 */
@Data
@Slf4j
public class ServerStart    extends AbstractStart {


    private Consumer<Map<Protocol,Class<? extends NettyConnector< ? extends NettyInbound,? extends NettyOutbound>> >> consumer = classes-> classes.put(Protocol.TCP,TcpServer.class);

    private PluginRegistry registry = Plugins.defaultPlugins();


    public ServerStart() {
        super(ServerConfig.builder().build(), ReactorMethodExtend.builder().build());
    }

    private static class StartBuilder{
        private static ServerStart start = new ServerStart();
    }


    public static ServerStart  builder(){
        return StartBuilder.start;
    }



    @Override
    public Start interceptor(FrameInterceptor... frameInterceptor) {
        registry.addServerPlugin(frameInterceptor);
        return this;
    }


    @Override
    public   Mono<Disposable> connect(){
        config.check();
        ServerTransport serverTransport =new ServerTransport(socketFactory()
                .accept(consumer)
                .getSocket(config.getProtocol())
                .orElseThrow(()->new NotFindConfigException("协议不存在")));
        return rsocketAcceptor()
                .map(rsocketAcceptor -> {
                      ServerSocketAdapter rsocket= (ServerSocketAdapter )rsocketAcceptor.accept(() -> serverTransport,registry,(ServerConfig)config,methodExtend);
                         return   rsocket.start()
                                 .map(socket->new TcpServerSession(rsocket))
                                 .doOnError(ex->log.error("connect error:",ex))
                                 .log("server")
                                 .block();
                });
    }



    private  Mono<RsocketAcceptor>  rsocketAcceptor(){
        return Mono.just(ServerSocketAdapter::new);
    }









}
