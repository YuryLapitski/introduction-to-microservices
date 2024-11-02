package com.epam.learn.song_service.dao;

import com.epam.learn.song_service.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SongRepository extends JpaRepository<Song, Integer> {
}
