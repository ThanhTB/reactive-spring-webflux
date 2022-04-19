package com.learnreactiveprogramming.service;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

class FluxAndMonoGeneratorServiceTest {

    FluxAndMonoGeneratorService service = new FluxAndMonoGeneratorService();

    @Test
    void namesFlux() {
        // given

        // when
        Flux<String> namesFlux = service.namesFlux();

        // then
        StepVerifier.create(namesFlux)
//                .expectNext("flux", "spring boot", "database", "service")
//                .expectNextCount(4)
                .expectNext("flux")
                .expectNextCount(3)
                .verifyComplete();
    }
}