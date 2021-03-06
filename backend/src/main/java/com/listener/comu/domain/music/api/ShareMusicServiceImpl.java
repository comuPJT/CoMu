package com.listener.comu.domain.music.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.listener.comu.domain.music.domain.*;
import com.listener.comu.domain.music.dto.SharePlaylistMusicReq;
import com.listener.comu.domain.music.dto.SharePlaylistMusicRes;
import com.listener.comu.domain.oauthlogin.api.entity.user.User;
import com.listener.comu.domain.oauthlogin.api.repository.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.listener.comu.config.RedisConfig.objectMapper;

@Slf4j
@Service
class ShareMusicServiceImpl implements ShareMusicService {

    private final String musicReqPrefix = "room:";
    private final String playedPrefix = "roomPlayed:";
    private final String musicLikePrefix = "shareLike:";
    private final String nowPlayingKey = "nowplaying:";
    private final long limit = 15L;

    private final RedisTemplate<String, Object> redisTemplate;
    private final MusicRepository musicRepository;
    private final UserRepository userRepository;
    private final HistoryRepository historyRepository;
    final StreamingService streamingService;

    ShareMusicServiceImpl(RedisTemplate<String, Object> redisTemplate, MusicRepository musicRepository, UserRepository userRepository, HistoryRepository historyRepository, StreamingService streamingService) {
        this.redisTemplate = redisTemplate;
        this.musicRepository = musicRepository;
        this.userRepository = userRepository;
        this.historyRepository = historyRepository;
        this.streamingService = streamingService;
    }

    public <T> T getRedisValue(String key, String hasyKey, Class<T> classType) throws JsonProcessingException {
        String redisValue = (String) redisTemplate.opsForHash().get(key, hasyKey);
        if (ObjectUtils.isEmpty(redisValue)) {
            return null;
        } else {
            return objectMapper().readValue(redisValue, classType);
        }
    }

