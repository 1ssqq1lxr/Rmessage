package io.rector.netty.transport.distribute;

import io.netty.buffer.Unpooled;
import io.reactor.netty.api.ChannelAttr;
import io.reactor.netty.api.codec.MessageBody;
import io.reactor.netty.api.codec.RConnection;
import io.reactor.netty.api.codec.TransportMessage;
import io.reactor.netty.api.exception.NoGroupCollectorException;
import io.rector.netty.transport.socket.ServerSocketAdapter;
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
@Slf4j
public class DirectServerMessageHandler implements ServerMessageHandler{

    private final ServerSocketAdapter serverSocketAdapter;


    public DirectServerMessageHandler(ServerSocketAdapter serverSocketAdapter) {
        this.serverSocketAdapter = serverSocketAdapter;
    }

    public Mono<Void>  sendOne(TransportMessage message, Mono<Void> offline){
        return   Mono.create(monoSink -> {
            MessageBody messageBody=(MessageBody) message.getMessageBody();
            Optional<Set<RConnection>> rConnection= Optional.ofNullable(serverSocketAdapter.getConnectionManager().getUserMultiConnection(messageBody.getTo()));
            if(rConnection.isPresent()){ // 发送
                byte[] bytes=message.getBytes();
                rConnection.get().stream().forEach(connection -> connection.getOutbound().send(Mono.just(Unpooled.wrappedBuffer(bytes))).then().subscribe());
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
                byte[] bs=message.getBytes();
                // 发送所有人 check在线状态
                ids.get()
                        .stream()
                        .filter(id->!id.equals(ChannelAttr.getUserId(message.getConnection().getInbound())))
                        .forEach(user->{
                            Optional<Set<RConnection>> sets=Optional.ofNullable(serverSocketAdapter.getConnectionManager().getUserMultiConnection(user));
                            if(sets.isPresent() && sets.get().size()>0){
                                sets.get().stream()
                                        .forEach(connection -> message.getConnection().getOutbound().send(Mono.just(Unpooled.wrappedBuffer(bs))).then().subscribe());
                            }
                            else {
                                log.info(" group offline message {}",message);
                                consumer.apply(user).subscribe(); }
                        });
                monoSink.success();
            }
            else {
                monoSink.error(new NoGroupCollectorException("not find groupCollector"));
            }
        });

    }


    public Mono<Void>  sendPong(TransportMessage message) {
        return message.getConnection().getOutbound().send(Mono.just(Unpooled.wrappedBuffer(message.getBytes()))).then();
    }

    public Mono<Void>  sendOffline(TransportMessage transportMessage,Set<String> users) {
        return Mono.create(monoSink -> {
            byte[] bytes = transportMessage.getBytes();
            users.stream()
                    .map(id->serverSocketAdapter.getConnectionManager().getUserMultiConnection(id))
                    .flatMap(rConnections -> rConnections.stream())
                    .forEach(connection -> transportMessage.getConnection().getOutbound().send(Mono.just(Unpooled.wrappedBuffer(bytes))).then().subscribe());
            monoSink.success();
        });

    }

}
