package com.listener.comu.domain.music.domain;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SharePlaylistMusic {
    @Builder.Default
    private String playId = "";
    private String title;
    private String contents;
    private Long userId;
    private Long musicId;
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now(); //접수 시간

    public void setId(){
        this.playId = this.userId + this.title + this.timestamp;
    }
}
