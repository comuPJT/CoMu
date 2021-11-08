package com.listener.comu.domain.music.api;

import com.listener.comu.domain.music.domain.Music;
import com.listener.comu.domain.music.domain.MusicRepository;
import com.listener.comu.domain.music.domain.SharePlaylistMusic;
import com.listener.comu.domain.music.dto.SharePlaylistMusicReq;
import com.listener.comu.domain.music.dto.SharePlaylistMusicRes;
import com.listener.comu.domain.music.dto.SearchMusicRes;
import com.listener.comu.domain.oauthlogin.api.entity.user.User;
import com.listener.comu.domain.oauthlogin.api.repository.user.UserRepository;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.listener.comu.config.RedisConfig.objectMapper;

@Service
class ShareMusicServiceImpl implements ShareMusicService {

    private final RedisTemplate<String,Object> redisTemplate;
    private final MusicRepository musicRepository;
    private final UserRepository userRepository;

    ShareMusicServiceImpl(RedisTemplate<String, Object> redisTemplate, MusicRepository musicRepository, UserRepository userRepository) {
        this.redisTemplate = redisTemplate;
        this.musicRepository = musicRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<SearchMusicRes> findMusicByQuery(String query) {
        return null;
    }

    @Override
    public void addMusicToPlayList(Long roomId, SharePlaylistMusicReq musicPlayReq) {
        final String key = "room:" + roomId; //room
        ListOperations<String, Object> operations = redisTemplate.opsForList();

        // TO DO - MariaDB에 음악정보 넣거나 불러오기

        SharePlaylistMusic play = SharePlaylistMusic.builder()
                .contents(musicPlayReq.getContents())
                .musicId(musicPlayReq.getMusicId())
                .userId(musicPlayReq.getUserId())
                .build();
        play.setId(); //unique Id

        operations.rightPush(key, play); // "room:[id]" 키에 저장하기
    }

    @Override
    public List<SharePlaylistMusicRes> getPlayedPlaylist(Long roomId) {
        final String key = "roomplayed:" + roomId; //room
        ListOperations<String, Object> operations = redisTemplate.opsForList();
        List<Object> roomPlaylist = operations.range(key, 0,-1);
        return null;
    }

    @Override
    public SharePlaylistMusicRes getPlayedMusicFromPlayList(Long roomId, String playId) {
        final String key = "roomplayed:" + roomId; //room
        ListOperations<String, Object> operations = redisTemplate.opsForList();
        List<Object> roomPlaylist = operations.range(key, 0,-1);
        if( roomPlaylist !=null ){
            int size = roomPlaylist.size();
            for(int i = 0 ; i < size ; i++) {
                SharePlaylistMusic play = objectMapper().convertValue(roomPlaylist.get(i), SharePlaylistMusic.class);
                if (playId.equals(play.getPlayId())) {
                    Long likeCount = redisTemplate.opsForSet().size("sharelike:" + playId);
                    Music reqMusic = musicRepository.getMusicById(play.getMusicId());
                    User user = userRepository.getById(play.getUserId());
                    if (likeCount!=null && reqMusic != null ) {
                        return SharePlaylistMusicRes.builder()
                                .playId(play.getPlayId())
                                .title(play.getTitle())
                                .contents(play.getContents())
                                .timestamp(play.getTimestamp())
                                .name(reqMusic.getName())
                                .singer(reqMusic.getSinger())
                                .username(user.getUsername())
                                .likes(likeCount)
                                .build();
                    }
                }
            }
        }
        return null;
    }

    @Override
    public void deletePlayedMusicFromPlayList(Long roomId, String playId) {
        final String key = "roomplayed:" + roomId; //room
        deleteMusicFromPlaylist(playId, key);
    }

    @Override
    public void deleteMusicRequestFromPlayList(Long roomId, String playId) {
        String key = "room:" + roomId; //room
        deleteMusicFromPlaylist(playId, key);
    }

    private void deleteMusicFromPlaylist(String playId, String key) {
        ListOperations<String, Object> operations = redisTemplate.opsForList();
        List<Object> roomPlaylist = operations.range(key, 0,-1);
        if( roomPlaylist !=null ) {
            int size = roomPlaylist.size();
            for(int i = 0 ; i < size ; i++) {
                SharePlaylistMusic play = objectMapper().convertValue(roomPlaylist.get(i), SharePlaylistMusic.class);
                if (playId.equals(play.getPlayId())) {
                    operations.remove(key, 1, play);
                    return;
                }
            }
        }
    }

    @Override
    public List<SharePlaylistMusicRes> getHonoredMusicAndContent(Long roomId) {
        return null;
    }

    @Override
    public SharePlaylistMusicRes getPlayedMusicFromHonorList(Long roomId, String playId) {
        return null;
    }

    @Override
    public void deleteMusicFromHonorList(Long roomId, String playId) {

    }

    @Override
    public List<SharePlaylistMusicRes> getPlaylistUpAndDown() {
        return null;
    }


    @Override
    public void toggleLikeMusicRequest(Long playId, Long userId) {
        SetOperations<String,Object> setOperations = redisTemplate.opsForSet();
        final String key = "sharelike:" + playId;
        if (Boolean.TRUE.equals(setOperations.isMember(key, userId))) setOperations.add(key, userId);
        else setOperations.remove(key, userId);
    }
}
