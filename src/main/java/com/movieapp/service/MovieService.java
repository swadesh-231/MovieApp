package com.movieapp.service;

import com.movieapp.dto.MovieDto;
import com.movieapp.dto.MovieResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface MovieService {
    MovieDto createMovie(MovieDto movieDto, MultipartFile posterFile);
    MovieDto getMovieById(Long movieId);
    List<MovieDto> getAllMovies();
    MovieDto updateMovie(Long movieId, MovieDto movieDto, MultipartFile posterFile);
    void deleteMovie(Long movieId);
    MovieResponse getMovies(
            Integer pageNumber,
            Integer pageSize,
            String sortBy,
            String sortDir
    );
}
