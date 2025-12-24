package com.movieapp.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieDto {
    private Long id;
    @NotBlank(message = "Movie title is required")
    @Size(min = 2, max = 100, message = "Movie title must be between 2 and 100 characters")
    private String title;

    @NotBlank(message = "Director name is required")
    @Size(min = 2, max = 60, message = "Director name must be between 2 and 60 characters")
    private String director;

    @NotBlank(message = "Studio name is required")
    @Size(min = 2, max = 60, message = "Studio name must be between 2 and 60 characters")
    private String studio;

    @NotEmpty(message = "Movie cast must not be empty")
    @Size(max = 20, message = "Movie cast cannot exceed 20 members")
    private Set<
            @NotBlank(message = "Cast name cannot be blank")
            @Size(min = 2, max = 50, message = "Cast name must be between 2 and 50 characters")
                    String
            > movieCast;

    @NotNull(message = "Release year is required")
    @Min(value = 1888, message = "Release year must be after 1888")
    @Max(value = 2100, message = "Release year must be valid")
    private Integer releaseYear;

    @NotBlank(message = "Poster name is required")
    private String poster;

    @NotBlank(message = "Poster URL is required")
    @Pattern(
            regexp = "^(http|https)://.*$",
            message = "Poster URL must be a valid URL"
    )
    private String posterUrl;
}
