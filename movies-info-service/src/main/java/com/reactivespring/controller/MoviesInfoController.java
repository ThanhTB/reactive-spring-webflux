package com.reactivespring.controller;

import com.reactivespring.domain.MovieInfo;
import com.reactivespring.service.MovieInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/movies-info")
public class MoviesInfoController {

    @Autowired
    private MovieInfoService service;

    Sinks.Many<MovieInfo> movieInfoSink = Sinks.many().replay().all();

    @GetMapping
    public Flux<MovieInfo> getAllMoviesInfo(@RequestParam(value = "year", required = false) Integer year) {
        if (year != null) {
            return service.getMoviesInfoByYear(year);
        }
        return service.getAllMoviesInfo();
    }

    @GetMapping(value = "/{id}")
    public Mono<ResponseEntity<MovieInfo>> getMovieInfoById(@PathVariable String id) {
        return service.getMovieInfoById(id).map(ResponseEntity.ok()::body).switchIfEmpty(Mono.just(ResponseEntity.notFound().build())).log();
    }

    @GetMapping(value = "/stream", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<MovieInfo> getMovieInfoById() {
        return movieInfoSink.asFlux();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<MovieInfo> addMovieInfo(@RequestBody @Valid MovieInfo movieInfo) {
        return service.addMovieInfo(movieInfo).doOnNext(savedInfo -> movieInfoSink.tryEmitNext(savedInfo));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<MovieInfo> updateMovieInfo(@PathVariable String id, @RequestBody MovieInfo movieInfo) {
        return service.updateMovieInfo(id, movieInfo);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteMovieInfo(@PathVariable String id) {
        return service.deleteMovieInfo(id);
    }
}
