package io.rector.netty.transport.method;

import io.netty.channel.Channel;
import io.reactor.netty.api.Idle;
import io.rector.netty.config.Config;
import reactor.ipc.netty.NettyContext;
import reactor.ipc.netty.options.ClientOptions;
import reactor.ipc.netty.options.ServerOptions;

import java.util.function.Consumer;

public interface MethodExtend {

    void setReadIdle(Idle readIdle);

    void setWriteIdle(Idle writeIdle);

    Idle getReadIdle();

    Idle getWriteIdle();

    void setAfterChannelInit(Consumer<? super Channel> afterChannelInit);

//    void setOptions(Consumer<ServerOptions.Builder<?>> options);

    Consumer<? extends ServerOptions.Builder<?>> getServerOptions();


    Consumer<? extends ClientOptions.Builder<?>> getClientOptions();

    void setAfterNettyContextInit(Consumer<? super NettyContext> afterNettyContextInit);

    void setConfig(Config config);
}
