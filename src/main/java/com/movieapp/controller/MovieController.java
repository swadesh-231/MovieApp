package com.movieapp.controller;

import com.movieapp.dto.MovieDto;
import com.movieapp.dto.MovieResponse;
import com.movieapp.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/v1/movies")
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;
    @PostMapping
    public ResponseEntity<MovieDto> createMovie(
            @RequestPart MultipartFile posterFile,
            @RequestPart MovieDto movieDto
    ) {
        MovieDto createdMovie = movieService.createMovie(movieDto, posterFile);
        return ResponseEntity.ok(createdMovie);
    }
    @GetMapping("/{movieId}")
    public ResponseEntity<MovieDto> getMovieById(@PathVariable Long movieId) {
        return ResponseEntity.ok(movieService.getMovieById(movieId));
    }
    @GetMapping
    public ResponseEntity<List<MovieDto>> getAllMovies() {
        return ResponseEntity.ok(movieService.getAllMovies());
    }

    @PutMapping("/{movieId}")
    public ResponseEntity<MovieDto> updateMovie(
            @PathVariable Long movieId,
            @RequestPart(required = false) MultipartFile posterFile,
            @RequestPart MovieDto movieDto
    ) {
        return ResponseEntity.ok(
                movieService.updateMovie(movieId, movieDto, posterFile)
        );
    }
    @DeleteMapping("/{movieId}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long movieId) {
        movieService.deleteMovie(movieId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/page")
    public ResponseEntity<MovieResponse> getMovies(
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        return ResponseEntity.ok(
                movieService.getMovies(pageNumber, pageSize, sortBy, sortDir)
        );
    }

}
