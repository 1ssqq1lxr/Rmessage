package io.rector.netty.config;

import io.netty.channel.Channel;
import reactor.ipc.netty.NettyContext;
import reactor.ipc.netty.options.ServerOptions;

import java.net.InetSocketAddress;
import java.util.function.Consumer;
import java.util.function.Supplier;


/**
 * @Auther: lxr
 * @Date: 2018/12/7 16:09
 * @Description:
 */
public interface Config {

    void setIp(String ip);

    void setPort(int port);

    Protocol getProtocol();

    InetSocketAddress getInetSocketAddress();

    void setProtocol(Protocol protocol);

    void setOptions(Consumer<ServerOptions.Builder<?>> options);

    Consumer<? extends ServerOptions.Builder<?>> getOptions();

    void setAfterNettyContextInit(Consumer<? super NettyContext> afterNettyContextInit);

    void setAfterChannelInit(Consumer<? super Channel> afterChannelInit);


    void onReadIdle(Long l, Supplier< Runnable> readLe);

    void onWriteIdle(Long l, Supplier< Runnable> writeLe);


    Long getReadIdle() ;

    Supplier<Runnable> getReadEvent();

    Long getWriteIdle();

    Supplier<Runnable> getWriteEvent();



}
