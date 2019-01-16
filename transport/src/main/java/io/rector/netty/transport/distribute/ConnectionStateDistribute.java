package io.rector.netty.transport.distribute;

import io.reactor.netty.api.ChannelAttr;
import io.reactor.netty.api.codec.OnlineMessage;
import io.rector.netty.transport.socket.ServerSocketAdapter;
import lombok.Data;
import reactor.core.publisher.Mono;
import reactor.ipc.netty.NettyInbound;

@Data
public class ConnectionStateDistribute {

    private ServerSocketAdapter serverSocketAdapter;

    public ConnectionStateDistribute(ServerSocketAdapter serverSocketAdapter) {
        this.serverSocketAdapter = serverSocketAdapter;
    }

    /**
     * @param onlineMessage 在线消息
     * @param inbound   inbound
     * @return  mono
     */
    public Mono<Void> init(OnlineMessage onlineMessage, NettyInbound inbound) {
        return Mono.create(sink->{
            ChannelAttr.boundUserId(inbound,onlineMessage.getUserId()); //  绑定id
            // 拉取离线消息
//            serverSocketAdapter.getOfflineMessagePipeline().
            sink.success();
        });
    }
}
