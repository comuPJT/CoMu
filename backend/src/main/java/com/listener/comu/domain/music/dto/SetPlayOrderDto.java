package com.listener.comu.domain.music.dto;

import com.listener.comu.domain.music.domain.MyMainPlaylist;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SetPlayOrderDto {

    List<MyMainPlaylist> myMainPlaylistList;
    long userSeq;

}
