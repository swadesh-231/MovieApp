package com.movieapp.dto;

import java.util.List;

public record MovieResponse(List<MovieDto> movieDtos,
                            Integer pageNumber,
                            Integer pageSize,
                            long totalElements,
                            int totalPages,
                            boolean isLast) {
}
