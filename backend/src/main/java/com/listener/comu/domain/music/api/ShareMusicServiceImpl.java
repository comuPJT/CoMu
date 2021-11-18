package com.listener.comu.domain.music.api;

import com.listener.comu.domain.music.domain.*;
import com.listener.comu.domain.music.dto.NextMusicRes;
import com.listener.comu.domain.music.dto.SharePlaylistMusicReq;
import com.listener.comu.domain.music.dto.SharePlaylistMusicRes;
import com.listener.comu.domain.oauthlogin.api.entity.user.User;
import com.listener.comu.domain.oauthlogin.api.repository.user.UserRepository;
import org.springframework.data.redis.core.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

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
    final StreamingService streamingService;

    ShareMusicServiceImpl(RedisTemplate<String, Object> redisTemplate, MusicRepository musicRepository, UserRepository userRepository, HistoryRepository historyRepository, StreamingService streamingService) {
        this.redisTemplate = redisTemplate;
        this.musicRepository = musicRepository;
        this.userRepository = userRepository;
        this.historyRepository = historyRepository;
        this.streamingService = streamingService;
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
                                    .source(musicPlayReq.getSource())
                                    .album(musicPlayReq.getAlbum())
                                    .onCloud(0)
                                    .build();
                musicRepository.save(music);
                // 음악 다운로드 -> s3 업로딩
                streamingService.executeDownloadAndUploadToS3(music);
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
        HashOperations<String, Object, Object> nowplayoperation = redisTemplate.opsForHash();
        SharePlaylistMusic nowPlay = objectMapper().convertValue(nowplayoperation.get("nowplaying", "room:" + roomId), SharePlaylistMusic.class);
        List<Object> playedList = operations.range(playedKey, 0,-1);
        List<Object> requestList = operations.range(reqKey, 0 ,-1);
        List<SharePlaylistMusicRes> response = new ArrayList<>();
        if( playedList != null ) {
            if( playedList.size() <= limit)
                resObjectList.addAll(playedList);
            else resObjectList.addAll(playedList.subList(0, (int)limit));
        }
        if( nowPlay != null) resObjectList.add(nowPlay);
        if( requestList != null ) resObjectList.addAll(requestList);
        convertObjectListToDtoList(resObjectList, response);
        return response;
    }

    private void convertObjectListToDtoList(List<Object> resObjectList, List<SharePlaylistMusicRes> response) {
        for (Object o : resObjectList) {
            SharePlaylistMusic play = objectMapper().convertValue(o, SharePlaylistMusic.class);
            Long likeCount = redisTemplate.opsForSet().size( musicLikePrefix + ":" + play.getPlayId());
            Optional<Music> reqMusic = musicRepository.findById(play.getMusicId());
            Optional<User> user = userRepository.findById(play.getUserId());
            String username = ( user.isPresent())? user.get().getUsername() : "Anonymous";
            if (likeCount != null && reqMusic.isPresent()) {
                Music music = reqMusic.get();
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
                        .likes(likeCount)
                        .status(play.getStatus())
                        .build();
                response.add(res);
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
                                .thumbnail(reqMusic.getThumbnail())
                                .album(reqMusic.getAlbum())
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
    public SharePlaylistMusicRes HonoredMusicAndContents(Long playId) {
        History history = historyRepository.getHistoryById(playId);
        if( history != null) {
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
    public void toggleLikeMusicRequest(Long playId, Long userId ) {
        SetOperations<String,Object> setOperations = redisTemplate.opsForSet();
        final String key = musicLikePrefix + ":" + playId;
        if (Boolean.TRUE.equals(setOperations.isMember(key, userId))) setOperations.add(key, userId);
        else setOperations.remove(key, userId);
    }

    @Override
    public NextMusicRes getNextMusic(long roomId, String playId) {
        HashOperations<String, Object, Object> operations = redisTemplate.opsForHash();
        ListOperations<String, Object> listOps = redisTemplate.opsForList();
        String nowMusicKey = "nowplaying";
        String musicName;
        SharePlaylistMusic nowPlay = objectMapper().convertValue(operations.get(nowMusicKey, "room:" + roomId), SharePlaylistMusic.class);
        // 진행할 신청곡이 있는 경우
        if( nowPlay != null ){
            System.out.println("There is music to be played soon..." + playId);
            musicName = musicRepository.getMusicById(nowPlay.getMusicId()).getSpotifyId();
            if ( nowPlay.getStatus().equals(Status.TODO) || nowPlay.getStatus().equals(Status.READY)) { // ready이거나 다음곡이 todo인 경우 play될때까지 기다린 후 반환
                observeFileCreated(roomId, operations, nowMusicKey, nowPlay);
                System.out.println("Getting ready for the music...!");
                nowPlay.setStatus(Status.PLAYING);
                operations.put(nowMusicKey, "room:"+roomId , nowPlay);
            }
            else { // 정상적으로 다음곡을 얻어온 경우
                System.out.println("Music is playing now...!");
            }
            return NextMusicRes.builder()
                    .playId(nowPlay.getPlayId())
                    .musicName(musicName).build();
        }
        // 신청곡이 없을 때 - 재생목록에서 가져오거나, 랜덤한 곡을 틀기.
        System.out.println("There is no playing music...!");
        //  재생되지 않은 신청곡 리스트에서 새로 값을 얻어와 스트리밍을 시작한다.
        Object next = listOps.leftPop(musicReqPrefix + ":" + roomId);
        nowPlay = objectMapper().convertValue(next, SharePlaylistMusic.class);
        if(nowPlay == null ) {// 신청곡이 하나도 없다면 랜덤으로 얻어오기
            nowPlay = getRandomMusicObject(roomId, operations, nowMusicKey);
            musicName = nowPlay.getTitle();
        }
        else {
            Music music = musicRepository.getMusicById(nowPlay.getMusicId());
            musicName = music.getSpotifyId();
            if (music.getOnCloud() == 0) { // 신청곡이 있지만 음원이 없어 랜덤재생을 해야하는 경우
                listOps.leftPush(musicReqPrefix + ":" + roomId, nowPlay); //꺼낸항목 다시 넣기
                nowPlay = getRandomMusicObject(roomId, operations, nowMusicKey);
                musicName = nowPlay.getTitle();
            }
        }
        streamingService.executeStreamingShell(roomId, listOps, operations, musicName, nowMusicKey, nowPlay);// 현재 재생으로 옮기고 streaming한다.
        observeFileCreated(roomId, operations, nowMusicKey, nowPlay);
        return NextMusicRes.builder()
                .playId("Anonymous")
                .musicName(nowPlay.getTitle()).build();
    }

    private SharePlaylistMusic getRandomMusicObject(long roomId, HashOperations<String, Object, Object> operations, String nowMusicKey) {
        long rand = getRandomSongForRoom(roomId); // 방id를 토대로 방에 맞는 랜덤곡을 받아온다.
        SharePlaylistMusic nowPlay = SharePlaylistMusic.builder()
                .title("")
                .contents("")
                .playId("Anonymous")
                .musicId(rand)
                .userId(-1L)
                .build();
        String musicName = musicRepository.getMusicById(rand).getSpotifyId();
        nowPlay.setTitle(musicName);
        operations.put(nowMusicKey, "room:" + roomId, nowPlay);
        return nowPlay;
    }
    private static void observeFileCreated(long roomId, HashOperations<String, Object, Object> operations,String nowMusicKey, SharePlaylistMusic nowPlay) {
//        String targetFile ="/tmp/hls/" + roomId + "/" + "music.m3u8";
        String targetFile = "stream.sh";
//        String targetFile = "stream.bat";
        while(true){ // 디렉토리를 모니터링 하다가 파일이 생성되는 시점에 응답주기
            File created = new File(targetFile);
            if(created.isFile()) {
                nowPlay.setStatus(Status.PLAYING);
                operations.put(nowMusicKey, "room:"+roomId, nowPlay);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Streaming file created...!");
                return ; // 파일이 생성되어 스트리밍 가능
            }
        }
    }
    private static long getRandomSongForRoom(long roomId){
        Random rd = new Random();
        return  rd.nextInt(3) + (roomId-1)*3;
    }
    // 매일 밤 12시 마다 redis에 있던 일반 사연 목록 중 좋아요수가 10이 넘는 사연을 명예의 전당에 영구적으로 저장한다.
    @Scheduled(cron = "0 0 0 * * *")
    @Override
    public void saveMusicAsHistory() {
        ListOperations<String, Object> operations = redisTemplate.opsForList();
        long roomSize = 6;
        for(long i = 0; i < roomSize; i++){
            String key = playedPrefix + ":" + i;
            List<Object> roomPlayed = operations.range(key, 0,-1);
            if(roomPlayed != null) {
                convertObjectListToHistoryAndSave(i, roomPlayed);
            }
        }

    }

    private void convertObjectListToHistoryAndSave(Long roomId, List<Object> roomPlayed) {
        long threshold = 10L;
        for (Object o : roomPlayed) {
            SharePlaylistMusic play = objectMapper().convertValue(o, SharePlaylistMusic.class);
            Long likeCount = redisTemplate.opsForSet().size(musicLikePrefix + ":" + play.getPlayId());
            Music reqMusic = musicRepository.getMusicById(play.getMusicId());
            User user = userRepository.getById(play.getUserId());
            if (likeCount != null && likeCount > threshold && reqMusic != null) { // 좋아요수가 기준치를 넘은 경우에만
                History history = new History(roomId, play.getTitle(), play.getContents(), play.getTimestamp(), likeCount);
                history.setMusic(reqMusic);
                history.setUser(user);
                historyRepository.save(history); // DB history table 저장
            }
        }
    }
}
