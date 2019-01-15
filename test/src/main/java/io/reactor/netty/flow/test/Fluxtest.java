package io.reactor.netty.flow.test;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.UnicastProcessor;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;

public class Fluxtest {

    @Test
    public void testFlux() throws InterruptedException {


        CountDownLatch countDownLatch = new CountDownLatch(4);
        UnicastProcessor<Object> objects = UnicastProcessor.create();
    ;
//        new Thread(() -> {

//            for(;;){
//                try {
//                    Thread.sleep(1000l);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                objects.onNext(new Object()) ;
//            }
//
//        }).start();

        Flux flux= Flux.interval(Duration.ofSeconds(1)).takeUntil(lon->lon==3);
//        Flux.combineLatest()
        objects.buffer(flux);
        objects.subscribe(System.out::println);
        countDownLatch.await();

    }


    @Test
    public void testFluxDoOnNext() throws InterruptedException {


        CountDownLatch countDownLatch = new CountDownLatch(4);
        Flux<Long> flux= Flux.interval(Duration.ofSeconds(1));
        flux.doOnNext(lo->{System.out.println("on next{}"+lo);})
                .checkpoint()
                .subscribe(lo->System.out.println("sub:{}"+lo));
        countDownLatch.await();

    }

}
