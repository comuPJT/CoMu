package com.listener.comu.domain.sharemusic.api;

import com.listener.comu.domain.sharemusic.dto.MusicPlayReq;
import com.listener.comu.domain.sharemusic.dto.PlayedMusicRes;
import com.listener.comu.domain.sharemusic.dto.SearchMusicRes;

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
