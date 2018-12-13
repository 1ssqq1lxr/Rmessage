package io.rector.netty.config;

import lombok.Builder;
import lombok.Data;
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

    public   Consumer<ServerOptions.Builder<?>>  options;

    @Override
    public InetSocketAddress getInetSocketAddress() {
        return null;
    }

    @Override
    public void setProtocol(Protocol protocol) {

    }



    public Consumer<? extends ServerOptions.Builder<?>> getOptions() {
        return options;
    }
}
