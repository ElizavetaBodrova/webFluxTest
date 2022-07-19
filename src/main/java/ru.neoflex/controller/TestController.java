package ru.neoflex.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.ParallelFlux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import ru.neoflex.services.Service1;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class TestController {

    @Autowired
    Service1 srv1;

    /**
     * simple synchronized method
     *
     * @return string
     */
    @GetMapping("/test/1")
    public String test1() {
        return srv1.test1();
    }

    /**
     * reactive method
     *
     * @return string
     */
    @GetMapping("/test/2")
    public Mono<String> test2() {
        return srv1.test2();
    }

    /**
     * reactive method, block thread for 5 seconds
     *
     * @return string
     */
    @GetMapping("/test/3")
    public Mono<String> test3() {

        Scheduler scheduler = Schedulers.newBoundedElastic(5, 10, "MyThreadGroup");
        return srv1.test3().delayElement(Duration.ofMillis(5000), scheduler);

    }

    /**
     * reactive method, which call test1 and test2
     *
     * @return concat results from test1 and test2
     */
    @GetMapping("/test/4")
    public Mono<String> test4() {

        Mono<String> test1 = Mono.just(test1()).subscribeOn(Schedulers.elastic());
        Mono<String> test2 = test2().subscribeOn(Schedulers.elastic());
        return Mono.zip(test1, test2, String::concat);

    }

    /**
     * reactive method, called parallel test1..test4,blocked every thread for 5 seconds
     *
     * @return concat results from test1..test4
     */
    @GetMapping("/test/5")
    public ParallelFlux<String> test5() {
        Scheduler scheduler = Schedulers.newBoundedElastic(5, 10, "MyThreadGroup");
        Mono<String> result = Mono.just("header");
        List<String> urls = new ArrayList(Arrays.asList("1", "2", "3", "4"));
        ;
        return Flux.fromIterable(urls) //contains A, B and C
                .flatMap(
                        url -> WebClient.create("http://localhost:8080").get().uri("/test/{n}",url)
                         .retrieve()
                         .bodyToMono(String.class)
                          .delayElement(Duration.ofMillis(5000), scheduler))
                .parallel();
        /*(Mono<String>) Mono.just(test1()).subscribeOn(scheduler).flatMap(a -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return Mono.just(a);
        }).subscribe(System.out::println);*/
    }

    /**
     * reactive method, call test2 with Web Client
     *
     * @return string
     */
    @GetMapping("/test/6")
    public Mono<String> test6() {

        WebClient client = WebClient.create("http://localhost:8080");
        return client.get().uri("/test/2").retrieve().bodyToMono(String.class);

    }

}
