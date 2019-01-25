package io.reactor.netty.api;

import lombok.extern.slf4j.Slf4j;
import reactor.ipc.netty.NettyConnector;
import reactor.ipc.netty.NettyInbound;
import reactor.ipc.netty.NettyOutbound;
import reactor.ipc.netty.tcp.TcpServer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Consumer;

/**
 * @Auther: lxr
 * @Date: 2018/12/13 14:21
 * @Description:
 */
@Slf4j
public class ReflectUtil {


    public static  <T  extends NettyConnector< ? extends NettyInbound,? extends NettyOutbound>>   T  staticMethod(Class<T> tClass, Object obj){
        try {
            Method newInstanceMethod = tClass.getDeclaredMethod("create", Consumer.class);
            return (T)newInstanceMethod.invoke(null,new Object[]{obj});

        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
             log.error("newInstance{}:{}",tClass,e);
        }
        return null;
    }

}
