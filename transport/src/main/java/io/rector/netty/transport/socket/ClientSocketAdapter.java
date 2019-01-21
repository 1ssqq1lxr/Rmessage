package io.rector.netty.transport.socket;

import io.reactor.netty.api.codec.OnlineMessage;
import io.reactor.netty.api.codec.Protocol;
import io.reactor.netty.api.codec.ProtocolCatagory;
import io.reactor.netty.api.codec.TransportMessage;
import io.rector.netty.config.ClientConfig;
import io.rector.netty.config.Config;
import io.rector.netty.flow.plugin.PluginRegistry;
import io.rector.netty.transport.Transport;
import io.rector.netty.transport.connection.RConnection;
import io.rector.netty.transport.distribute.DirectClientMessageHandler;
import io.rector.netty.transport.method.MethodExtend;
import lombok.Getter;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @Auther: luxurong
 * @Date: 2019/1/18 14:37
 * @Description:
 **/
@Getter
public class ClientSocketAdapter  extends Rsocket {


    private RConnection rConnection;


    private ClientConfig config;

    private MethodExtend methodExtend;

    private byte[] ping ;

    private DirectClientMessageHandler directClientMessageHandler;

    public ClientSocketAdapter(Supplier<Transport> transport, PluginRegistry pluginRegistry, Config config, MethodExtend methodExtend) {
        this.transport = transport;
        this.config=(ClientConfig) config;
        this.methodExtend=methodExtend;
        this.ping= TransportMessage.builder().clientType(this.config.getClientType()).type(ProtocolCatagory.PING).build().getBytes();
    }


    @Override
    public Config getConfig() {
        return this.config;
    }

    @Override
    public Protocol getPrptocol() {
        return this.config.getProtocol();
    }

    @Override
    public Supplier<Consumer<RConnection>> next() {
        return ()->rConnection -> {
            this.rConnection=rConnection;
            directClientMessageHandler = new DirectClientMessageHandler(rConnection);
            Optional.ofNullable(methodExtend.getReadIdle())
                    .ifPresent(read-> rConnection.onReadIdle(read.getTime(), () -> {
                        sendPing();
                        read.getEvent().get().run();
                    }).subscribe());
            Optional.ofNullable(methodExtend.getWriteIdle())
                    .ifPresent(write-> rConnection.onWriteIdle(methodExtend.getWriteIdle().getTime(),()->{
                        sendPing();
                        methodExtend.getWriteIdle().getEvent().get().run();
                    }).subscribe());
            OnlineMessage onlineMessage= OnlineMessage
                    .builder()
                    .userId(config.getUserId())
                    .build();
            directClientMessageHandler.send(TransportMessage.builder()
                    .clientType(config.getClientType())
                    .type(ProtocolCatagory.ONLINE)
                    .messageBody(onlineMessage)
                    .build()).subscribe();
        };
    }

    @Override
    public MethodExtend getMethodExtend() {
        return methodExtend;
    }

    private void sendPing(){
        directClientMessageHandler.sendPing(ping).subscribe();
    }

}
