package com.listener.comu.domain.music.api;

import com.listener.comu.domain.music.dto.SharePlaylistMusicReq;
import com.listener.comu.domain.music.dto.SharePlaylistMusicRes;
import com.listener.comu.domain.music.dto.SearchMusicRes;

import java.util.List;


public interface ShareMusicService {
    List<SearchMusicRes> findMusicByQuery(String query);
    void addMusicToPlayList(Long roomId, SharePlaylistMusicReq musicPlayReq);
    List<SharePlaylistMusicRes> getPlayedPlaylist(Long roomId);
    SharePlaylistMusicRes getPlayedMusicFromPlayList(Long roomId, String playId);
    void  deletePlayedMusicFromPlayList(Long roomId, String playId);
    void deleteMusicRequestFromPlayList(Long roomId, String playId);
    /* TO DO - 앞뒤로 15곡씩 보여주기 - 리스트 반환*/
    List<SharePlaylistMusicRes> getPlaylistUpAndDown (Long roomId);
    List<SharePlaylistMusicRes> getHonoredMusicAndContent(Long roomId);
    /* TO DO - 명예의 전당 사연 상세보기 */
    SharePlaylistMusicRes getPlayedMusicFromHonorList(Long roomId, String playId);
    /* TD DO - 명예의 전당 사연 삭제*/
    void deleteMusicFromHonorList(Long roomId, String playId);
    void toggleLikeMusicRequest(Long playId, Long userId);
    /* EVENT DRIVEN */
    /* TO DO - 재생하기 위해 다음곡 가져오기 */
    /* TO DO - 재생 후 list 조정 */
}
