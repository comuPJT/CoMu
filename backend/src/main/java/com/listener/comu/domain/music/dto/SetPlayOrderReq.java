package com.listener.comu.domain.music.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
// 메인 재생 목록의 음악 순서 변경 시 요청 body를 담는 Dto
public class SetPlayOrderReq {

    long userSeq;
    List<MusicIdPlayOrderDto> musicIdPlayOrderDtoList;

}
