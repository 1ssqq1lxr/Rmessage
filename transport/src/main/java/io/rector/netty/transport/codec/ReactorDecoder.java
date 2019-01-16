package io.rector.netty.transport.codec;

import io.reactor.netty.api.codec.TransportMessage;
import io.rector.netty.transport.distribute.ConnectionStateDistribute;
import io.rector.netty.transport.distribute.DirectServerMessageDistribute;
import reactor.core.Disposable;
import reactor.core.publisher.UnicastProcessor;

/**
 * @Auther: lxr
 * @Date: 2018/12/26 17:11
 * @Description:
 */
public interface ReactorDecoder {
    DecoderAcceptor decode(UnicastProcessor<TransportMessage> offlineMessagePipeline, DirectServerMessageDistribute distribute, ConnectionStateDistribute connectionStateDistribute, Disposable disposable);
}
