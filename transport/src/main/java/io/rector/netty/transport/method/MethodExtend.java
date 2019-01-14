package io.rector.netty.transport.method;

import io.netty.channel.Channel;
import io.reactor.netty.api.Idle;
import io.rector.netty.config.Config;
import io.rector.netty.transport.distribute.OfflineMessageDistribute;
import reactor.ipc.netty.NettyContext;
import reactor.ipc.netty.options.ServerOptions;

import java.util.function.Consumer;

public interface MethodExtend {

    void setReadIdle(Idle readIdle);

    void setWriteIdle(Idle writeIdle);

    Idle getReadIdle();

    Idle getWriteIdle();

    void setOfflineMessageDistribute(OfflineMessageDistribute offlineMessageDistribute);

    OfflineMessageDistribute getOfflineMessageDistribute();

    void setAfterChannelInit(Consumer<? super Channel> afterChannelInit);

    void setOptions(Consumer<ServerOptions.Builder<?>> options);

    Consumer<? extends ServerOptions.Builder<?>> getOptions();

    void setAfterNettyContextInit(Consumer<? super NettyContext> afterNettyContextInit);

    void setConfig(Config config);
}
