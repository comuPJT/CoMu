package com.listener.comu.domain.music.domain;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@RedisHash( value = "room" ) // keyspace 최종키가 저장되는 형태 -> room:[id]
public class Room implements Serializable {

    @Id
    private String id; //Long이 지원된다면 Long으로 바꾸자 -
    private static List<Object> roomSharedPlaylist;

    @Builder
    public Room(String id){
        this.id = id;
        roomSharedPlaylist = new ArrayList<>();
    }

    public void addToSharePlaylist(SharePlaylistMusic sharePlaylist) {
        roomSharedPlaylist.add(sharePlaylist);
    }
}
