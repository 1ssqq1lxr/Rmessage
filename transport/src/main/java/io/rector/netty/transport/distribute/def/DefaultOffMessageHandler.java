package io.rector.netty.transport.distribute.def;


import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import io.reactor.netty.api.codec.ClientType;
import io.reactor.netty.api.codec.OfflineMessage;
import io.reactor.netty.api.codec.TransportMessage;
import io.rector.netty.transport.distribute.OffMessageHandler;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 默认离线消息
 */
public class DefaultOffMessageHandler implements OffMessageHandler {

    private Multimap<String, TransportMessage> messages = Multimaps.synchronizedMultimap(LinkedHashMultimap.create()); // user -> list<RConnection>

    /**
     *
     * @param message 离线消息 针对个人 from 来自哪里的 example 【 point -> point 】【 point -> group】
     * @return
     */
    @Override
    public Mono<Void> storageOfflineMessage(Flux<OfflineMessage> message) {
        return message.doOnNext(s->messages.put(s.getUserId(),s.getMessage())).then();
    }

    @Override
    public Flux<TransportMessage> getToMessages(String to, ClientType clientType) {
        return Flux.just(messages.get(to).toArray(new TransportMessage[messages.get(to).size()]));
    }
}
