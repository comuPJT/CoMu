package com.listener.comu.domain.music.api;

import com.listener.comu.domain.music.dto.SharePlaylistMusicReq;
import com.listener.comu.domain.music.dto.SharePlaylistMusicRes;
import com.listener.comu.domain.music.dto.SearchMusicRes;

import java.util.List;


public interface ShareMusicService {
    List<SearchMusicRes> findMusicByQuery(String query);
    void addMusicToPlayList(Long roomId, SharePlaylistMusicReq musicPlayReq);
    void deleteMusicFromPlayList(Long roomId, String playId);
    List<SharePlaylistMusicRes> getPlayedMusicAndContent(Long roomId);
    List<SharePlaylistMusicRes> getHonoredMusicAndContent(Long roomId);
    void toggleLikeMusicRequest(Long playId, Long userId);
}
