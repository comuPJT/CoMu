package com.listener.comu.music;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.listener.comu.domain.music.domain.Room;
import com.listener.comu.domain.music.domain.RoomRedisRepository;
import com.listener.comu.domain.music.domain.SharePlaylistMusic;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class SharePlaylistMusicTest {
    @Autowired
    private RoomRedisRepository repo;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    public void RoomCreateTest(){
        // given
        SharePlaylistMusic play = SharePlaylistMusic.builder()
                .contents("안녕하세용 처음 가입해봤어요")
                .musicId("1")
                .userId("1")
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
    public void redisListTest() throws JsonProcessingException {
        final String key = "roomtest:1"; //room
        final ListOperations<String, String> stringStringListOperations = redisTemplate.opsForList();
        SharePlaylistMusic play = SharePlaylistMusic.builder()
                .contents("안녕하세용 처음 가입해봤어요")
                .musicId("1")
                .userId("1")
                .build();

        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // timestamp 형식 안따르도록 설정
        mapper.registerModules(new JavaTimeModule(), new Jdk8Module()); // LocalDateTime 매핑을 위해 모듈 활성화
        String jsonInString = mapper.writeValueAsString(play);
        stringStringListOperations.rightPush(key,jsonInString);

    }
}
