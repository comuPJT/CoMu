package com.listener.comu.domain.music.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MyPlaylistRequest {

    private long userSeq;
    private String name;

}
