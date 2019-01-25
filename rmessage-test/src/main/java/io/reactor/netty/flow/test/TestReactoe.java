package io.reactor.netty.flow.test;

import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.ChannelOption;
import lombok.Data;
import org.junit.Test;
import org.reactivestreams.Publisher;
import reactor.ipc.netty.NettyInbound;
import reactor.ipc.netty.NettyOutbound;
import reactor.ipc.netty.options.ClientOptions;
import reactor.ipc.netty.options.ServerOptions;
import reactor.ipc.netty.tcp.TcpClient;
import reactor.ipc.netty.tcp.TcpServer;

import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Consumer;

/**
 * @Auther: luxurong
 * @Date: 2019/1/23 20:50
 * @Description:
 **/
public class TestReactoe {


    @Test
    public void testReactorNetty(){
        Consumer<? super ServerOptions.Builder<?>> opsHandler= ops-> ops.host("localhost").port(10008).option(ChannelOption.ALLOCATOR, UnpooledByteBufAllocator.DEFAULT);
        BiFunction<? super NettyInbound, ? super NettyOutbound, ? extends Publisher<Void>> newHandler=
                (in,out)->{
                    in.receiveObject().map(obj->"server测试结果=========="+obj).subscribe(System.out::println);
//                    out.onWriteIdle(5000,()->{
//                        out.context().channel().close();
//                    });
                    return out.neverComplete();
                };
        TcpServer.create(opsHandler)
                .newHandler(newHandler)
                .block()
                .onClose()
                .block();
    }

}
