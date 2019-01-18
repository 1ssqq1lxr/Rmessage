package io.rector.netty.transport.distribute;

import io.reactor.netty.api.ChannelAttr;
import io.reactor.netty.api.codec.OnlineMessage;
import io.reactor.netty.api.codec.TransportMessage;
import io.rector.netty.transport.socket.ServerSocketAdapter;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

public class ConnectionStateDistribute {

    private ServerSocketAdapter serverSocketAdapter;

    public ConnectionStateDistribute(ServerSocketAdapter serverSocketAdapter) {
        this.serverSocketAdapter = serverSocketAdapter;
    }

    /**
     * @return  mono
     */
    public Mono<Void> init(TransportMessage message) {
        return Mono.create(sink->{
            OnlineMessage onlineMessage=(OnlineMessage)message.getMessageBody();
            ChannelAttr.boundUserId(message.getInbound(),onlineMessage.getUserId()); //  绑定id
            // 拉取离线消息 每次10条
            serverSocketAdapter.getOffMessageHandler().getToMessages(onlineMessage.getUserId(),message.getClientType())
             .buffer(10)
             .delayElements(Duration.ofMillis(100), Schedulers.elastic())
             .limitRate(10)
                    .subscribe(msg->{});
            sink.success();
        });
    }

}
