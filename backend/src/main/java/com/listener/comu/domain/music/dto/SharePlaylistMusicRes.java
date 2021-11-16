package com.listener.comu.domain.music.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@ApiModel("SharePlaylistMusicResponse")
public class SharePlaylistMusicRes {
    @ApiModelProperty(name="재생번호")
    private String playId;
    @ApiModelProperty(name="사연 제목")
    private String title;
    @ApiModelProperty(name="사연 내용")
    private String contents;
    @ApiModelProperty(name="사연 등록 일시" )
    private LocalDateTime timestamp;
    @ApiModelProperty(name="노래 제목")
    private String name;
    @ApiModelProperty(name="가수")
    private String singer;
    @ApiModelProperty(name="사연자 닉네임")
    private String username;
    @ApiModelProperty(name="좋아요 수")
    @Builder.Default
    private long likes = 0;
}
