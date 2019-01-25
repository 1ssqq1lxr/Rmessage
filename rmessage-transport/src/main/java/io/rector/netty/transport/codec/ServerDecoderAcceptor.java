package io.rector.netty.transport.codec;

import io.reactor.netty.api.codec.*;
import io.reactor.netty.api.exception.NotSupportException;
import io.rector.netty.transport.connection.ConnectionManager;
import io.rector.netty.transport.distribute.ConnectionStateDistribute;
import io.rector.netty.transport.distribute.DirectServerMessageHandler;
import io.rector.netty.transport.distribute.UserTransportHandler;
import lombok.extern.slf4j.Slf4j;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;
import reactor.core.publisher.UnicastProcessor;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;


/**
 * @Auther: lxr
 * @Date: 2018/12/26 17:08
 * @Description:
 */
@Slf4j
public class ServerDecoderAcceptor implements DecoderAcceptor{



    private DirectServerMessageHandler directServerMessageHandler;

    private UnicastProcessor<OfflineMessage> offlineMessagePipeline;

    private Disposable disposable;

    private ConnectionStateDistribute connectionStateDistribute;

    private ConnectionManager connectionManager;

    private UserTransportHandler userHandler;

    private AtomicBoolean atomicBoolean;

    public ServerDecoderAcceptor(UnicastProcessor<OfflineMessage> offlineMessagePipeline,
                                 DirectServerMessageHandler directServerMessageHandler,
                                 ConnectionStateDistribute connectionStateDistribute,
                                 Disposable disposable,
                                 ConnectionManager connectionManager,
                                 UserTransportHandler userHandler,
                                 AtomicBoolean atomicBoolean) {
        this.directServerMessageHandler = directServerMessageHandler;
        this.offlineMessagePipeline=offlineMessagePipeline;
        this.connectionStateDistribute=connectionStateDistribute;
        this.disposable=disposable;
        this.connectionManager=connectionManager;
        this.userHandler=userHandler;
        this.atomicBoolean =atomicBoolean;
    }


    @Override
    public  void transportMessage(TransportMessage message) { // 分发消息
        log.info("accept message {}",message);
        if(message.isDiscard()){
            log.info("message is discard {}",message);
        }
        else {
            switch (message.getType()){
                case OFFLINE:
                    throw new NotSupportException("type OFFLINE message not support");
                case ONLINE:
                    ConnectionState connectionState= (ConnectionState)message.getMessageBody();
                    if(!userHandler.isAllowLogin(connectionState.getUserId(),connectionState.getPassword())) return;
                    Mono.fromRunnable(()->{
                        if(!disposable.isDisposed()){ disposable.dispose(); }//取消关闭连接
                    })
                            .then(connectionStateDistribute.init(message,connectionManager,atomicBoolean))
                            .doOnError(throwable -> log.error("【ServerDecoderAcceptor：transportMessage ONLINE】 {}",throwable))
                            .subscribe();
                    break;
                case ONE: // 单发
                    MessageBody one=(MessageBody)message.getMessageBody();
                    if(!userHandler.checkIsFriend(one.getFrom(),one.getTo())) return;
                    Mono<Void> offline= buildOffline(message, ((MessageBody)message.getMessageBody()).getTo());
                    directServerMessageHandler.sendOne(message,offline)
                            .doOnError(throwable -> log.error("【ServerDecoderAcceptor：transportMessage ONE】 {}",throwable))
                            .subscribe();
                    break;
                case GROUP:  //群发
                    MessageBody group=(MessageBody)message.getMessageBody();
                    if(!userHandler.checkIsFriend(group.getFrom(),group.getTo())) return;
                    Function<String,Mono<Void>> consumer = uid->buildOffline(message,uid);
                    directServerMessageHandler.sendGroup(message,consumer)
                            .doOnError(throwable -> log.error("【ServerDecoderAcceptor：transportMessage GROUP】 {}",throwable))
                            .subscribe();
                    break;
                case PING:  //回复pong
                    directServerMessageHandler.sendPong(
                            TransportMessage
                                    .builder()
                                    .connection(message.getConnection())
                                    .clientType(message.getClientType())
                                    .type(ProtocolCatagory.PONG)
                                    .build())
                            .subscribe();
                    break;
                case PONG:
                    throw new NotSupportException("type PONG message not support");
                case ONEACK:
                    //暂时未实现
                case GROUPACK:
                    //暂时未实现
                default:
                    break;
            }

        }
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
