package io.reactor.netty.flow.test.tcp;

import io.reactor.netty.api.codec.ClientType;
import io.rector.netty.core.init.ClientStart;
import io.rector.netty.core.session.TcpClientSession;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

/**
 * @Auther: luxurong
 * @Date: 2019/1/18 13:52
 * @Description:
 **/

public class ClientTest {


    @Test
    public void  client() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        ClientStart
                .builder()
                .tcp()
                .ip("127.0.0.1")
                .port(1888)
                .userId("213")
                .onReadIdle(10000l,()->()->System.out.println("心跳了"))
                .setClientType(ClientType.Ios)
//                .interceptor(frame -> frame,frame -> frame)
                .setAfterChannelInit(channel -> {
                    //  channel设置
                })
                .connect()
                .cast(TcpClientSession.class)
                .subscribe(session->{
        });
        countDownLatch.await();
    }


}
