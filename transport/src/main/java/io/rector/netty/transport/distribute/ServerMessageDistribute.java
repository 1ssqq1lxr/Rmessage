package io.rector.netty.transport.distribute;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.rector.netty.transport.connction.RConnection;
import io.rector.netty.transport.socket.ServerSocketAdapter;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.omg.CORBA.Object;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
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
public class ServerMessageDistribute {

    private final ServerSocketAdapter serverSocketAdapter;


    public ServerMessageDistribute(ServerSocketAdapter serverSocketAdapter) {
        this.serverSocketAdapter = serverSocketAdapter;
    }

    private UnicastProcessor<byte[]> offLine = UnicastProcessor.create();


    public void  sendOne(String key, Mono<byte[]> msg){
       Optional<RConnection> rConnection= Optional.ofNullable((RConnection) serverSocketAdapter.getIds().get(key));
       if(rConnection.isPresent()){ // 发送
           rConnection.get().getOutbound().send(msg.map(Unpooled::wrappedBuffer)).then().subscribe();
       }
       else { // 处理离线消息
           offLine.startWith(msg);
       }
    }

    public void  sendGroup(String key, Mono<byte[]> msg){
        Optional.ofNullable((List<RConnection>) serverSocketAdapter.getKeys().get(key))
                .ifPresent(rConnections -> rConnections.stream().forEach(rConnection -> {
                    rConnection.getOutbound().send(msg.map(Unpooled::wrappedBuffer)).then().subscribe();
                }));
    }

}
