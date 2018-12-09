package io.rector.netty.core;

import io.rector.netty.transport.socket.Rsocket;
import io.rector.netty.config.Config;
import io.rector.netty.config.Protocol;
import io.rector.netty.config.ServerConfig;
import lombok.Data;
import reactor.core.publisher.UnicastProcessor;
import reactor.ipc.netty.NettyConnector;
import reactor.ipc.netty.NettyInbound;
import reactor.ipc.netty.NettyOutbound;

import java.io.Closeable;
import java.io.IOException;

/**
 * @Auther: lxr
 * @Date: 2018/12/7 17:27
 * @Description:
 */
@Data
public class ServerStart<T extends NettyConnector< ? extends NettyInbound,? extends NettyOutbound>> implements Closeable {

    private static UnicastProcessor<Operation> operations =UnicastProcessor.create();

    private Rsocket<T> rsocket;

    private ServerStart(Config config){

    }

    public  static ServerStart.builder builder(){
            return  new ServerStart.builder();
    }

    @Override
    public void close() throws IOException {
        rsocket.close(()->{});
    }

    public static class builder{

        private  Protocol protocol;

        public   String ip;

        public   Integer port;

        public  ServerStart build(){
            return  new ServerStart(ServerConfig.builder()
                    .ip(ip)
                    .port(port)
                    .protocol(protocol).build());
        }






    }

}
