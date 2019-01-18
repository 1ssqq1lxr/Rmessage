package io.reactor.netty.flow.test.tcp;

import io.rector.netty.core.init.ServerStart;
import io.rector.netty.transport.distribute.DefaultOfflineMessageDistribute;
import org.junit.Test;
import reactor.ipc.netty.tcp.TcpServer;

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
                .interceptor(frame -> frame,frame -> frame)
                .setAfterChannelInit(channel -> {
                    //  channel设置
                })
                .<TcpServer>connect().subscribe(session->{
            session.addGroupHandler(groupId -> null).subscribe();
            session.addOfflineHandler(new DefaultOfflineMessageDistribute()).subscribe();
        });
        countDownLatch.await();
    }


}
