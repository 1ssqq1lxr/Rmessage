package io.rector.netty.core.session;

import io.reactor.netty.api.codec.MessageBody;
import io.reactor.netty.api.codec.ProtocolCatagory;
import io.reactor.netty.api.codec.TransportMessage;
import io.rector.netty.config.ClientConfig;
import io.rector.netty.transport.listener.MessageListener;
import io.rector.netty.transport.socket.ClientSocketAdapter;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.concurrent.atomic.LongAdder;

/**
 * @Auther: luxurong
 * @Date: 2019/1/18 16:20
 * @Description:
 **/
public class TcpClientSession  implements ClientSession{

    private final ClientSocketAdapter clientSocketAdapter;

    private ClientConfig clientConfig;

    private LongAdder longAdder  = new LongAdder();

    public TcpClientSession(ClientSocketAdapter clientSocketAdapter) {
        this.clientSocketAdapter = clientSocketAdapter;
        this.clientConfig=(ClientConfig)clientSocketAdapter.getConfig();
    }

    @Override
    public void dispose() {
        clientSocketAdapter.closeServer().subscribe();
    }

    @Override
    public Mono<Void> sendPoint(String userId, String body) {
        Objects.requireNonNull(userId,"userId not null");
        Objects.requireNonNull(body,"body not null");
        return clientSocketAdapter.getDirectClientMessageHandler().send(buildMessage(userId,ProtocolCatagory.ONE,body));
    }

    @Override
    public Mono<Void> sendGroup(String group, String body) {
        return clientSocketAdapter.getDirectClientMessageHandler().send(buildMessage(group,ProtocolCatagory.GROUP,body));
    }

    @Override
    public Mono<Void> sendMessage(TransportMessage message) {
        return clientSocketAdapter.getDirectClientMessageHandler().send(message);
    }

    @Override
    public Disposable accept(MessageListener messageListener) {
        return clientSocketAdapter.getDirectClientMessageHandler().receive(messageListener);
    }


    private TransportMessage buildMessage(String to ,ProtocolCatagory type,String body){
        return TransportMessage
                .builder()
                .type(type)
                .clientType(clientConfig.getClientType())
                .discard(false)
                .messageBody(MessageBody
                        .builder()
                        .from(clientConfig.getUserId())
                        .to(to)
                        .messageId(longAdder.longValue())
                        .body(body)
                        .timestammp(System.currentTimeMillis())
                        .build()
                ).build();

    }

}
