package com.listener.comu.domain.music.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
public class SharePlaylistMusicRes {
    private String playId;
    private String title;
    private String contents;
    private LocalDateTime timestamp;
    private String name;
    private String singer;
    private String username;
    @Builder.Default
    private long likes = 0;
}
