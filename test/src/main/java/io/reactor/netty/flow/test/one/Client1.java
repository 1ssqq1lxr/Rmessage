package io.reactor.netty.flow.test.one;

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

public class Client1 {


    @Test
    public void  client() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        ClientStart
                .builder()
                .tcp()
                .ip("127.0.0.1")
                .port(1888)
                .userId("21344")  //设置用户名
                .password("12312") //设置密码
                .onReadIdle(10000l,()->()->System.out.println("心跳了"))//设置读心跳,以及设置回调runner
                .setClientType(ClientType.Ios)//设置客户端类型
                .setAfterChannelInit(channel -> {
                    //  channel设置
                })
                .connect()
                .cast(TcpClientSession.class)
                .subscribe(session->{
                    session.sendPoint("123","测试一下哦").subscribe(); //发送单聊消息
                    session.sendGroup("group1","123").subscribe();  // 发送群聊消息
                    session.accept(message -> {
                    }); // 接受所有消息
        });
        countDownLatch.await();
    }


}
