package com.epam.learn.song_service.service.validator;

import com.epam.learn.song_service.dao.SongRepository;
import com.epam.learn.song_service.dto.SongDTO;
import com.epam.learn.song_service.model.Song;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class SongService {
    @Autowired
    private SongRepository songRepository;

    public Song createSong(Song song) {
        if (songRepository.existsById(song.getId())) {
            throw new DataIntegrityViolationException("Metadata for resource ID=" + song.getId() + " already exists.");
        }
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
        song.setId(dto.getId());
        song.setName(dto.getName());
        song.setArtist(dto.getArtist());
        song.setAlbum(dto.getAlbum());
        song.setDuration(dto.getDuration());
        song.setYear(dto.getYear());
        return song;
    }

    public SongDTO convertToDTO(Song song) {
        SongDTO dto = new SongDTO();
        dto.setId(song.getId());
        dto.setName(song.getName());
        dto.setArtist(song.getArtist());
        dto.setAlbum(song.getAlbum());
        dto.setDuration(song.getDuration());
        dto.setYear(song.getYear());
        return dto;
    }
}