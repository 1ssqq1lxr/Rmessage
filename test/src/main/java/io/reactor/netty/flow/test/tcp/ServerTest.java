package io.reactor.netty.flow.test.tcp;

import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.ChannelOption;
import io.rector.netty.core.init.ServerStart;
import io.rector.netty.core.session.TcpServerSession;
import io.rector.netty.transport.distribute.def.DefaultOffMessageHandler;
import io.rector.netty.transport.distribute.def.DefaultUserTransportHandler;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

/**
 * @Auther: luxurong
 * @Date: 2019/1/18 13:52
 * @Description:
 **/

public class ServerTest {


    @Test
    public void  server() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        ServerStart
                .builder()
                .tcp()
                .ip("127.0.0.1")
                .port(1888)
                .onReadIdle(10000l) //设置读心跳时间
                .onWriteIdle(10000l) //设置写心跳时间
                .option(ChannelOption.ALLOCATOR, UnpooledByteBufAllocator.DEFAULT)
                .option(ChannelOption.SO_REUSEADDR,true)
                .option(ChannelOption.SO_RCVBUF,1023)
                .interceptor(frame -> frame,frame -> frame)// 拦截所有message
                .setAfterChannelInit(channel -> {//  channel设置
                })
                .connect()
                .cast(TcpServerSession.class)
                .subscribe(session->{
                    session.addGroupHandler(groupId -> null).subscribe();
                    session.addOfflineHandler(new DefaultOffMessageHandler()).subscribe();
                    session.addUserHandler(new DefaultUserTransportHandler());
                });
        countDownLatch.await();
    }


}
