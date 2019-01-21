package io.reactor.netty.flow.test.tcp;

import io.rector.netty.core.init.ServerStart;
import io.rector.netty.core.session.TcpServerSession;
import io.rector.netty.transport.distribute.def.DefaultOffMessageHandler;
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
                .interceptor(frame -> frame,frame -> frame)
                .setAfterChannelInit(channel -> {//  channel设置
                })
                .connect()
                .cast(TcpServerSession.class)
                .subscribe(session->{
                    session.addGroupHandler(groupId -> null).subscribe();
                    session.addOfflineHandler(new DefaultOffMessageHandler()).subscribe();
        });
        countDownLatch.await();
    }


}
