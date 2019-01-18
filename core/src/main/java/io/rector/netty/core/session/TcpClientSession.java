package io.rector.netty.core.session;

import io.reactor.netty.api.codec.TransportMessage;
import io.rector.netty.transport.socket.ClientSocketAdapter;
import reactor.core.publisher.Mono;

/**
 * @Auther: luxurong
 * @Date: 2019/1/18 16:20
 * @Description:
 **/
public class TcpClientSession  implements ClientSession{

    private final ClientSocketAdapter clientSocketAdapter;


    public TcpClientSession(ClientSocketAdapter clientSocketAdapter) {
        this.clientSocketAdapter = clientSocketAdapter;
    }

    @Override
    public void dispose() {
        clientSocketAdapter.closeServer().subscribe();
    }

    @Override
    public Mono<Void> sendPoint(String userId, String body) {
        return null;
    }

    @Override
    public Mono<Void> sendGroup(String group, String body) {
        return null;
    }

    @Override
    public Mono<Void> sendMessage(TransportMessage message) {
        return null;
    }
}
