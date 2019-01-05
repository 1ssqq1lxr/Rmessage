package io.rector.netty.transport.codec;

import io.reactor.netty.api.codec.TransportMessage;
import io.rector.netty.transport.distribute.ServerMessageDistribute;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.UnicastProcessor;


/**
 * @Auther: lxr
 * @Date: 2018/12/26 17:08
 * @Description:
 */
@Slf4j
public class ServerDecoderAcceptor implements DecoderAcceptor{

    private ServerMessageDistribute distribute;

    private TransportMessage  message;

    public ServerDecoderAcceptor(ServerMessageDistribute distribute, TransportMessage message) {
        this.distribute = distribute;
        this.message = message;
    }

   private UnicastProcessor<TransportMessage> messages = UnicastProcessor.create();


    @Override
    public void transportMessage() { // 分发消息
        if(message.isDiscard()){
            log.info("message is discard {}",message);
        }
        else {
            switch (message.getType()){
//                case CONFIRM:
                case LEAVE:
                case GROUP:
                    distribute.sendGroup(message.getTo(),message.toBytes());
                    break;
                case PING:
                case JOIN:
                case ONE:
                    if(!distribute.sendOne(message.getTo(),message.toBytes())){ //发送失败
                        messages.onNext(message);
                    }
                    break;
                case ACCEPT:
            }

        }
    }



}
