package io.rector.netty.transport.distribute;

import io.netty.buffer.Unpooled;
import io.reactor.netty.api.codec.MessageBody;
import io.rector.netty.transport.connction.RConnection;
import io.rector.netty.transport.socket.ServerSocketAdapter;
import lombok.Data;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;
import reactor.core.publisher.UnicastProcessor;

import java.util.List;
import java.util.Optional;

/**
 * @Auther: lxr
 * @Date: 2018/12/27 19:23
 * @Description: 消息传输逻辑
 */
@Data
public class DirectServerMessageDistribute {

    private final ServerSocketAdapter serverSocketAdapter;


    public DirectServerMessageDistribute(ServerSocketAdapter serverSocketAdapter) {
        this.serverSocketAdapter = serverSocketAdapter;
    }

    public Mono<Void>  sendOne(MessageBody body, Mono<Void> offline){
        return   Mono.create(monoSink -> {
            Optional<RConnection> rConnection= Optional.ofNullable((RConnection) serverSocketAdapter.getIds().get(body.getTo()));
            if(rConnection.isPresent()){ // 发送
//                rConnection.get().getOutbound().then().subscribe();
            }
            else
                offline.subscribe();
            monoSink.success();
        });
    }

    public Mono<Void>  sendGroup(MessageBody body){
//        Optional.ofNullable((List<RConnection>) serverSocketAdapter.getKeys().get(key))
//                .ifPresent(rConnections -> rConnections.stream().forEach(rConnection -> {
//                    rConnection.getOutbound().send(msg.map(Unpooled::wrappedBuffer)).then().subscribe();
//                }));

        return   Mono.create(monoSink -> {
//            Optional<RConnection> rConnection= Optional.ofNullable((RConnection) serverSocketAdapter.getIds().get(body.getTo()));

            monoSink.success();
        });
    }

}
