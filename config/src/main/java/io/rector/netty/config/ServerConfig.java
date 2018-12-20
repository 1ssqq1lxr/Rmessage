package io.rector.netty.config;

import io.netty.channel.Channel;
import lombok.Builder;
import lombok.Data;
import org.apache.logging.log4j.core.config.plugins.util.PluginRegistry;
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

    private  Long readIdle = 10*1000l;

    private Supplier<Runnable> readEvent = ()->()->{};

    private  Long writeIdle = 10*1000l;;

    private Supplier<Runnable> writeEvent = ()->()->{};;

    private  Consumer<ServerOptions.Builder<?>>  options ;

    private Consumer<? super NettyContext> afterNettyContextInit;

    private Consumer<? super Channel> afterChannelInit;

    private PluginRegistry pluginRegistry;


    @Override
    public InetSocketAddress getInetSocketAddress() {
        return null;
    }

    public Consumer<? extends ServerOptions.Builder<?>> getOptions() {
        this.options= builder ->builder.host(ip).port(port).afterChannelInit(afterChannelInit).afterNettyContextInit(afterNettyContextInit);
        return this.options;
    }


    @Override
    public void onReadIdle(Long l, Supplier<Runnable> readLe) {
            this.readIdle=l;
            this.readEvent=readLe;
    }


    @Override
    public void onWriteIdle(Long l, Supplier< Runnable> writeLe) {
        this.writeIdle=l;
        this.writeEvent=writeLe;
    }

    @Override
    public PluginRegistry getPluginRegistry() {
        return this.pluginRegistry;
    }

}
