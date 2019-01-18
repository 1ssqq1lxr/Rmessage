package io.rector.netty.transport.method;

import io.netty.channel.Channel;
import io.reactor.netty.api.Idle;
import io.rector.netty.config.ClientConfig;
import io.rector.netty.config.Config;
import io.rector.netty.config.ServerConfig;
import io.rector.netty.transport.distribute.OfflineMessageDistribute;
import lombok.Builder;
import lombok.Data;
import reactor.ipc.netty.NettyContext;
import reactor.ipc.netty.options.ClientOptions;
import reactor.ipc.netty.options.ServerOptions;

import java.util.function.Consumer;

/**
 * @Auther: lxr
 * @Date: 2019/1/14 16:12
 * @Description:
 */
@Data
@Builder
public class ReactorMethodExtend implements MethodExtend {

    private  Config config;

    private  Idle readIdle;

    private  Idle writeIdle;




    private Consumer<? super Channel> afterChannelInit;

    private Consumer<ServerOptions.Builder<?>> serverOptions;

    private Consumer<ClientOptions.Builder<?>> clientOptions;

    private Consumer<? super NettyContext> afterNettyContextInit;


    public Consumer<? extends ServerOptions.Builder<?>> getServerOptions() {
        ServerConfig serverConfig =(ServerConfig)config;
        this.serverOptions= builder ->builder.host(serverConfig.getIp()).port(serverConfig.port).afterChannelInit(afterChannelInit).afterNettyContextInit(afterNettyContextInit);
        return this.serverOptions;
    }

    @Override
    public Consumer<? extends ClientOptions.Builder<?>> getClientOptions() {
        ClientConfig clientConfig =(ClientConfig)config;
        this.clientOptions= builder ->builder.disablePool().host(clientConfig.getIp()).port(clientConfig.port).afterChannelInit(afterChannelInit).afterNettyContextInit(afterNettyContextInit);
        return clientOptions;
    }

    @Override
    public void setAfterNettyContextInit(Consumer<? super NettyContext> afterNettyContextInit) {
        this.afterNettyContextInit=afterNettyContextInit;
    }


}
