package com.listener.comu.domain.music.domain;

import lombok.*;

import java.time.LocalDateTime;

@Setter
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
    @Builder.Default
    private Status status = Status.TODO;

    public void setId(){
        this.playId = this.userId + this.title + this.timestamp;
    }
}
