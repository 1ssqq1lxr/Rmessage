package io.rector.netty.transport.distribute;

import io.netty.buffer.Unpooled;
import io.reactor.netty.api.codec.TransportMessage;
import io.rector.netty.transport.connection.RConnection;
import io.rector.netty.transport.listener.MessageListener;
import lombok.extern.slf4j.Slf4j;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;

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
        return connection.getOutbound().send(Mono.just(Unpooled.wrappedBuffer(bs))).then();
    }


    public Disposable receive(MessageListener messageListener) {
       return connection.receiveMsg().subscribe(messageListener::accept);
    }

    public Mono<Void> send(TransportMessage buildMessage) {
        return connection.getOutbound().send(Mono.just(Unpooled.wrappedBuffer(buildMessage.getBytes()))).then();
    }
}
