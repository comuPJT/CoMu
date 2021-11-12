package com.listener.comu.domain.music.api;

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
    /* EVENT DRIVEN */
    /* TO DO - 재생하기 위해 다음곡 가져오기 */
    /* TO DO - 재생 후 list 조정 */
}
