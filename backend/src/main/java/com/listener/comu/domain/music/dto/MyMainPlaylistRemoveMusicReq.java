package com.listener.comu.domain.music.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MyMainPlaylistRemoveMusicReq {

    private long userSeq;
    private List<Long> musicIds;

}
