package io.rector.netty.core.init;

import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.reactor.netty.api.Idle;
import io.reactor.netty.api.codec.ClientType;
import io.reactor.netty.api.codec.Protocol;
import io.reactor.netty.api.exception.NotSupportException;
import io.rector.netty.config.Config;
import io.rector.netty.transport.codec.MessageDecoder;
import io.rector.netty.transport.method.MethodExtend;
import io.rector.netty.transport.socket.SocketFactory;
import reactor.ipc.netty.NettyConnector;
import reactor.ipc.netty.NettyInbound;
import reactor.ipc.netty.NettyOutbound;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @Auther: lxr
 * @Date: 2018/12/13 17:51
 * @Description:
 */
abstract class AbstractStart implements Start {

    protected final Config config;

    protected final MethodExtend methodExtend;

    public AbstractStart(Config config, MethodExtend methodExtend) {
        this.config = config;
        this.methodExtend = methodExtend;
        methodExtend.setConfig(config);
    }

    @Override
    public Start tcp() {
        config.setProtocol(Protocol.TCP);
        methodExtend.setAfterNettyContextInit(nettyContext -> nettyContext.addHandler("decoder",new MessageDecoder()));
        return this;
    }


    @Override
    public Start websocket() {
        config.setProtocol(Protocol.WS);
        return this;
    }

    @Override
    public Start userId(String userId) {
       throw new NotSupportException("server not support set  userId");
    }


    @Override
    public Start ip(String ip) {
        config.setIp(ip);
        return this;
    }

    @Override
    public Start port(int port) {
        config.setPort(port);
        return this;
    }

    @Override
    public Start password(String password) {
         throw new NotSupportException("server not support set  password");
    }


    @Override
    public Start onReadIdle(Long l) {
        onReadIdle(l,null);
        return this;
    }

    @Override
    public Start onReadIdle(Long l, Supplier< Runnable> readLe) {
        methodExtend.setReadIdle(Idle.builder().time(l).event(readLe).build());
        return this;
    }

    @Override
    public Start onWriteIdle(Long l) {
        onWriteIdle(l,null);
        return this;
    }

    @Override
    public Start onWriteIdle(Long l, Supplier<Runnable> write) {
        methodExtend.setReadIdle(Idle.builder().time(l).event(write).build());
        return this;
    }

    @Override
    public <T> Start option(ChannelOption<T> key, T value) {
        return null;
    }


//    @Override
//    public Start setAfterNettyContextInit(Consumer<? super NettyContext> afterNettyContextInit) {
//        methodExtend.setAfterNettyContextInit(afterNettyContextInit);
//        return this;
//    }

    @Override
    public Start setAfterChannelInit(Consumer<? super Channel> afterChannelInit) {
        methodExtend.setAfterChannelInit(afterChannelInit);
        return this;
    }

    protected  SocketFactoryAcceptor socketFactory(){
        return SocketFactory::new;
    }


    protected @FunctionalInterface interface SocketFactoryAcceptor{

        SocketFactory accept(Consumer<Map<Protocol,Class<? extends NettyConnector< ? extends NettyInbound,? extends NettyOutbound>> >> consumer);

    }

    @Override
    public Start setClientType(ClientType type) {
           // null
        return this;
    }
}
