package io.reactor.netty.flow.test.one;

import io.reactor.netty.api.codec.ClientType;
import io.reactor.netty.api.codec.TransportMessage;
import io.rector.netty.core.init.ClientStart;
import io.rector.netty.core.session.TcpClientSession;
import io.rector.netty.transport.listener.MessageListener;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

/**
 * @Auther: luxurong
 * @Date: 2019/1/18 13:52
 * @Description:
 **/

public class Client2 {


    @Test
    public void  client() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        ClientStart
                .builder()
                .tcp()
                .ip("127.0.0.1")
                .port(1888)
                .userId("123")
                .setClientType(ClientType.Ios)
//                .interceptor(frame -> frame,frame -> frame)
                .setAfterChannelInit(channel -> {
                    //  channel设置
                })
                .connect()
                .cast(TcpClientSession.class)
                .subscribe(session->{
                    session.accept(message -> {
                        System.out.println(" client 2接收消息 "+message);
                    });
        });
        countDownLatch.await();
    }


}
