package io.rector.netty.transport.distribute;

import io.netty.buffer.Unpooled;
import io.reactor.netty.api.codec.MessageBody;
import io.reactor.netty.api.codec.TransportMessage;
import io.reactor.netty.api.exception.NoGroupCollectorException;
import io.rector.netty.transport.connction.RConnection;
import io.rector.netty.transport.socket.ServerSocketAdapter;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

/**
 * @Auther: lxr
 * @Date: 2018/12/27 19:23
 * @Description: 消息传输逻辑
 */
@Data
@Slf4j
public class DirectServerMessageDistribute {

    private final ServerSocketAdapter serverSocketAdapter;


    public DirectServerMessageDistribute(ServerSocketAdapter serverSocketAdapter) {
        this.serverSocketAdapter = serverSocketAdapter;
    }

    public Mono<Void>  sendOne(TransportMessage message, Mono<Void> offline){
        return   Mono.create(monoSink -> {
            MessageBody messageBody=(MessageBody) message.getMessageBody();
            Optional<RConnection> rConnection= Optional.ofNullable((RConnection) serverSocketAdapter.getIds().get(messageBody.getTo()));
            if(rConnection.isPresent()){ // 发送
                rConnection.get().getOutbound().send(Mono.just(Unpooled.wrappedBuffer(message.getBytes()))).then().subscribe();
            }
            else
                offline.subscribe();
            monoSink.success();
        });
    }


    public Mono<Void>  sendGroup(TransportMessage message, Function<String, Mono<Void>> consumer){
        return   Mono.create(monoSink -> {
            MessageBody messageBody=(MessageBody) message.getMessageBody();
            Optional<Set<String>> ids=Optional.ofNullable(serverSocketAdapter.getGroupCollector().loadGroupUser(messageBody.getTo()));
            if(ids.isPresent()){
                // 发送所有人 check在线状态
                ids.get().stream().forEach(id->{
                    Optional<RConnection> connection=Optional.ofNullable((RConnection) serverSocketAdapter.getIds().get(id));
                    if(!connection.isPresent()){
                        consumer.apply(id).subscribe();
                        log.info(" group offline message {}",message);
                    }
                    else
                        connection.get().getOutbound().send(Mono.just(Unpooled.wrappedBuffer(message.getBytes()))).then().subscribe();
                });
                monoSink.success();
            }
            else {
                monoSink.error(new NoGroupCollectorException("not find groupCollector"));
            }
        });

    }


    public Mono<Void>  sendPong(TransportMessage message) {
        return Mono.fromRunnable(()-> message.getOutbound().send(Mono.just(Unpooled.wrappedBuffer(message.getBytes()))).then().subscribe());
    }
}
