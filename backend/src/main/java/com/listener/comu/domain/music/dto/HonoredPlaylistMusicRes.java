package com.listener.comu.domain.music.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class HonoredPlaylistMusicRes {

    private Long playId;
    private String title;
    private String contents;
    private LocalDateTime timestamp;
    private String name;
    private String singer;
    private String username;
    private Long likes;
}
