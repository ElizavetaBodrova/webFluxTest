package ru.neoflex.services;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import ru.neoflex.intf.Interface1;

import java.time.Duration;


@Component
public class Service1 implements Interface1 {

    public String test1(){
        return "test1";
    }
    public Mono<String> test2(){
        return Mono.just("test2");
    }
    public Mono<String> test3(){
        return Mono.just("test3");
    }

}
