package io.rector.netty.transport.method;

import io.netty.channel.Channel;
import io.reactor.netty.api.Idle;
import io.rector.netty.config.Config;
import io.rector.netty.config.ServerConfig;
import io.rector.netty.transport.distribute.OfflineMessageDistribute;
import lombok.Builder;
import lombok.Data;
import reactor.ipc.netty.NettyContext;
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


    private  OfflineMessageDistribute offlineMessageDistribute;

    private Consumer<? super Channel> afterChannelInit;

    private Consumer<ServerOptions.Builder<?>> options;

    private Consumer<? super NettyContext> afterNettyContextInit;


    public Consumer<? extends ServerOptions.Builder<?>> getOptions() {
        ServerConfig serverConfig =(ServerConfig)config;
        this.options= builder ->builder.host(serverConfig.getIp()).port(serverConfig.port).afterChannelInit(afterChannelInit).afterNettyContextInit(afterNettyContextInit);
        return this.options;
    }

    @Override
    public void setAfterNettyContextInit(Consumer<? super NettyContext> afterNettyContextInit) {
        this.afterNettyContextInit=afterNettyContextInit;
    }


}
