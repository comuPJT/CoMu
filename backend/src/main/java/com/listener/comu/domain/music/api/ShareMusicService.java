package com.listener.comu.domain.music.api;

import com.listener.comu.domain.music.dto.SharePlaylistMusicReq;
import com.listener.comu.domain.music.dto.SharePlaylistMusicRes;
import com.listener.comu.domain.music.dto.SearchMusicRes;

import java.util.List;


public interface ShareMusicService {
    List<SearchMusicRes> findMusicByQuery(String query);
    void addMusicToPlayList(Long roomId, SharePlaylistMusicReq musicPlayReq);
    void deleteMusicRequestFromPlayList(Long roomId, String playId);
    List<SharePlaylistMusicRes> getPlayedPlaylist(Long roomId); /* 진행된 일반 사연 보여주기 */
    List<SharePlaylistMusicRes> getHonoredMusicAndContent(Long roomId);
    void toggleLikeMusicRequest(Long playId, Long userId);
    /* 사연 상세보기 */
    SharePlaylistMusicRes getPlayedMusicFromPlayList(Long roomId, String playId);
    /* TO DO - 명예의 전당 사연 상세보기 */
    SharePlaylistMusicRes getPlayedMusicFromHonorList(Long roomId, String playId);
    /* TD DO - 명예의 전당 사연 삭제*/
    void deleteMusicFromHonorList(Long roomId, String playId);
    /* TD DO - 재생된 사연곡 사연 삭제*/
    void deletePlayedMusicFromPlayList(Long roomId, String playId);
    /* TO DO - 앞뒤로 15곡씩 보여주기 - 리스트 반환*/
    List<SharePlaylistMusicRes> getPlaylistUpAndDown ();
}
