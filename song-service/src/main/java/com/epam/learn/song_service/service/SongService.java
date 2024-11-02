package com.epam.learn.song_service.service;

import com.epam.learn.song_service.dao.SongRepository;
import com.epam.learn.song_service.dto.SongDTO;
import com.epam.learn.song_service.model.Song;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class SongService {
    @Autowired
    private SongRepository songRepository;

    public Song createSong(Song song) {
        return songRepository.save(song);
    }

    public SongDTO getSongById(Integer id) {
        Song song = songRepository.findById(id).orElseThrow();
        return convertToDTO(song);
    }

    public List<Integer> deleteSongs(String idsCSV) {
        List<Integer> ids = Arrays.stream(idsCSV.split(","))
                .map(Integer::parseInt)
                .toList();

        List<Integer> existingIds = ids.stream()
                .filter(id -> songRepository.existsById(id))
                .toList();

        songRepository.deleteAllById(existingIds);
        return existingIds;
    }

    public Song convertToEntity(SongDTO dto) {
        Song song = new Song();
        song.setName(dto.getName());
        song.setArtist(dto.getArtist());
        song.setAlbum(dto.getAlbum());
        song.setLength(dto.getLength());
        song.setResourceId(dto.getResourceId());
        song.setYear(dto.getYear());
        return song;
    }

    public SongDTO convertToDTO(Song song) {
        SongDTO dto = new SongDTO();
        dto.setName(song.getName());
        dto.setArtist(song.getArtist());
        dto.setAlbum(song.getAlbum());
        dto.setLength(song.getLength());
        dto.setResourceId(song.getResourceId());
        dto.setYear(song.getYear());
        return dto;
    }
}