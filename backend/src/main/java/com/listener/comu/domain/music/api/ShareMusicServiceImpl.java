package com.listener.comu.domain.music.api;

import com.listener.comu.domain.music.domain.SharePlaylistMusic;
import com.listener.comu.domain.music.dto.SharePlaylistMusicReq;
import com.listener.comu.domain.music.dto.SharePlaylistMusicRes;
import com.listener.comu.domain.music.dto.SearchMusicRes;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.listener.comu.config.RedisConfig.objectMapper;

@Service
class ShareMusicServiceImpl implements ShareMusicService {

    private final RedisTemplate<String,Object> redisTemplate;

    ShareMusicServiceImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public List<SearchMusicRes> findMusicByQuery(String query) {
        return null;
    }

    @Override
    public void addMusicToPlayList(Long roomId, SharePlaylistMusicReq musicPlayReq) {
        final String key = "room:" + roomId; //room
        ListOperations<String, Object> operations = redisTemplate.opsForList();

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
        ListOperations<String, Object> operations = redisTemplate.opsForList();
        List<Object> roomPlaylist = operations.range(key, 0,-1);
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
    public List<SharePlaylistMusicRes> getPlayedMusicAndContent(Long roomId) {
        return null;
    }

    @Override
    public List<SharePlaylistMusicRes> getHonoredMusicAndContent(Long roomId) {
        return null;
    }

    @Override
    public void toggleLikeMusicRequest(Long playId, Long userId) {
        SetOperations<String,Object> setOperations = redisTemplate.opsForSet();
        final String key = "sharealike:" + playId;
        if( !setOperations.isMember(key, userId)) setOperations.add(key, userId);
        else setOperations.remove(key,userId);
    }
}
