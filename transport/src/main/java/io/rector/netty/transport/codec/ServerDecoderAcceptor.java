package io.rector.netty.transport.codec;

import io.reactor.netty.api.codec.MessageBody;
import io.reactor.netty.api.codec.OnlineMessage;
import io.reactor.netty.api.codec.TransportMessage;
import io.rector.netty.transport.distribute.ConnectionStateDistribute;
import io.rector.netty.transport.distribute.DirectServerMessageDistribute;
import lombok.extern.slf4j.Slf4j;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;
import reactor.core.publisher.UnicastProcessor;


/**
 * @Auther: lxr
 * @Date: 2018/12/26 17:08
 * @Description:
 */
@Slf4j
public class ServerDecoderAcceptor implements DecoderAcceptor{



    private DirectServerMessageDistribute directServerMessageDistribute;

    private UnicastProcessor<TransportMessage> offlineMessagePipeline;

    private Disposable disposable;

    private ConnectionStateDistribute connectionStateDistribute;

    public ServerDecoderAcceptor(UnicastProcessor<TransportMessage> offlineMessagePipeline, DirectServerMessageDistribute directServerMessageDistribute, ConnectionStateDistribute connectionStateDistribute,Disposable disposable) {
        this.directServerMessageDistribute = directServerMessageDistribute;
        this.offlineMessagePipeline=offlineMessagePipeline;
        this.connectionStateDistribute=connectionStateDistribute;
        this.disposable=disposable;
    }


    @Override
    public Mono<Void> transportMessage(TransportMessage message) { // 分发消息
        return Mono.create(monoSink -> {
            if(message.isDiscard()){
                log.info("message is discard {}",message);
            }
            else {
                switch (message.getType()){
                    case ONLINE:
                        connectionStateDistribute.init(message)
                                .then(Mono.fromRunnable(()->{
                                    if(!disposable.isDisposed()){
                                        disposable.dispose(); //取消关闭连接
                                    }
                                })).subscribe();
                        break;
                    case ONE: // 单发
                        Mono<Void>  offline=Mono.create(sink ->{
                            offlineMessagePipeline.onNext(message);
                            sink.success();
                        });
                        directServerMessageDistribute.sendOne(message,offline).subscribe();
                        break;
                    case GROUP:  //群发
                        MessageBody groupBody = (MessageBody)message.getMessageBody();
                        directServerMessageDistribute.sendGroup(groupBody)
                                .doOnError(throwable -> log.error("【ServerDecoderAcceptor：transportMessage】 {}",throwable))
                                .subscribe();
                        break;
                    case PING:  //回复pong


                    case ACCEPT:

                }

            }
            monoSink.success();
        });
    }



}
