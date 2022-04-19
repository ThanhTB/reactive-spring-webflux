package com.learnreactiveprogramming.service;

import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.function.Function;

public class FluxAndMonoGeneratorService {

    public Flux<String> namesFlux(){
        return Flux.fromIterable(List.of("flux", "spring boot", "database", "service")).log();
    }

    public Mono<String> nameMono() {
        return Mono.just("mono").log();
    }

    public Flux<String> namesFlux_map(int stringLength){
        return Flux.fromIterable(List.of("flux", "spring boot", "database", "service"))
                .map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
                .map(s -> s.length() + " - " + s);
    }

    public Flux<String> namesFlux_flatmap(int stringLength){
        return Flux.fromIterable(List.of("flux", "spring boot", "database", "service"))
                .map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
//                .flatMap(s -> splitString(s)).log()
                .concatMap(s -> splitString(s));
    }

    private Flux<String> splitString(String name) {
        System.out.println(name);
        String[] split = name.split("");
        int delay = new Random().nextInt(1000);
        return Flux.fromArray(split).map(String::toLowerCase);
    }


    public static void main(String[] args) {
        FluxAndMonoGeneratorService service = new FluxAndMonoGeneratorService();
//        service.namesFlux().subscribe(s -> {
//            System.out.println("Flux -> Name is: " + s);
//        });
//
//        service.namesFlux_map(4).subscribe(s -> {
//            System.out.println("Flux -> toUpperCase -> Name is: " + s);
//        });
//
//        service.nameMono().subscribe(s -> {
//            System.out.println("Mono -> Name is: " + s);
//        });

        service.namesFlux_flatmap(10).subscribe(s -> {
            System.out.println("Flux -> flatMap -> Name is: " + s);
        });
    }
}
