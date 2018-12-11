package io.rector.netty.flow.plugin;


import io.rector.netty.flow.frame.Frame;

/**
 * @Auther: lxr
 * @Date: 2018/12/11 14:41
 * @Description:
 */
public class Plugins {


    private static PluginRegistry DEFAULT = new PluginRegistry();

    private Plugins() {}

    public static void interceptClient(FrameInterceptor interceptor) {
        DEFAULT.addClientPlugin(interceptor);
    }

    public static void interceptServer(FrameInterceptor interceptor) {
        DEFAULT.addServerPlugin(interceptor);
    }

    public static PluginRegistry defaultPlugins() {
        return DEFAULT;
    }





}
