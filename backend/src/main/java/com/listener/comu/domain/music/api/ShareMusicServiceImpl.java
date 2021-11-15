package com.listener.comu.domain.music.api;

import com.listener.comu.domain.music.domain.*;
import com.listener.comu.domain.music.dto.SharePlaylistMusicReq;
import com.listener.comu.domain.music.dto.SharePlaylistMusicRes;
import com.listener.comu.domain.oauthlogin.api.entity.user.User;
import com.listener.comu.domain.oauthlogin.api.repository.user.UserRepository;
import com.listener.comu.util.S3Uploader;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.listener.comu.config.RedisConfig.objectMapper;

@Service
class ShareMusicServiceImpl implements ShareMusicService {

    private final String musicReqPrefix = "room";
    private final String playedPrefix = "roomPlayed";
    private final String musicLikePrefix = "shareLike";
    private final long limit = 15L;

    private final RedisTemplate<String,Object> redisTemplate;
    private final MusicRepository musicRepository;
    private final UserRepository userRepository;
    private final HistoryRepository historyRepository;
    private final S3Uploader s3Uploader;

    ShareMusicServiceImpl(RedisTemplate<String, Object> redisTemplate, MusicRepository musicRepository, UserRepository userRepository, HistoryRepository historyRepository, S3Uploader s3Uploader) {
        this.redisTemplate = redisTemplate;
        this.musicRepository = musicRepository;
        this.userRepository = userRepository;
        this.historyRepository = historyRepository;
        this.s3Uploader = s3Uploader;
    }

