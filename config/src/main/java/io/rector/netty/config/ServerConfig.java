package io.rector.netty.config;

import io.netty.channel.Channel;
import lombok.Builder;
import lombok.Data;
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
@Data
@Builder
public class ServerConfig implements Config{

    private  Protocol protocol;

    public   String ip;

    public   int port;

    private  Long readIdle;

    private Supplier<Runnable> readEvent;

    private  Long writeIdle;

    private Supplier<Runnable> writeEvent;

    private  Consumer<ServerOptions.Builder<?>>  options ;

    private Consumer<? super NettyContext> afterNettyContextInit;

    private Consumer<? super Channel> afterChannelInit;

    @Override
    public InetSocketAddress getInetSocketAddress() {
        return null;
    }

    public Consumer<? extends ServerOptions.Builder<?>> getOptions() {
        this.options= builder ->builder.host(ip).port(port).afterChannelInit(afterChannelInit).afterNettyContextInit(afterNettyContextInit);
        return this.options;
    }


}
