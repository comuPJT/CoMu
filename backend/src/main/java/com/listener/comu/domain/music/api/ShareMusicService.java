package com.listener.comu.domain.music.api;

import com.listener.comu.domain.music.dto.MusicPlayReq;
import com.listener.comu.domain.music.dto.PlayedMusicRes;
import com.listener.comu.domain.music.dto.SearchMusicRes;

import java.util.List;


public interface ShareMusicService {
    List<SearchMusicRes> findMusicByQuery(String query);
    void addMusicToPlayList(Long roomId, MusicPlayReq musicPlayReq);
    void deleteMusicFromPlayList(Long roomId, Long playId);
    List<PlayedMusicRes> getPlayedMusicAndContent(Long roomId);
    List<PlayedMusicRes> getHonoredMusicAndContent(Long roomId);
    void likeMusicRequest(Long playId, Long userId);
    void undoLikeMusicRequest(Long playId, Long userId);
}
