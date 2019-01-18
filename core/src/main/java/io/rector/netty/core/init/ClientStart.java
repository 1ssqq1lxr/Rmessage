package io.rector.netty.core.init;

import io.reactor.netty.api.exception.NotSupportException;
import io.rector.netty.config.ClientConfig;
import io.rector.netty.core.session.ServerSession;
import io.rector.netty.flow.plugin.FrameInterceptor;
import io.rector.netty.transport.method.ReactorMethodExtend;
import reactor.core.publisher.Mono;
import reactor.ipc.netty.NettyConnector;
import reactor.ipc.netty.NettyInbound;
import reactor.ipc.netty.NettyOutbound;

/**
 * @Auther: luxurong
 * @Date: 2019/1/18 13:39
 * @Description: 客户端start
 **/
public class ClientStart extends  AbstractStart {


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
    public <T extends NettyConnector<? extends NettyInbound, ? extends NettyOutbound>> Mono<ServerSession<T>> connect() {
        return null;
    }
}
