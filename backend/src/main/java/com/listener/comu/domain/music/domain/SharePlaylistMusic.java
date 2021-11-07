package com.listener.comu.domain.music.domain;

import com.listener.comu.domain.music.dto.MusicPlayReq;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SharePlaylistMusic {

    private String contents;
    private Long userId;
    private Long musicId;
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now(); //접수 시간
    @Builder.Default
    private boolean played = false;
}
