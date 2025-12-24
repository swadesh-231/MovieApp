package com.movieapp.service.impl;

import com.movieapp.dto.MovieDto;
import com.movieapp.dto.MovieResponse;
import com.movieapp.entity.Movie;
import com.movieapp.exception.MovieNotFoundException;
import com.movieapp.repository.MovieRepository;
import com.movieapp.service.FileService;
import com.movieapp.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {
    private final MovieRepository movieRepository;
    private final FileService fileService;
    private final ModelMapper modelMapper;

    @Value("${project.files}")
    private String posterPath;

    @Value("${base.url}")
    private String baseUrl;
    @Override
    public MovieDto createMovie(MovieDto movieDto, MultipartFile posterFile) {
        String fileName = fileService.uploadFile(posterPath, posterFile);
        movieDto.setPoster(fileName);
        Movie movie = modelMapper.map(movieDto, Movie.class);
        Movie savedMovie = movieRepository.save(movie);
        MovieDto response = modelMapper.map(savedMovie, MovieDto.class);
        response.setPosterUrl(baseUrl + "/file/" + fileName);
        return response;
    }

    @Override
    public MovieDto getMovieById(Long movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() ->
                        new MovieNotFoundException("Movie not found with id: " + movieId));

        MovieDto response = modelMapper.map(movie, MovieDto.class);
        response.setPosterUrl(baseUrl + "/file/" + movie.getPoster());

        return response;
    }

    @Override
    public List<MovieDto> getAllMovies() {
        return movieRepository.findAll()
                .stream()
                .map(movie -> {
                    MovieDto dto = modelMapper.map(movie, MovieDto.class);
                    dto.setPosterUrl(baseUrl + "/file/" + movie.getPoster());
                    return dto;
                })
                .toList();
    }

    @Override
    public MovieDto updateMovie(Long movieId, MovieDto movieDto, MultipartFile posterFile) {
        Movie existingMovie = movieRepository.findById(movieId)
                .orElseThrow(() ->
                        new MovieNotFoundException("Movie not found with id: " + movieId));

        String posterName = existingMovie.getPoster();

        if (posterFile != null && !posterFile.isEmpty()) {
            posterName = fileService.uploadFile(posterPath, posterFile);
        }

        movieDto.setPoster(posterName);

        modelMapper.map(movieDto, existingMovie);
        Movie updatedMovie = movieRepository.save(existingMovie);

        MovieDto response = modelMapper.map(updatedMovie, MovieDto.class);
        response.setPosterUrl(baseUrl + "/file/" + posterName);

        return response;
    }

    @Override
    public void deleteMovie(Long movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() ->
                        new MovieNotFoundException("Movie not found with id: " + movieId));

        fileService.deleteFile(posterPath, movie.getPoster());
        movieRepository.delete(movie);

    }

    @Override
    public MovieResponse getMovies(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Movie> page = movieRepository.findAll(pageable);

        List<MovieDto> movies = page.getContent()
                .stream()
                .map(movie -> {
                    MovieDto dto = modelMapper.map(movie, MovieDto.class);
                    dto.setPosterUrl(baseUrl + "/file/" + movie.getPoster());
                    return dto;
                })
                .toList();

        return new MovieResponse(
                movies,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }
}
