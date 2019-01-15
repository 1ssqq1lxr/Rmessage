package io.rector.netty.transport.distribute;

import io.reactor.netty.api.codec.OnlineMessage;
import io.rector.netty.transport.socket.ServerSocketAdapter;
import lombok.Data;
import reactor.core.publisher.Mono;

@Data
public class ConnectionStateDistribute {

    private ServerSocketAdapter serverSocketAdapter;

    public ConnectionStateDistribute(ServerSocketAdapter serverSocketAdapter) {
        this.serverSocketAdapter = serverSocketAdapter;
    }

    public Mono<Void> init(OnlineMessage onlineMessage) {
        return Mono.create(sink->{

            sink.success();
        });
    }
}
