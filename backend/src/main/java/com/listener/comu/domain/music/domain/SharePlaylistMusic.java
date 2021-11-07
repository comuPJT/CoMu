package com.listener.comu.domain.music.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class SharePlaylistMusic {

    private String contents;
    private String userId;
    private String musicId;
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now(); //접수 시간
    @Builder.Default
    private String isPlayed = "N";
}
