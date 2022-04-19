package com.reactivespring.controller;

import com.reactivespring.client.MoviesInfoRestClient;
import com.reactivespring.client.ReviewsRestClient;
import com.reactivespring.domain.Movie;
import com.reactivespring.domain.MovieInfo;
import com.reactivespring.domain.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/v1/movies")
public class MoviesController {
    @Autowired
    MoviesInfoRestClient moviesInfoRestClient;

    @Autowired
    ReviewsRestClient reviewsRestClient;

    @GetMapping("/{id}")
    public Mono<Movie> retrieveMovieById(@PathVariable("id") String id) {
        return moviesInfoRestClient.retrieveMovieInfo(id).flatMap(movieInfo -> {
            Mono<List<Review>> reviewsMono = reviewsRestClient.retrieveReview(id).collectList();
            return reviewsMono.map(reviews -> new Movie(movieInfo, reviews));
        });
    }

    @GetMapping(value = "/stream", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<MovieInfo> retrieveMovieInfos() {
        return moviesInfoRestClient.retrieveMovieInfoStream();
    }
}
