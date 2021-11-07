package com.listener.comu.domain.music.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class SharePlaylistMusicRes {
    private String playId;
    private String title;
    private String contents;
    private LocalDateTime timestamp;
    private String name;
    private String singer;
    private String username;
}
