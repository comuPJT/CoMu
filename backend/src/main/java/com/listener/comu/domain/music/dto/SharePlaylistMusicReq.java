package com.listener.comu.domain.music.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class SharePlaylistMusicReq {
    @ApiModelProperty(name="사연 제목", example = "안녕하세요!")
    private String title;
    @ApiModelProperty(name="사연 내용", example = "처음 가입해 봤어요! 너무 신기하네요~")
    private String contents;
    @ApiModelProperty(name="사연자 id", example ="1")
    private Long userId;
    @ApiModelProperty(name="신청한 음악 id", example = "1")
    private Long musicId;
}
