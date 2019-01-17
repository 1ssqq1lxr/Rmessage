package io.rector.netty.transport.codec;

import io.reactor.netty.api.codec.MessageBody;
import io.reactor.netty.api.codec.OfflineMessage;
import io.reactor.netty.api.codec.OnlineMessage;
import io.reactor.netty.api.codec.TransportMessage;
import io.rector.netty.transport.distribute.ConnectionStateDistribute;
import io.rector.netty.transport.distribute.DirectServerMessageDistribute;
import lombok.extern.slf4j.Slf4j;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;
import reactor.core.publisher.UnicastProcessor;

import java.util.function.Consumer;
import java.util.function.Function;


/**
 * @Auther: lxr
 * @Date: 2018/12/26 17:08
 * @Description:
 */
@Slf4j
public class ServerDecoderAcceptor implements DecoderAcceptor{



    private DirectServerMessageDistribute directServerMessageDistribute;

    private UnicastProcessor<OfflineMessage> offlineMessagePipeline;

    private Disposable disposable;

    private ConnectionStateDistribute connectionStateDistribute;

    public ServerDecoderAcceptor(UnicastProcessor<OfflineMessage> offlineMessagePipeline, DirectServerMessageDistribute directServerMessageDistribute, ConnectionStateDistribute connectionStateDistribute,Disposable disposable) {
        this.directServerMessageDistribute = directServerMessageDistribute;
        this.offlineMessagePipeline=offlineMessagePipeline;
        this.connectionStateDistribute=connectionStateDistribute;
        this.disposable=disposable;
    }


    @Override
    public Mono<Void> transportMessage(TransportMessage message) { // 分发消息
        return Mono.defer(()->{
            if(message.isDiscard()){
                log.info("message is discard {}",message);
                return Mono.empty();
            }
            else {
                switch (message.getType()){
                    case ONLINE:
                        return connectionStateDistribute.init(message)
                                .then(Mono.fromRunnable(()->{
                                    if(!disposable.isDisposed()){
                                        disposable.dispose(); //取消关闭连接
                                    }
                                }));
                    case ONE: // 单发
                        Mono<Void> offline= buildOffline(message, ((MessageBody)message.getMessageBody()).getTo());
                        return directServerMessageDistribute.sendOne(message,offline);
                    case GROUP:  //群发
                        Function<String,Mono<Void>> consumer = uid->buildOffline(message,uid);
                        return directServerMessageDistribute.sendGroup(message,consumer)
                                .doOnError(throwable -> log.error("【ServerDecoderAcceptor：transportMessage】 {}",throwable));
                    case PING:  //回复pong
                        return directServerMessageDistribute.sendPong(message);
                    case ACCEPT:
                        return null;
                    default:
                        return Mono.empty();
                }

            }
        });
    }


    private Mono<Void>  buildOffline(TransportMessage message,String userId){
        return  Mono.fromRunnable(() ->
                offlineMessagePipeline.onNext(
                        OfflineMessage.builder()
                                .userId(userId)
                                .message(message)
                                .build()));
    }


}
