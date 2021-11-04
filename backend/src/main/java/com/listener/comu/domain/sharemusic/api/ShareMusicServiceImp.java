package com.listener.comu.domain.sharemusic.api;

import com.listener.comu.domain.sharemusic.dto.MusicPlayReq;
import com.listener.comu.domain.sharemusic.dto.PlayedMusicRes;
import com.listener.comu.domain.sharemusic.dto.SearchMusicRes;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
class ShareMusicServiceImpl implements ShareMusicService {
    @Override
    public List<SearchMusicRes> findMusicByQuery(String query) {
        return null;
    }

    @Override
    public void addMusicToPlayList(Long roomId, MusicPlayReq musicPlayReq) {

    }

    @Override
    public void deleteMusicFromPlayList(Long roomId, Long playId) {

    }

    @Override
    public List<PlayedMusicRes> getPlayedMusicAndContent(Long roomId) {
        return null;
    }

    @Override
    public List<PlayedMusicRes> getHonoredMusicAndContent(Long roomId) {
        return null;
    }

    @Override
    public void likeMusicRequest(Long playId, Long userId) {

    }

    @Override
    public void undoLikeMusicRequest(Long playId, Long userId) {

    }
}