    @Override
    public boolean addMusicToPlayList(Long roomId, SharePlaylistMusicReq musicPlayReq) {
        String key = musicReqPrefix + ":" + roomId; //room
        ListOperations<String, Object> operations = redisTemplate.opsForList();
        Long size = operations.size(key);
        if( size != null && size < limit ) { //15개 미만일때만!
            Music music = musicRepository.getMusicBySpotifyId(musicPlayReq.getSpotifyId());
            if( music == null) {
                // 음악 테이블에 데이터 추가
                music = Music.builder().spotifyId(musicPlayReq.getSpotifyId())
                                    .thumbnail(musicPlayReq.getThumbnail())
                                    .name(musicPlayReq.getName())
                                    .singer(musicPlayReq.getSinger())
                                    .source(musicPlayReq.getSource()).build();
                musicRepository.save(music);
                // 음악 다운로드 -> s3 업로딩
                String cmd = "youtube-dl -f 160+140 -o src/main/resources/static/" + music.getId() + ".%(ext)s " + music.getSource();
                Runtime rt = Runtime.getRuntime();
                try {
                    Process pr = rt.exec(cmd);
                    pr.waitFor();
                    String sourceFilepath = "src/main/resources/static/" + music.getId() + ".mp4";
                    s3Uploader.dirUpload(new File(sourceFilepath),"static");
                    pr.destroy();
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // redis 현재 방 플레이리스트에 추가
            SharePlaylistMusic play = SharePlaylistMusic.builder()
                    .contents(musicPlayReq.getContents())
                    .musicId(music.getId())
                    .userId(musicPlayReq.getUserId())
                    .title(musicPlayReq.getTitle())
                    .build();
            play.setId(); //unique Id

            operations.rightPush(key, play); // "room:[id]" 키에 저장하기
            return true;
        }
        return false;
    }

    @Override
    public List<SharePlaylistMusicRes> getPlayedPlaylist(Long roomId) {
        final String key = playedPrefix + ":" + roomId; //room
        ListOperations<String, Object> operations = redisTemplate.opsForList();
        List<Object> roomPlaylist = operations.range(key, 0,-1);
        List<SharePlaylistMusicRes> response = new ArrayList<>();
        if(roomPlaylist != null) {
            convertObjectListToDtoList(roomPlaylist, response);
        }
        return response;
    }

    @Override
    public List<SharePlaylistMusicRes> getPlaylistUpAndDown(Long roomId) {
        List<Object> resObjectList = new ArrayList<>();
        String playedKey = playedPrefix + ":" + roomId;
        String reqKey = musicReqPrefix + ":" + roomId;
        ListOperations<String, Object> operations = redisTemplate.opsForList();
        List<Object> playedList = operations.range(playedKey, 0,-1);
        List<Object> requestList = operations.range(reqKey, 0 ,-1);
        List<SharePlaylistMusicRes> response = new ArrayList<>();
        if( playedList != null ) {
            if( playedList.size() <= limit)
                resObjectList.addAll(playedList);
            else resObjectList.addAll(playedList.subList(0, (int)limit));
        }
        if( requestList != null ) resObjectList.addAll(requestList);
        convertObjectListToDtoList(resObjectList, response);
        return response;
    }

    private void convertObjectListToDtoList(List<Object> resObjectList, List<SharePlaylistMusicRes> response) {
        for (Object o : resObjectList) {
            SharePlaylistMusic play = objectMapper().convertValue(o, SharePlaylistMusic.class);
            Long likeCount = redisTemplate.opsForSet().size( musicLikePrefix + ":" + play.getPlayId());
            Music reqMusic = musicRepository.getMusicById(play.getMusicId());
            User user = userRepository.getById(play.getUserId());
            if (likeCount != null && reqMusic != null) {
                response.add(SharePlaylistMusicRes.builder()
                        .playId(play.getPlayId())
                        .title(play.getTitle())
                        .contents(play.getContents())
                        .timestamp(play.getTimestamp())
                        .name(reqMusic.getName())
                        .singer(reqMusic.getSinger())
                        .username(user.getUsername())
                        .likes(likeCount)
                        .build());
            }
        }
    }

    @Override
    public SharePlaylistMusicRes getPlayedMusicFromPlayList(Long roomId, String playId) {
        final String key = playedPrefix + ":" + roomId; //room
        ListOperations<String, Object> operations = redisTemplate.opsForList();
        List<Object> roomPlaylist = operations.range(key, 0,-1);
        if( roomPlaylist !=null ){
            for (Object o : roomPlaylist) {
                SharePlaylistMusic play = objectMapper().convertValue(o, SharePlaylistMusic.class);
                if (playId.equals(play.getPlayId())) {
                    Long likeCount = redisTemplate.opsForSet().size("sharelike:" + playId);
                    Music reqMusic = musicRepository.getMusicById(play.getMusicId());
                    User user = userRepository.getById(play.getUserId());
                    if (likeCount != null && reqMusic != null) {
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
        final String key = playedPrefix + ":" + roomId; //room
        deleteMusicFromPlaylist(playId, key);
    }

    @Override
    public void deleteMusicRequestFromPlayList(Long roomId, String playId) {
        String key = musicReqPrefix + ":"+ roomId; //room
        deleteMusicFromPlaylist(playId, key);
    }

    private void deleteMusicFromPlaylist(String playId, String key) {
        ListOperations<String, Object> operations = redisTemplate.opsForList();
        List<Object> roomPlaylist = operations.range(key, 0,-1);
        if( roomPlaylist !=null ) {
            for (Object o : roomPlaylist) {
                SharePlaylistMusic play = objectMapper().convertValue(o, SharePlaylistMusic.class);
                if (playId.equals(play.getPlayId())) {
                    operations.remove(key, 1, play);
                    return;
                }
            }
        }
    }

    @Override
    public List<SharePlaylistMusicRes> getHonoredPlayList() {
        List<History> history = historyRepository.findAll();
        List<SharePlaylistMusicRes> response = new ArrayList<>();
        for(History h : history){
            response.add(SharePlaylistMusicRes.builder()
                    .playId(h.getId().toString())
                    .title(h.getTitle())
                    .contents(h.getContents())
                    .timestamp(h.getTimestamp())
                    .name(h.getMusic().getName())
                    .singer(h.getMusic().getSinger())
                    .username(h.getUser().getUsername())
                    .likes(h.getLikes())
                    .build());
        }
        return response;
    }

    @Override
    public SharePlaylistMusicRes HonoredMusicAndContents(Long playId) {
        History history = historyRepository.getHistoryById(playId);
        if( history != null) {
            return SharePlaylistMusicRes.builder()
                    .playId(history.getId().toString())
                    .title(history.getTitle())
                    .contents(history.getContents())
                    .timestamp(history.getTimestamp())
                    .name(history.getMusic().getName())
                    .singer(history.getMusic().getSinger())
                    .username(history.getUser().getUsername())
                    .likes(history.getLikes())
                    .build();
        }
        return null;
    }

    @Override
    public void deleteMusicFromHonorList(Long playId) {
        historyRepository.deleteById(playId);
    }


    @Override
    public void toggleLikeMusicRequest(Long playId, Long userId) {
        SetOperations<String,Object> setOperations = redisTemplate.opsForSet();
        final String key = musicLikePrefix + ":" + playId;
        if (Boolean.TRUE.equals(setOperations.isMember(key, userId))) setOperations.add(key, userId);
        else setOperations.remove(key, userId);
    }

    @Override
    public long getNextMusic(String playSeq) {
        return 0;
    }
}
