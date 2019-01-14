package io.rector.netty.transport.codec;


import io.reactor.netty.api.codec.TransportMessage;
import reactor.core.publisher.Mono;
import reactor.core.publisher.UnicastProcessor;

/**
 * @Auther: lxr
 * @Date: 2018/12/26 17:13
 * @Description:
 */
public interface DecoderAcceptor {

     Mono<Void> transportMessage( TransportMessage message);


}
