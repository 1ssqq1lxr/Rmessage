package io.rector.netty.transport.distribute;

import io.netty.buffer.Unpooled;
import io.reactor.netty.api.ChannelAttr;
import io.reactor.netty.api.codec.MessageBody;
import io.reactor.netty.api.codec.TransportMessage;
import io.reactor.netty.api.exception.NoGroupCollectorException;
import io.rector.netty.transport.connction.RConnection;
import io.rector.netty.transport.socket.ClientSocketAdapter;
import io.rector.netty.transport.socket.ServerSocketAdapter;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.ipc.netty.NettyInbound;
import reactor.ipc.netty.NettyOutbound;

import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

/**
 * @Auther: lxr
 * @Date: 2018/12/27 19:23
 * @Description: 消息传输逻辑
 */
@Slf4j
public class DirectClientMessageHandler {

    private final  RConnection connection;

    public DirectClientMessageHandler(RConnection connection) {
        this.connection=connection;
    }


    public Mono<Void>  sendPing( byte[] bs) {
        return Mono.fromRunnable(()-> connection.getOutbound().send(Mono.just(Unpooled.wrappedBuffer(bs))).then().subscribe());
    }



}
