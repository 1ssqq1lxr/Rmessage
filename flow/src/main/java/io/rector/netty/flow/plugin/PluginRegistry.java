package io.rector.netty.flow.plugin;


import io.netty.util.ReferenceCountUtil;
import io.reactor.netty.api.codec.TransportMessage;
import io.reactor.netty.api.frame.Frame;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.stream.Collectors;

/**
 * @Auther: lxr
 * @Date: 2018/12/11 15:05
 * @Description:
 */
public class PluginRegistry {

    private LinkedList<FrameInterceptor> serverInterceptors = new LinkedList<>();

    private LinkedList<FrameInterceptor> clientInterceptors = new LinkedList<>();



    public void addClientPlugin(FrameInterceptor interceptor) {
        clientInterceptors.add(interceptor);
    }

    public void addServerPlugin(FrameInterceptor interceptor) {
        serverInterceptors.add(interceptor);
    }

    public PluginRegistry addClientPlugin(FrameInterceptor... interceptor) {
        clientInterceptors.addAll(Arrays.stream(interceptor).collect(Collectors.toSet()));
        return  this;
    }

    public PluginRegistry addServerPlugin(FrameInterceptor... interceptor) {
        serverInterceptors.addAll(Arrays.stream(interceptor).collect(Collectors.toSet()));
        return  this;
    }


    public TransportMessage applyClient(TransportMessage message) {
        return doResult(message,clientInterceptors);
    }

    public TransportMessage applyServer(TransportMessage message) {
        return doResult(message,serverInterceptors);
    }

    private TransportMessage doResult(TransportMessage message, LinkedList<FrameInterceptor>  interceptors){
            for (FrameInterceptor i : interceptors) {
                message = i.apply(message);
            }
            return message;

    }

}
