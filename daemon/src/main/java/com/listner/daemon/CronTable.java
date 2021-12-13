package com.listner.daemon;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.listner.daemon.api.StreamingService;
import com.listner.daemon.domain.Music;
import com.listner.daemon.domain.MusicRepository;
import com.listner.daemon.domain.SharePlaylistMusic;
import com.listner.daemon.domain.Status;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.io.File;
import java.util.Random;

import static com.listner.daemon.config.RedisConfig.objectMapper;

@Component
public class CronTable {

    private final RedisTemplate<String,Object> redisTemplate;
    private final MusicRepository musicRepository;
    private final StreamingService streamingService;
    private final static String musicReqPrefix = "room:";

    public CronTable(RedisTemplate<String, Object> redisTemplate, MusicRepository musicRepository, StreamingService streamingService) {
        this.redisTemplate = redisTemplate;
        this.musicRepository = musicRepository;
        this.streamingService = streamingService;
    }

    // 10초 마다 재생중인 목록을 모니터링하며 사연 스트리밍 스케줄링
    @Scheduled(cron="*/10 * * * * *")
    public void scheduleLiveStream() throws JsonProcessingException {
        HashOperations<String, String, String> hashOps = redisTemplate.opsForHash();
        ListOperations<String, Object> listOps = redisTemplate.opsForList();
        String musicName;
        for(long i = 1 ; i <= 6 ; i++){
            String nowPlayingKey = "nowplaying:";
            SharePlaylistMusic nowPlay = getRedisValue(nowPlayingKey, musicReqPrefix + i, SharePlaylistMusic.class);
            if( nowPlay == null ){ //재생곡이 없는 경우
                //  재생되지 않은 신청곡 리스트에서 새로 값을 얻어와 스트리밍을 시작한다.
                Object next = listOps.leftPop(musicReqPrefix + i);
                nowPlay = objectMapper().readValue((String)next, SharePlaylistMusic.class);
                if( nowPlay == null ) {// 신청곡이 하나도 없다면 랜덤으로 얻어오기
                    nowPlay = getRandomMusicObject(i, hashOps, nowPlayingKey);
                    musicName = nowPlay.getTitle();
                }
                else { // 신청곡이 있는 경우
                    Music music = musicRepository.getMusicById(nowPlay.getMusicId());
                    musicName = music.getSpotifyId();
                    if (music.getOnCloud() == 0) { // 신청곡이 있지만 음원이 없어 랜덤재생을 해야하는 경우
                        listOps.leftPush(musicReqPrefix + i, nowPlay); //꺼낸항목 다시 넣기
                        streamingService.executeDownloadAndUploadToS3(music);
                        nowPlay = getRandomMusicObject(i, hashOps, nowPlayingKey);
                        musicName = nowPlay.getTitle();
                    }
                }
                streamingService.executeStreamingShell(i, listOps, hashOps, musicName, nowPlayingKey, nowPlay);// 현재 재생으로 옮기고 streaming한다.
                observeFileCreated(i, musicName, hashOps, nowPlayingKey, nowPlay);
            }
        }
    }

    private SharePlaylistMusic getRandomMusicObject(long roomId, HashOperations<String, String, String> operations, String nowMusicKey) {
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
        try {
            operations.put(nowMusicKey, musicReqPrefix + roomId, objectMapper().writeValueAsString(nowPlay));
        } catch (JsonProcessingException e) {
            System.out.println("FAILED :Save Java Object as Redis String");
            e.printStackTrace();
        }
        return nowPlay;
    }
    private static long getRandomSongForRoom(long roomId){
        Random rd = new Random();
        return  rd.nextInt(3) + (roomId-1)*3 + 1;
    }
    private static void observeFileCreated(long roomId, String musicName, HashOperations<String, String, String> operations,String nowMusicKey, SharePlaylistMusic nowPlay) throws JsonProcessingException {
//        String targetFile = musicName + ".mp4"; // mac or window
        String targetFile = musicName + ".mp4"; // EC2 docker
        while(true){ // 디렉토리를 모니터링 하다가 파일이 생성되는 시점에 응답주기
            File created = new File(targetFile);
            if(created.isFile()) {
                nowPlay.setStatus(Status.PLAYING);
                operations.put(nowMusicKey, musicReqPrefix + roomId, objectMapper().writeValueAsString(nowPlay));
                System.out.println("Streaming file created...!");
                return ; // 파일이 생성되어 스트리밍 가능
            }
        }
    }
    public <T> T getRedisValue(String key, String hasyKey, Class<T> classType) throws JsonProcessingException {
        String redisValue = (String) redisTemplate.opsForHash().get(key, hasyKey);
        if (ObjectUtils.isEmpty(redisValue)) {
            return null;
        }else{
            return objectMapper().readValue(redisValue,classType);
        }
    }
}
