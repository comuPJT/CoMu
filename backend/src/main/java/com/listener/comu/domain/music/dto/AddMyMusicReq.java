package com.listener.comu.domain.music.dto;

import com.listener.comu.domain.music.domain.Music;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddMyMusicReq {

    private List<Music> musicList;
    private long userSeq;

}
