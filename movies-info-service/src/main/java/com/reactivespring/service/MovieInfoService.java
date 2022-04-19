package com.reactivespring.service;

import com.reactivespring.domain.MovieInfo;
import com.reactivespring.repository.MovieInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class MovieInfoService {

    @Autowired
    private MovieInfoRepository repository;

    public Mono<MovieInfo> addMovieInfo(MovieInfo movieInfo) {
        return repository.save(movieInfo);
    }

    public Flux<MovieInfo> getAllMoviesInfo() {
        return repository.findAll();
    }

    public Mono<MovieInfo> getMovieInfoById(String id) {
        return repository.findById(id);
    }

    public Mono<MovieInfo> updateMovieInfo(String id, MovieInfo movieInfo) {
        return repository.findById(id).flatMap(movie -> {
            movie.setCast(movieInfo.getCast());
            movie.setName(movieInfo.getName());
            movie.setYear(movieInfo.getYear());
            movie.setRelease_date(movieInfo.getRelease_date());

            return repository.save(movie);
        });
    }

    public Mono<Void> deleteMovieInfo(String id) {
        return repository.deleteById(id);
    }

    public Flux<MovieInfo> getMoviesInfoByYear(Integer year) {
        return repository.findByYear(year);
    }
}
