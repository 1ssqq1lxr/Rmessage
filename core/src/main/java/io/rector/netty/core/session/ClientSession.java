package io.rector.netty.core.session;

import io.reactor.netty.api.codec.TransportMessage;
import io.rector.netty.transport.listener.MessageListener;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;

/**
 * @Auther: luxurong
 * @Date: 2019/1/18 16:20
 * @Description: 客户端session操作类
 **/
public interface ClientSession  extends Disposable {

    // 发送单聊消息
    Mono<Void>  sendPoint(String userId,String body);

    // 发送群组消息
    Mono<Void>  sendGroup(String group,String body);

    // 自定义构建消息
    Mono<Void>  sendMessage(TransportMessage message);

    // 接受消息
    Disposable  accept(MessageListener messageListener);





}
