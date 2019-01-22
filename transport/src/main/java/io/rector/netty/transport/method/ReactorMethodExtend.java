package io.rector.netty.transport.method;

import io.netty.channel.Channel;
import io.reactor.netty.api.Idle;
import io.rector.netty.config.ClientConfig;
import io.rector.netty.config.Config;
import io.rector.netty.config.ServerConfig;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import reactor.ipc.netty.NettyContext;
import reactor.ipc.netty.options.ClientOptions;
import reactor.ipc.netty.options.ServerOptions;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * @Auther: lxr
 * @Date: 2019/1/14 16:12
 * @Description:
 */
@Getter
@Setter
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
        this.serverOptions= builder ->{
            ServerOptions.Builder<?> builder1 =builder.host(serverConfig.getIp()).port(serverConfig.port).afterNettyContextInit(afterNettyContextInit);
            Optional.ofNullable(afterChannelInit)
                    .ifPresent(init->builder1   .afterChannelInit(afterChannelInit));
        };
        return this.serverOptions;
    }

    @Override
    public Consumer<? extends ClientOptions.Builder<?>> getClientOptions() {
        ClientConfig clientConfig =(ClientConfig)config;
        this.clientOptions= builder ->{
            ClientOptions.Builder<?> builder1 =builder.disablePool().host(clientConfig.getIp()).port(clientConfig.port).afterNettyContextInit(afterNettyContextInit);
            Optional.ofNullable(afterChannelInit)
                    .ifPresent(init->builder1   .afterChannelInit(afterChannelInit));
        };
        return clientOptions;
    }

    @Override
    public void setAfterNettyContextInit(Consumer<? super NettyContext> afterNettyContextInit) {
        this.afterNettyContextInit=afterNettyContextInit;
    }


}
