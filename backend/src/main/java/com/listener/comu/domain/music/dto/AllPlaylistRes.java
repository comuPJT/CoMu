package com.listener.comu.domain.music.dto;

import com.listener.comu.domain.music.domain.Myplaylist;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AllPlaylistRes {

    Myplaylist myplaylist;
    List<String> thumbnails;

}
