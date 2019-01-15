package io.reactor.netty.flow.test;

import org.junit.Test;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;

/**
 * @Auther: lxr
 * @Date: 2019/1/14 20:12
 * @Description:
 */
public class TestMono {

    @Test
    public void test() throws InterruptedException {
        Flux flux=  Flux.interval(Duration.ofSeconds(2));
        flux.doOnNext(System.out::println).then().subscribe();


        CountDownLatch countDownLatch = new CountDownLatch(1);
        Disposable disposable=Mono.defer(()->{
            System.out.println("关闭了");
            return Mono.empty();
        }).delaySubscription(Duration.ofSeconds(5))
                .subscribe();
        Thread.sleep(3000);
        disposable.dispose();
        System.out.println("哈哈了");
        countDownLatch.await();
    }



    @Test
    public void test2() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Flux flux=  Flux.interval(Duration.ofSeconds(2));
        flux.doOnNext(System.out::println).then().subscribe();
        countDownLatch.await();
    }

}
