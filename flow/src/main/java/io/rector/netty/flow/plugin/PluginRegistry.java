package io.rector.netty.flow.plugin;


import io.netty.util.ReferenceCountUtil;
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


    public Frame applyClient(Frame frame) {
        return doResult(frame,clientInterceptors);
    }

    public Frame applyServer(Frame frame) {
        return doResult(frame,serverInterceptors);
    }

    private  Frame doResult(Frame rSocket,LinkedList<FrameInterceptor>  interceptors){
        try {
            for (FrameInterceptor i : interceptors) {
                rSocket = i.apply(rSocket);
            }
            return rSocket;
        }
        finally {
            if(rSocket.refCnt()>0){
                ReferenceCountUtil.release(rSocket);
            }
        }
    }

}
