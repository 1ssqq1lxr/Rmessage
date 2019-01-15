package io.reactor.netty.flow.test;

import org.junit.Test;
import reactor.core.Disposable;
import reactor.core.publisher.UnicastProcessor;
import reactor.util.concurrent.Queues;

import java.util.concurrent.CountDownLatch;

public class ReactorTest {

    /**
     * UnicastProcessor
     */

    @Test
    public void testUnicastProcessor() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(4);
        UnicastProcessor<Object> objects = UnicastProcessor.create();
         new Thread(() -> {
             objects.onNext(new Object()) ;
             countDownLatch.countDown();
         }).start();
        new Thread(() -> {
            objects.onNext(new Object()) ;
            countDownLatch.countDown();
        }).start();

        new Thread(() -> {
            objects.onNext(new Object()); ;
            countDownLatch.countDown();
        }).start();

        Disposable disposable= objects.subscribe(obj->{
            System.out.println(Thread.currentThread().getId()+":"+Thread.currentThread().getName()+obj);
        });
        Thread.sleep(2000);
        disposable.dispose();
        objects.onNext(new Object()); ;
        objects.onNext(new Object()); ;
        objects.onNext(new Object()); ;

        countDownLatch.await();

    }


    @Test
    public void testUnicastProcessorCallback() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(4);
        UnicastProcessor<Object> objects = UnicastProcessor.create(Queues.one().get(),o -> {
            System.out.println(o+"被丢弃了");
        }, () -> {
            System.out.println("关闭了");
        });
        new Thread(() -> {
            for(;;){
                objects.onNext(new Object()) ;
            }

        }).start();
        new Thread(() -> {
            objects.onNext(new Object()) ;
            countDownLatch.countDown();
        }).start();

        new Thread(() -> {
            objects.onNext(new Object()); ;
            countDownLatch.countDown();
        }).start();

        new Thread(() -> {
            objects.onNext(new Object()); ;
            countDownLatch.countDown();
        }).start();

        Disposable disposable= objects.subscribe(obj->{
            System.out.println(Thread.currentThread().getId()+":"+Thread.currentThread().getName()+obj);
        });
        Thread.sleep(2000);
        disposable.dispose();
        objects.onNext(new Object()); ;
        objects.onNext(new Object()); ;
        objects.onNext(new Object()); ;

        countDownLatch.await();

    }



}
