package io.rector.netty.transport.distribute;

import io.netty.buffer.Unpooled;
import io.reactor.netty.api.codec.MessageBody;
import io.reactor.netty.api.codec.TransportMessage;
import io.reactor.netty.api.exception.NoGroupCollectorException;
import io.rector.netty.transport.connction.RConnection;
import io.rector.netty.transport.socket.ServerSocketAdapter;
import lombok.Data;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.Set;

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

    public Mono<Void>  sendOne(TransportMessage body, Mono<Void> offline){
        return   Mono.create(monoSink -> {
            MessageBody messageBody=(MessageBody) body.getMessageBody();
            Optional<RConnection> rConnection= Optional.ofNullable((RConnection) serverSocketAdapter.getIds().get(messageBody.getTo()));
            if(rConnection.isPresent()){ // 发送
                rConnection.get().getOutbound().send(Mono.just(Unpooled.wrappedBuffer(body.getBytes()))).then().subscribe();
            }
            else
                offline.subscribe();
            monoSink.success();
        });
    }

    public Mono<Void>  sendGroup(MessageBody body){
      return   Mono.create(monoSink -> {
              Optional<Set<String>> ids=Optional.ofNullable(serverSocketAdapter.getGroupCollector().loadGroupUser(body.getTo()));
                            if(ids.isPresent()){
                                // 发送所有人 check在线状态
                                monoSink.success();
                            }
                            else {
                                monoSink.error(new NoGroupCollectorException("not find groupCollector"));
                            }
        });

    }

}
