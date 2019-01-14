package io.rector.netty.transport.codec;

import io.reactor.netty.api.codec.TransportMessage;
import io.rector.netty.transport.distribute.DirectServerMessageDistribute;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoProcessor;
import reactor.core.publisher.UnicastProcessor;


/**
 * @Auther: lxr
 * @Date: 2018/12/26 17:08
 * @Description:
 */
@Slf4j
public class ServerDecoderAcceptor implements DecoderAcceptor{



    private DirectServerMessageDistribute distribute;

    private UnicastProcessor<TransportMessage> offlineMessagePipeline;

    public ServerDecoderAcceptor(UnicastProcessor<TransportMessage> offlineMessagePipeline,DirectServerMessageDistribute distribute) {
        this.distribute = distribute;
        this.offlineMessagePipeline=offlineMessagePipeline;
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
                    case ADDUSER:
                    case DELUSER:
                    case LEAVE:
                    case GROUP:
                        distribute.sendGroup(message.getTo(),message.toBytes());
                        break;
                    case PING:

                    case JOIN:
                    case ONE:
                        if(!distribute.sendOne(message.getTo(),message.toBytes())){ //发送失败
                            offlineMessagePipeline.onNext(message);
                        }
                        break;
                    case ACCEPT:
                }

            }
            monoSink.success();
        });
    }



}
