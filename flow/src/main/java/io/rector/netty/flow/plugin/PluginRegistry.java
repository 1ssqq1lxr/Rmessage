package io.rector.netty.flow.plugin;


import io.rector.netty.flow.frame.Frame;

import java.util.LinkedList;

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


    public Frame applyClient(Frame frame) {
        for (FrameInterceptor i : clientInterceptors) {
            frame = i.apply(frame);
        }
        return frame;
    }

    public Frame applyServer(Frame rSocket) {
        for (FrameInterceptor i : serverInterceptors) {
            rSocket = i.apply(rSocket);
        }
        return rSocket;
    }

}
