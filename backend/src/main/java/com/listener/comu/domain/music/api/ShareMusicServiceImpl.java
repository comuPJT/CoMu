package com.listener.comu.domain.music.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.listener.comu.domain.music.domain.SharePlaylistMusic;
import com.listener.comu.domain.music.dto.MusicPlayReq;
import com.listener.comu.domain.music.dto.PlayedMusicRes;
import com.listener.comu.domain.music.dto.SearchMusicRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.listener.comu.config.RedisConfig.objectMapper;

@Service
class ShareMusicServiceImpl implements ShareMusicService {

    private final RedisTemplate<String,SharePlaylistMusic> redisTemplate;

    ShareMusicServiceImpl(RedisTemplate<String, SharePlaylistMusic> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public List<SearchMusicRes> findMusicByQuery(String query) {
        return null;
    }

    @Override
    public void addMusicToPlayList(Long roomId, MusicPlayReq musicPlayReq) {
        final String key = "room:" + roomId; //room
        ListOperations<String, SharePlaylistMusic> operations = redisTemplate.opsForList();
        SharePlaylistMusic play = SharePlaylistMusic.builder()
                .contents(musicPlayReq.getContents())
                .musicId(musicPlayReq.getMusicId())
                .userId(musicPlayReq.getUserId())
                .build();
        play.setId(); //unique Id
        operations.rightPush(key, play); // "room:[id]" 키에 저장하기
    }

    @Override
    public void deleteMusicFromPlayList(Long roomId, String playId) {
        final String key = "room:" + roomId; //room
        ListOperations<String, SharePlaylistMusic> operations = redisTemplate.opsForList();
        List<SharePlaylistMusic> roomPlaylist = operations.range(key, 0,-1);
        int size = roomPlaylist.size();
        for(int i = 0 ; i < size ; i++) {
            SharePlaylistMusic play = objectMapper().convertValue(roomPlaylist.get(i), SharePlaylistMusic.class);
            if (playId.equals(play.getPlayId())) {
                operations.remove(key, 1, play);
                return;
            }
        }
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
