package com.listener.comu.domain.music.api;

import com.listener.comu.domain.music.domain.*;
import com.listener.comu.domain.music.dto.NextMusicRes;
import com.listener.comu.domain.music.dto.SharePlaylistMusicReq;
import com.listener.comu.domain.music.dto.SharePlaylistMusicRes;
import com.listener.comu.domain.oauthlogin.api.entity.user.User;
import com.listener.comu.domain.oauthlogin.api.repository.user.UserRepository;
import com.listener.comu.util.S3Uploader;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
                                    .source(musicPlayReq.getSource())
                                    .album(musicPlayReq.getAlbum())
                                    .build();
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
                        .thumbnail(reqMusic.getThumbnail())
                        .album(reqMusic.getAlbum())
                        .singer(reqMusic.getSinger())
                        .username(user.getUsername())
                        .likes(likeCount)
                        .status(play.getStatus())
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
        String nowMusicKey = "nowplaying";
        SharePlaylistMusic nowPlay = objectMapper().convertValue(operations.get(nowMusicKey, "room:" + roomId), SharePlaylistMusic.class);
        if( nowPlay != null ){ // 진행할 신청곡이 있는 경우
            System.out.println("There is music to be played soon..." + nowPlay.getMusicId() + " " + playId);
            if( nowPlay.getPlayId().equals(playId)) {
                // 신청곡이 이전 곡으로, 바뀌지 않은 경우
                System.out.println("Playing music not updated yet...!");
                operations.delete(nowMusicKey, "room:" + roomId);
                ListOperations<String, Object> listOps = redisTemplate.opsForList();
                // 일반 사연으로 옮긴다.
                nowPlay.setStatus(Status.DONE);
                listOps.rightPush(playedPrefix + ":" + roomId, nowPlay);
                // 재생되지 않은 신청곡 리스트에서 새로 값을 얻어와 스트리밍을 시작한다.
                Object next = listOps.leftPop(musicReqPrefix + ":" + roomId);
                nowPlay = objectMapper().convertValue(next, SharePlaylistMusic.class);
                if(nowPlay == null)
                    nowPlay = getRandomMusicObject(roomId, operations, nowMusicKey);
                setNowPlayAndStream(roomId, operations, nowMusicKey, nowPlay);
            }
            if( nowPlay.getStatus().equals(Status.PLAYING)) { // 정상적으로 다음곡을 얻어온 경우
                System.out.println("Music is playing now...!" + nowPlay.getMusicId());
                return NextMusicRes.builder()
                        .playId(nowPlay.getPlayId())
                        .musicId(nowPlay.getMusicId()).build();
            }
            else if(observeFileCreated(roomId, nowPlay.getMusicId())){ // ready이거나 다음곡이 TODO 인 경우
                System.out.println("Getting ready for the music...!" + nowPlay.getMusicId());
                nowPlay.setStatus(Status.PLAYING);
                operations.put(nowMusicKey, "room:"+roomId , nowPlay);
                return NextMusicRes.builder()
                        .playId(nowPlay.getPlayId())
                        .musicId(nowPlay.getMusicId()).build();
            }
        }
        // 신청곡이 없을 때 랜덤한 곡을 틀도록 한다.
        System.out.println("No music in the playlist...!");
        nowPlay = getRandomMusicObject(roomId, operations, nowMusicKey);
        // 지금 재생중인 곡을 리턴한다.
        return NextMusicRes.builder()
                .playId(nowPlay.getPlayId())
                .musicId(nowPlay.getMusicId()).build();
    }

    private SharePlaylistMusic getRandomMusicObject(long roomId, HashOperations<String, Object, Object> operations, String nowMusicKey) {
        SharePlaylistMusic nowPlay;
        long rand = getRandomSongForRoom(roomId); // 방id를 토대로 방에 맞는 랜덤곡을 받아온다.
        nowPlay = SharePlaylistMusic.builder()
                .title("none")
                .contents("")
                .musicId(rand)
                .userId(-1L)
                .build();
        nowPlay.setId();
        setNowPlayAndStream(roomId, operations, nowMusicKey, nowPlay);// 현재 재생으로 옮기고 streaming한다.
        if( observeFileCreated(roomId, nowPlay.getMusicId())){ // 스트리밍 파일이 생성되면
            nowPlay.setStatus(Status.PLAYING); //재생중 상태로 옮긴다.
            operations.put(nowMusicKey, "room:"+roomId , nowPlay);
        }
        return nowPlay;
    }

    private void setNowPlayAndStream(long roomId, HashOperations<String, Object, Object> operations, String nowMusicKey, SharePlaylistMusic nowPlay) {
        nowPlay.setStatus(Status.READY);
        operations.put(nowMusicKey, "room:"+roomId , nowPlay);
        Runtime rt = Runtime.getRuntime();
//        String cmd = "sh stream.sh " + roomId + " " + nowPlay.getMusicId();
        String cmd = "stream.bat " + roomId + " " + nowPlay.getMusicId();
        try {
            System.out.println("Streaming start...");
            rt.exec(cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean observeFileCreated(long roomId, long musicId) {
//        String targetFile ="/tmp/hls/" + roomId + "/" + musicId + ".m3u8";
//        String targetFile = "stream.sh";
        String targetFile = "stream.bat";
        while(true){ // 디렉토리를 모니터링 하다가 파일이 생성되는 시점에 응답주기
            File created = new File(targetFile);
            if(created.isFile()) {
                System.out.println("Streaming file created...!");
                return true; // 파일이 생성되어 스트리밍 가능
            }
        }
    }
    private static long getRandomSongForRoom(long roomId){
        Random rd = new Random();
        return  rd.nextInt(3) + (roomId -1)*3;
    }
}
