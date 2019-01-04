package io.rector.netty.flow.plugin;

import io.reactor.netty.api.codec.TransportMessage;

import java.util.function.Function;

/**
 * @Auther: lxr
 * @Date: 2018/12/11 15:10
 * @Description:
 */
public @FunctionalInterface interface FrameInterceptor extends Function<TransportMessage, TransportMessage> {
}
