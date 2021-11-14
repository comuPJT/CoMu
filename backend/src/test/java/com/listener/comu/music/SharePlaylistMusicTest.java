package com.listener.comu.music;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.listener.comu.domain.music.domain.*;
import com.listener.comu.domain.music.dto.SharePlaylistMusicRes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.listener.comu.config.RedisConfig.objectMapper;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class SharePlaylistMusicTest {
    @Autowired
    private RoomRedisRepository repo;

//    @Autowired
//    private StringRedisTemplate redisTemplate;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Autowired
    private HistoryRepository historyRepository;

    @Test
    public void RedisRepositoryRoomCreateTest(){
        // given
        SharePlaylistMusic play = SharePlaylistMusic.builder()
                .contents("안녕하세용 처음 가입해봤어요")
                .musicId(1L)
                .userId(1L)
                .build();
        Room room = new Room("4");
        room.addToSharePlaylist(play);

        // when
        Room savedRoom = repo.save(room);

        // then
        Optional<Room> findRoom = repo.findById(savedRoom.getId());

        assertThat(findRoom.isPresent()).isEqualTo(Boolean.TRUE);
    }

    @Test
    public void RedisTemplateRedisListTest() throws JsonProcessingException {
        final String key = "roomtest:1" ; //room
        ListOperations<String, Object> operations = redisTemplate.opsForList();
        SharePlaylistMusic play = SharePlaylistMusic.builder()
                .title("처음입네다")
                .contents("안녕하세용 처음 가입해봤어요")
                .musicId(1L)
                .userId(1L)
                .build();
        play.setId();
        operations.rightPush(key, play);
        List<Object> playList = operations.range(key,0,-1);

        int size = playList.size() ;
        SharePlaylistMusic play2 = objectMapper().convertValue(playList.get(size-1), SharePlaylistMusic.class);

        assertThat(play2.getMusicId()).isEqualTo(1L);
    }

    @Test
    public void RedisTemplateSetTest() {
        SetOperations<String,Object> setOperations = redisTemplate.opsForSet();
        String key = "testsuite";
        Long size = setOperations.size(key);

        if( !setOperations.isMember(key, 1L)) {
            setOperations.add(key, 1L);
            assertThat(size + 1).isEqualTo(setOperations.size(key));
        }
        else {
            setOperations.remove(key,1L);
            assertThat(size - 1).isEqualTo(setOperations.size(key));
        }
    }

    @Test
    public void deleteHonoredPlayTest() {
        assertThat(historyRepository.getHistoryById(1L).getLikes()).isEqualTo(20);
        historyRepository.deleteById(1L);
        assertThat(historyRepository.getHistoryById(1L)).isEqualTo(null);
    }
    @Test
    public void getHonoredPlayListTest() {
        History history = historyRepository.getHistoryById(1L);
        assertThat(history.getMusic().getSinger()).isEqualTo("조성모");
    }

    @Test
    public void getAllHonoredPlayListInRoom() {
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
        assertThat(response.size()).isEqualTo(2);
    }

    @Test
    public void getMusicRequestAndDownload(){
        Music music = Music.builder().singer("박재범")
                .name("Me Like Yuh")
                .thumbnail("https://i.scdn.co/image/ab67616d0000b27382ecc5ea89bf34479a71a297")
                .source("https://www.youtube.com/watch?v=9dIVOtRtBb8&list=RDLvmbLw7qfI0&index=2")
                .spotifyId("3SWju8HQ6II7QXkWtFSDE1")
                .build();
        String cmd = "youtube-dl -f 18 -o " + music.getId() + ".%(ext)s " + music.getSource();
        Runtime rt = Runtime.getRuntime();
        try {
            Process pr = rt.exec(cmd);
            pr.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
