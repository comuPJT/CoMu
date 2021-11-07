package com.listener.comu.domain.music.dto;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class MusicPlayReq {
    private String contents;
    private Long userId;
    private Long musicId;
}
