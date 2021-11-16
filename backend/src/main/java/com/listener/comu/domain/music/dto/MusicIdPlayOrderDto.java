package com.listener.comu.domain.music.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
// 메인 재생 목록의 음악 순서 변경 시 사용되는 Dto
public class MusicIdPlayOrderDto {

    long musicId;
    int playOrder;

}
