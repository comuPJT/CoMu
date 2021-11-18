package com.listener.comu.domain.music.api;

import com.listener.comu.domain.music.dto.NextMusicRes;
import com.listener.comu.domain.music.dto.SharePlaylistMusicReq;
import com.listener.comu.domain.music.dto.SharePlaylistMusicRes;

import java.util.List;


public interface ShareMusicService {
    List<SharePlaylistMusicRes> getPlaylistUpAndDown (Long roomId);
    boolean addMusicToPlayList(Long roomId, SharePlaylistMusicReq musicPlayReq);
    List<SharePlaylistMusicRes> getPlayedPlaylist(Long roomId);
    SharePlaylistMusicRes getPlayedMusicFromPlayList(Long roomId, String playId);
    void deletePlayedMusicFromPlayList(Long roomId, String playId);
    void deleteMusicRequestFromPlayList(Long roomId, String playId);
    List<SharePlaylistMusicRes> getHonoredPlayList();
    SharePlaylistMusicRes HonoredMusicAndContents(Long playId);
    void deleteMusicFromHonorList(Long playId);
    void toggleLikeMusicRequest(Long playId, Long userId);
}
