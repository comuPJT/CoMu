package com.listener.comu.domain.music.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class NextMusicRes {
    @ApiModelProperty(name="재생id")
    String playId;
    @ApiModelProperty(name="음악Id")
    long musicId;
}
