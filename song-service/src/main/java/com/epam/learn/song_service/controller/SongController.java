package com.epam.learn.song_service.controller;

import com.epam.learn.song_service.dto.SongDTO;
import com.epam.learn.song_service.model.Song;
import com.epam.learn.song_service.service.SongService;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/songs")
public class SongController {
    @Autowired
    private SongService songService;

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<Map<String, Integer>> createSong(@Valid @RequestBody SongDTO songDTO) throws Exception {
        try {
            Song createdSong = songService.createSong(songService.convertToEntity(songDTO));
            return ResponseEntity.ok().body(Map.of("id", createdSong.getId()));
        } catch (Exception e) {
            throw new Exception("An internal server error has occurred. " + e.getMessage());
        }
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<SongDTO> getSongById(@PathVariable("id") Integer id) throws Exception {
        if (id <= 0) {
            throw new BadRequestException(String.format("Invalid value '%s' for ID. Must be a positive integer", id));
        }
        try {
            return ResponseEntity.ok(songService.getSongById(id));
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(String.format("The song with ID=%s does not exist.", id));
        } catch (Exception e) {
            throw new Exception("An internal server error has occurred. " + e.getMessage());
        }
    }

    @DeleteMapping(produces = "application/json")
    public ResponseEntity<Map<String, List<Integer>>> deleteSongs(@RequestParam String id) throws Exception {
        if (id == null || id.isEmpty()) {
            throw new BadRequestException("ID list should not be empty");
        }
        if (id.length() >= 200) {
            throw new BadRequestException(
                    String.format("IDs string too long. Received %s characters, but must be less than 200 characters.", id.length()));
        }
        try{
            Map<String, List<Integer>> deletedIds = Map.of("ids", songService.deleteSongs(id));
            return ResponseEntity.ok().body(deletedIds);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Invalid id format. ID should be a CSV string of integer.");
        } catch (Exception e) {
            throw new Exception("An internal server error has occurred. " + e.getMessage());
        }

    }
}