    @Override
    public boolean addMusicToPlayList(Long roomId, SharePlaylistMusicReq musicPlayReq) {
        String key = musicReqPrefix + roomId; //room
        ListOperations<String, Object> operations = redisTemplate.opsForList();
        Long size = operations.size(key);
        try {
            if (size != null && size < limit) { //15??? ???????????????!
                Music music = musicRepository.getMusicBySpotifyId(musicPlayReq.getSpotifyId());
                if (music == null) {
                    // ?????? ???????????? ????????? ??????
                    music = Music.builder().spotifyId(musicPlayReq.getSpotifyId())
                            .thumbnail(musicPlayReq.getThumbnail())
                            .name(musicPlayReq.getName())
                            .singer(musicPlayReq.getSinger())
                            .source(musicPlayReq.getSource())
                            .album(musicPlayReq.getAlbum())
                            .onCloud(0)
                            .build();
                    musicRepository.save(music);
                    // ?????? ???????????? -> s3 ?????????
                    streamingService.executeDownloadAndUploadToS3(music);
                }
                // redis ?????? ??? ????????????????????? ??????
                SharePlaylistMusic play = SharePlaylistMusic.builder()
                        .contents(musicPlayReq.getContents())
                        .musicId(music.getId())
                        .userId(musicPlayReq.getUserId())
                        .title(musicPlayReq.getTitle())
                        .build();
                play.setId(); //unique Id

                operations.rightPush(key, objectMapper().writeValueAsString(play)); // "room:[id]" ?????? ????????????
                return true;
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<SharePlaylistMusicRes> getPlayedPlaylist(Long roomId) {
        final String key = playedPrefix + roomId; //room
        ListOperations<String, Object> operations = redisTemplate.opsForList();
        List<Object> roomPlaylist = operations.range(key, 0, -1);
        List<SharePlaylistMusicRes> tempRes = new ArrayList<>();
        if (roomPlaylist != null) {
            convertObjectListToDtoList(roomPlaylist, tempRes);
            List<SharePlaylistMusicRes> response = new ArrayList<>();
            for (SharePlaylistMusicRes res : tempRes) {
                if (!res.getContents().equals("")) response.add(res);
            }
            return response;
        }
        return tempRes;
    }

    @Override
    public List<SharePlaylistMusicRes> getPlaylistUpAndDown(Long roomId) {
        List<SharePlaylistMusicRes> response = new ArrayList<>();
        List<Object> resObjectList = new ArrayList<>();

        ListOperations<String, Object> operations = redisTemplate.opsForList();
        List<Object> playedList = operations.range(playedPrefix + roomId, 0, -1);
        List<Object> requestList = operations.range(musicReqPrefix + roomId, 0, -1);
        try {
            if (playedList != null) {
                if (playedList.size() <= limit)
                    resObjectList.addAll(playedList);
                else resObjectList.addAll(playedList.subList(0, (int) limit));
            }
            SharePlaylistMusic nowPlay = getRedisValue(nowPlayingKey, musicReqPrefix + roomId, SharePlaylistMusic.class);
            if (nowPlay != null) resObjectList.add(nowPlay);
            if (requestList != null) resObjectList.addAll(requestList);
            convertObjectListToDtoList(resObjectList, response);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return response;
    }

    private void convertObjectListToDtoList(List<Object> resObjectList, List<SharePlaylistMusicRes> response) {

        for (Object o : resObjectList) {
            try {
                SharePlaylistMusic play = objectMapper().readValue((String) o, SharePlaylistMusic.class);
                if (play != null) {
                    Optional<Music> reqMusic = musicRepository.findById(play.getMusicId());
                    reqMusic.ifPresent(music -> response.add(makeSharePlaylistMusicRes(play, music)));
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public SharePlaylistMusicRes getPlayedMusicFromPlayList(Long roomId, String playId) {
        ListOperations<String, Object> operations = redisTemplate.opsForList();
        List<Object> roomPlaylist = operations.range(playedPrefix + roomId, 0, -1);
        if (roomPlaylist != null) {
            for (Object o : roomPlaylist) {
                try {
                    SharePlaylistMusic play = objectMapper().readValue((String) o, SharePlaylistMusic.class);
                    if (playId.equals(play.getPlayId())) {
                        Optional<Music> reqMusic = musicRepository.findById(play.getMusicId());
                        if (reqMusic.isPresent()) {
                            return makeSharePlaylistMusicRes(play, reqMusic.get());
                        }
                    }
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    public void deletePlayedMusicFromPlayList(Long roomId, String playId) {
        final String key = playedPrefix + roomId; //room
        deleteMusicFromPlaylist(playId, key);
    }

    @Override
    public void deleteMusicRequestFromPlayList(Long roomId, String playId) {
        String key = musicReqPrefix + roomId; //room
        deleteMusicFromPlaylist(playId, key);
    }

    private void deleteMusicFromPlaylist(String playId, String key) {
        ListOperations<String, Object> operations = redisTemplate.opsForList();
        List<Object> roomPlaylist = operations.range(key, 0, -1);
        if (roomPlaylist != null) {
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
        for (History h : history) {
            response.add(SharePlaylistMusicRes.builder()
                    .playId(h.getId().toString())
                    .title(h.getTitle())
                    .contents(h.getContents())
                    .timestamp(h.getTimestamp())
                    .name(h.getMusic().getName())
                    .thumbnail(h.getMusic().getThumbnail())
                    .album(h.getMusic().getAlbum())
                    .singer(h.getMusic().getSinger())
                    .username(h.getUser().getUsername())
                    .likes(h.getLikes())
                    .build());
        }
        return response;
    }

    @Override
    public SharePlaylistMusicRes getHonoredMusicAndContents(Long playId) {
        History history = historyRepository.getHistoryById(playId);
        if (history != null) {
            return SharePlaylistMusicRes.builder()
                    .playId(history.getId().toString())
                    .title(history.getTitle())
                    .contents(history.getContents())
                    .timestamp(history.getTimestamp())
                    .name(history.getMusic().getName())
                    .thumbnail(history.getMusic().getThumbnail())
                    .album(history.getMusic().getAlbum())
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
    public boolean toggleLikeMusicRequest(Long playId, Long userId) {
        SetOperations<String, Object> setOperations = redisTemplate.opsForSet();
        final String key = musicLikePrefix + playId;
        if (Boolean.TRUE.equals(setOperations.isMember(key, userId))) {
            setOperations.add(key, userId);
            return true;
        } else setOperations.remove(key, userId);
        return false;
    }

    @Override
    public SharePlaylistMusicRes getNowPlayingMusic(long roomId) {
        try {
            SharePlaylistMusic play = getRedisValue(nowPlayingKey, musicReqPrefix + roomId, SharePlaylistMusic.class);
            if (play != null) {
                Optional<Music> reqMusic = musicRepository.findById(play.getMusicId());
                if (reqMusic.isPresent())
                    return makeSharePlaylistMusicRes(play, reqMusic.get());
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private SharePlaylistMusicRes makeSharePlaylistMusicRes(SharePlaylistMusic play, Music music) {
        Long likeCount = redisTemplate.opsForSet().size(musicLikePrefix + play.getPlayId());
        Optional<User> user = userRepository.findById(play.getUserId());
        String username = (user.isPresent()) ? user.get().getUsername() : "Anonymous";
        SharePlaylistMusicRes res = SharePlaylistMusicRes.builder()
                .playId(play.getPlayId())
                .title(play.getTitle())
                .contents(play.getContents())
                .timestamp(play.getTimestamp())
                .name(music.getName())
                .thumbnail(music.getThumbnail())
                .album(music.getAlbum())
                .singer(music.getSinger())
                .username(username)
                .status(play.getStatus())
                .build();
        if (likeCount != null) res.setLikes(likeCount);
        return res;
    }

    // ?????? ??? 12??? ?????? redis??? ?????? ?????? ?????? ?????? ??? ??????????????? 10??? ?????? ????????? ????????? ????????? ??????????????? ????????????.
    @Scheduled(cron = "0 0 0 * * ?")
    @SuppressWarnings("unchecked")
    public void saveMusicAsHistory() {
        ListOperations<String, Object> operations = redisTemplate.opsForList();
        long roomSize = 6;
        for (long i = 1; i <= roomSize; i++) {
            String key = playedPrefix + i;
            List<Object> roomPlayed = operations.range(key, 0, -1);
            if (roomPlayed != null) {
                convertObjectListToHistoryAndSave(i, roomPlayed);
            }
        }
        try {
            redisTemplate.execute((RedisCallback) connection -> {
                connection.flushAll();
                return null;
            });
        } catch (Exception e) {
            log.warn("?????? ????????? ??????????????? ??????????????????.", e);
        }
    }

    private void convertObjectListToHistoryAndSave(Long roomId, List<Object> roomPlayed) {
        long threshold = 10L;
        for (Object o : roomPlayed) {
            SharePlaylistMusic play = objectMapper().convertValue(o, SharePlaylistMusic.class);
            Long likeCount = redisTemplate.opsForSet().size(musicLikePrefix + play.getPlayId());
            Music reqMusic = musicRepository.getMusicById(play.getMusicId());
            User user = userRepository.getById(play.getUserId());
            if (likeCount != null && likeCount > threshold && reqMusic != null) { // ??????????????? ???????????? ?????? ????????????
                History history = new History(roomId, play.getTitle(), play.getContents(), play.getTimestamp(), likeCount);
                history.setMusic(reqMusic);
                history.setUser(user);
                historyRepository.save(history); // DB history table ??????
            }
        }
    }
}