package io.rector.netty.transport.distribute;

import io.reactor.netty.api.ChannelAttr;
import io.reactor.netty.api.codec.OnlineMessage;
import io.reactor.netty.api.codec.TransportMessage;
import io.rector.netty.transport.socket.ServerSocketAdapter;
import lombok.Data;
import reactor.core.publisher.Mono;

@Data
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
            ChannelAttr.boundUserId(message.getInbound(),((OnlineMessage)message.getMessageBody()).getUserId()); //  绑定id
            // 拉取离线消息
//            serverSocketAdapter.getOfflineMessagePipeline().
            sink.success();
        });
    }

}
