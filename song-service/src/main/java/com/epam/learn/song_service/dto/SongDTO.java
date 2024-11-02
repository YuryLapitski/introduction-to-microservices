package com.epam.learn.song_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SongDTO {
    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotBlank(message = "Artist cannot be blank")
    private String artist;

    @NotNull(message = "Album cannot be null")
    private String album;

    @NotNull(message = "Length cannot be null")
    @Size(min = 1, message = "Length must be greater than 0")
    private String length;

    @NotNull(message = "Resource ID cannot be null")
    private String resourceId;

    @NotBlank(message = "Year must be provided")
    @Size(min = 4, max = 4, message = "Year should be a 4-digit number")
    private String year;
}