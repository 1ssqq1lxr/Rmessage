package io.rector.netty.transport.codec;

import io.reactor.netty.api.codec.OfflineMessage;
import io.rector.netty.transport.distribute.ConnectionStateDistribute;
import io.rector.netty.transport.distribute.DirectServerMessageHandler;
import reactor.core.Disposable;
import reactor.core.publisher.UnicastProcessor;

import java.util.function.Consumer;

/**
 * @Auther: lxr
 * @Date: 2018/12/26 17:11
 * @Description:
 */
public interface ReactorDecoder {
    DecoderAcceptor decode(UnicastProcessor<OfflineMessage> offlineMessagePipeline, DirectServerMessageHandler distribute, ConnectionStateDistribute connectionStateDistribute, Disposable disposable, Consumer<String> consumer);
}
