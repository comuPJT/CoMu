package com.listener.comu.domain.mymusic.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MusicRepository extends JpaRepository<Music, Long> {

    @Query(value = "SELECT * FROM music WHERE id IN (SELECT music_id FROM myplaylist_music WHERE myplaylist_id = :myplaylistId)", nativeQuery = true)
    List<Music> getMusicByMyplaylistId(long myplaylistId);

}
