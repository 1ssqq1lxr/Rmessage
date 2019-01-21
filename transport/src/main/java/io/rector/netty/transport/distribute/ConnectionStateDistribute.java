package io.rector.netty.transport.distribute;

import io.reactor.netty.api.ChannelAttr;
import io.reactor.netty.api.codec.ConnectionState;
import io.reactor.netty.api.codec.TransportMessage;
import io.rector.netty.transport.connection.ConnectionManager;
import io.rector.netty.transport.socket.ServerSocketAdapter;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

/**
  * @Author luxurong
  * @Description //TODO 2019/1/18
  * @Date 15:38 2019/1/18  连接状态处理
  **/
@Slf4j
public class ConnectionStateDistribute {

    private ServerSocketAdapter serverSocketAdapter;

    public ConnectionStateDistribute(ServerSocketAdapter serverSocketAdapter) {
        this.serverSocketAdapter = serverSocketAdapter;
    }

    /**
     * @return  mono
     */
    public Mono<Void> init(TransportMessage message, ConnectionManager connectionManager, AtomicBoolean atomicBoolean) {
        return Mono.create(sink->{
            ConnectionState connectionState =(ConnectionState)message.getMessageBody();
            ChannelAttr.boundUserId(message.getConnection().getInbound(), connectionState.getUserId()); //  绑定id
            ChannelAttr.boundClientType(message.getConnection().getInbound(),  message.getClientType()); //  绑定clietType
            connectionManager.acceptConnection(connectionState.getUserId(),message.getConnection()); // 维护 connection relation
            atomicBoolean.compareAndSet(false,true);
            // 拉取离线消息 每次10条
            Optional.ofNullable(serverSocketAdapter.getOffMessageHandler())
                    .ifPresent(offMessageHandler -> serverSocketAdapter.getOffMessageHandler().getToMessages(connectionState.getUserId(),message.getClientType())
                            .buffer(10)
                            .delayElements(Duration.ofMillis(100), Schedulers.elastic())
                            .limitRate(10)
                            .doOnError(throwable -> log.error("offline message handler {} ",throwable))
                            .subscribe(msg->{}));
            sink.success();
        });
    }

}
