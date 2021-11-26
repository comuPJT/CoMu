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
    @ApiModelProperty(name="사연자 고유 시퀀스", example ="1")
    private Long userId;
    @ApiModelProperty(name="신청한 음악의 스포티파이 Id", example = "3FeVmId7tL5YN8B7R3imoM")
    private String spotifyId;
    @ApiModelProperty(name="신청곡의 썸네일 이미지 Url", example = "https://i.scdn.co/image/ab67616d0000b273f60a9b7e2abafc38da31f575")
    private String thumbnail;
    @ApiModelProperty(name="신청곡 이름", example = "My universe")
    private String name;
    @ApiModelProperty(name="가수 이름", example = "BTS, Coldplay")
    private String singer;
    @ApiModelProperty(name="유튜브 소스", example = "https://www.youtube.com/watch?v=3YqPKLZF_WU")
    private String source;
    @ApiModelProperty(name = "앨범명", example = "화양연화")
    private String album;
}
