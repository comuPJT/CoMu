package com.listener.comu.domain.music.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MusicRepository extends JpaRepository<Music, Long> {

    @Query(value = "SELECT * FROM music WHERE id IN (SELECT music_id FROM myplaylist_music WHERE myplaylist_id = :myplaylistId)", nativeQuery = true)
    List<Music> getMusicByMyplaylistId(long myplaylistId);

    // IN 절에 들어가는 musicIds 순서대로 음악 가져오기
    @Query(value = "SELECT * FROM music WHERE id IN (:musicIds) ORDER BY FIELD(id, :musicIds)", nativeQuery = true)
    List<Music> getSortedMusicsById(List<Long> musicIds);

    // spotify_id가 존재하면 해당 id 가져오기
    @Query(value = "SELECT spotify_id FROM music WHERE spotify_id IN (:spotifyIds)", nativeQuery = true)
    List<String> getPresentSpotifyIds(List<String> spotifyIds);

    // spotify_id로 해당 music 객체 id 가져오기
    @Query(value = "SELECT id FROM music WHERE spotify_id = :spotifyId", nativeQuery = true)
    long getMusicIdBySpotifyId(String spotifyId);

    Music getMusicBySpotifyId(String spotifyId);
    Music getMusicById(long id);

}
