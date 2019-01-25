package io.rector.netty.transport.codec;

import io.reactor.netty.api.codec.OfflineMessage;
import io.rector.netty.transport.connection.ConnectionManager;
import io.rector.netty.transport.distribute.ConnectionStateDistribute;
import io.rector.netty.transport.distribute.DirectServerMessageHandler;
import io.rector.netty.transport.distribute.UserTransportHandler;
import reactor.core.Disposable;
import reactor.core.publisher.UnicastProcessor;

import java.util.concurrent.atomic.AtomicBoolean;


/**
 * @Auther: lxr
 * @Date: 2018/12/26 17:11
 * @Description:
 */
public interface ReactorDecoder {
    DecoderAcceptor decode(UnicastProcessor<OfflineMessage> offlineMessagePipeline, DirectServerMessageHandler distribute, ConnectionStateDistribute connectionStateDistribute, Disposable disposable, ConnectionManager  connectionManager, UserTransportHandler userHandler, AtomicBoolean atomicBoolean);
}
