package io.rector.netty.transport.codec;

import io.reactor.netty.api.codec.TransportMessage;
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



    private DirectServerMessageDistribute distribute;

    private UnicastProcessor<TransportMessage> offlineMessagePipeline;

    private Disposable disposable;

    public ServerDecoderAcceptor(UnicastProcessor<TransportMessage> offlineMessagePipeline,DirectServerMessageDistribute distribute,Disposable disposable) {
        this.distribute = distribute;
        this.offlineMessagePipeline=offlineMessagePipeline;
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
                        if(!disposable.isDisposed()){
                            disposable.dispose(); //取消关闭连接
                        }
                        break;
                    case ONE: // 单发
                        if(!distribute.sendOne(message.getTo(),message.toBytes())){ //发送失败
                            offlineMessagePipeline.onNext(message);
                        }
                        break;

                    case GROUP:  //群发
                        distribute.sendGroup(message.getTo(),message.toBytes());
                        break;
                    case PING:  //回复pong

                    case LEAVE: // 离开群组


                    case JOIN:  // 加入群组

                    case ACCEPT:
                }

            }
            monoSink.success();
        });
    }



}
