package io.rector.netty.config;

import lombok.Builder;
import lombok.Data;
import reactor.ipc.netty.options.ServerOptions;

import java.net.InetSocketAddress;
import java.util.function.Consumer;

/**
 * @Auther: lxr
 * @Date: 2018/12/7 16:09
 * @Description:
 */
@Builder
@Data
public class ServerConfig implements Config{

    private  Protocol protocol;

    public   String ip;

    public   Integer port;

    @Override
    public InetSocketAddress getInetSocketAddress() {
        return null;
    }

    @Override
    public void setProtocol(Protocol protocol) {

    }

}
