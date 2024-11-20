package com.epam.learn.song_service.dto;

import com.epam.learn.song_service.service.validator.ValidDuration;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SongDTO {
    @NotNull(message = "ID cannot be null")
    private int id;

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotBlank(message = "Artist cannot be blank")
    private String artist;

    @NotNull(message = "Album cannot be null")
    private String album;

    @NotBlank(message = "Duration cannot be null")
    @ValidDuration(message = "Duration must be in the format MM:SS or HH:MM:SS")
    private String duration;

    @NotBlank(message = "Year must be provided")
    @Size(min = 4, max = 4, message = "Year must be in YYYY format")
    private String year;
}