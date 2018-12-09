package io.rector.netty.core;

import io.rector.netty.core.socket.TcpSocket;
import io.rector.netty.transport.ServerTransport;
import io.rector.netty.transport.connction.DuplexConnection;
import io.rector.netty.transport.socket.Rsocket;
import io.rector.netty.config.Config;
import io.rector.netty.config.Protocol;
import io.rector.netty.config.ServerConfig;
import io.rector.netty.transport.socket.RsocketAcceptor;
import lombok.Data;
import reactor.core.publisher.Mono;
import reactor.core.publisher.UnicastProcessor;
import reactor.ipc.netty.tcp.TcpServer;

import java.io.Closeable;
import java.io.IOException;

/**
 * @Auther: lxr
 * @Date: 2018/12/7 17:27
 * @Description:
 */
@Data
public class ServerStart implements Closeable {

    private static UnicastProcessor<Operation> operations =UnicastProcessor.create();

    public Mono<DuplexConnection> connect(ServerConfig config){
        TcpServer tcpServer = TcpServer.create();
        ServerTransport serverTransport =new ServerTransport(tcpServer,config);
        return serverTransport.connect();
    }


    public RsocketAcceptor<TcpServer> acceptorScoket(){
        return  TcpSocket::new;
    }



    public  static ServerStart.builder builder(){
            return  new ServerStart.builder();
    }

    @Override
    public void close() throws IOException {
//        rsocket.close(()->{});
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
