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
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.Optional;

import static com.listener.comu.config.RedisConfig.objectMapper;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class SharePlaylistMusicTest {
    @Autowired
    private RoomRedisRepository repo;

//    @Autowired
//    private StringRedisTemplate redisTemplate;

    @Autowired
    private RedisTemplate<String,SharePlaylistMusic> redisTemplate;


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
        ListOperations<String, SharePlaylistMusic> operations = redisTemplate.opsForList();
        SharePlaylistMusic play = SharePlaylistMusic.builder()
                .contents("안녕하세용 처음 가입해봤어요")
                .musicId(1L)
                .userId(1L)
                .build();
        operations.rightPush(key, play);
        List<SharePlaylistMusic> playList = operations.range(key,0,-1);
        SharePlaylistMusic play2 = objectMapper().convertValue(playList.get(0), SharePlaylistMusic.class);

        assertThat(play2.getMusicId()).isEqualTo(1L);

    }
}
