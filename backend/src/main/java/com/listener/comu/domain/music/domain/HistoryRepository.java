package com.listener.comu.domain.music.domain;

import com.listener.comu.domain.music.dto.SharePlaylistMusicRes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HistoryRepository extends JpaRepository<History, Long> {
//    @Query(name= "getHistoryMusicsByRoomId", nativeQuery = true)
//    List<SharePlaylistMusicRes> getHistoryMusicsByRoomId(Long roomId);
    List<History> getHistoriesByRoomId(Long roomId);
//    @Query(value= "getHistoryMusicById", nativeQuery = true)
//    SharePlaylistMusicRes getHistoryMusicById(Long id);
    History getHistoryById(Long id);
}
