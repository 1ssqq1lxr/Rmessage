package io.rector.netty.transport.listener;

import io.reactor.netty.api.codec.TransportMessage;

/**
 * @Auther: luxurong
 * @Date: 2019/1/19 04:26
 * @Description:
 **/
public interface MessageListener {

    void accept(TransportMessage message);

}
