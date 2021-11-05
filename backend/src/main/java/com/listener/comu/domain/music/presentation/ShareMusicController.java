package com.listener.comu.domain.music.presentation;

import com.listener.comu.domain.music.api.ShareMusicService;
import com.listener.comu.domain.music.dto.BaseResponseBody;
import com.listener.comu.domain.music.dto.MusicPlayReq;
import com.listener.comu.domain.music.dto.PlayedMusicRes;
import com.listener.comu.domain.music.dto.SearchMusicRes;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 공용 음악 관련 API 요청 처리를 위한 컨트롤러 정의.
 */
@Api(value = "공용 음악 API", tags = {"ShareMusic"})
@RestController
@RequestMapping("/api/share")
public class ShareMusicController {

    private final ShareMusicService shareMusicService;

    public ShareMusicController(ShareMusicService shareMusicService) {
        this.shareMusicService = shareMusicService;
    }

    @GetMapping("/search")
    @ApiOperation(value = "spotify 음악 검색", notes = "user로부터 keyword를 받아와 spotify api 제목, 아티스트 검색 결과를 리스트로 돌려준다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공", response = SearchMusicRes.class),
            @ApiResponse(code = 401, message = "인증 실패", response = BaseResponseBody.class),
            @ApiResponse(code = 404, message = "사용자 없음", response = BaseResponseBody.class),
            @ApiResponse(code = 500, message = "서버 오류", response = BaseResponseBody.class)
    })
    public ResponseEntity<List<SearchMusicRes>> findMusicByKeyword(@RequestParam String keyword) {
        return ResponseEntity.status(200).body(shareMusicService.findMusicByQuery(keyword));
    }

    @PostMapping("/{roomId}")
    @ApiOperation(value = "곡/사연 신청", notes = "user로부터 신청곡, 사연을 받아와 접속한 방의 재생리스트에 넣는다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공", response =  BaseResponseBody.class),
            @ApiResponse(code = 401, message = "인증 실패", response = BaseResponseBody.class),
            @ApiResponse(code = 404, message = "사용자 없음", response = BaseResponseBody.class),
            @ApiResponse(code = 500, message = "서버 오류", response = BaseResponseBody.class)
    })
    public ResponseEntity<? extends BaseResponseBody> addMusicToPlayList(@PathVariable Long roomId, @RequestBody MusicPlayReq musicPlayReq) {
        shareMusicService.addMusicToPlayList(roomId, musicPlayReq);
        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "Success"));
    }

    @DeleteMapping ("/{roomId}/music/{playId}")
    @ApiOperation(value = "신청곡 삭제", notes = "특정방에 있는 user가 선택한 노래를 삭제한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공", response = BaseResponseBody.class),
            @ApiResponse(code = 401, message = "인증 실패", response = BaseResponseBody.class),
            @ApiResponse(code = 404, message = "사용자 없음", response = BaseResponseBody.class),
            @ApiResponse(code = 500, message = "서버 오류", response = BaseResponseBody.class)
    })
    public ResponseEntity<? extends BaseResponseBody> deleteMusicFromPlayList(@PathVariable Long roomId, @PathVariable Long playId) {
        shareMusicService.deleteMusicFromPlayList(roomId, playId);
        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "Success"));
    }
    //명예의 전당과 사연 조회는 유저 인증 필요 없음
    @GetMapping("/{roomId}/story")
    @ApiOperation(value = "일반 사연 목록 조회", notes = "하루 내에 접수되어 재생된 신청곡과 사연을 조회한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공", response = PlayedMusicRes.class),
            @ApiResponse(code = 401, message = "인증 실패", response = BaseResponseBody.class),
            @ApiResponse(code = 404, message = "사용자 없음", response = BaseResponseBody.class),
            @ApiResponse(code = 500, message = "서버 오류", response = BaseResponseBody.class)
    })
    public ResponseEntity<List<PlayedMusicRes>> getPlayedMusicAndContent (@PathVariable Long roomId) {
        return ResponseEntity.status(200).body(shareMusicService.getPlayedMusicAndContent(roomId));
    }

    @GetMapping("/{roomId}/honor")
    @ApiOperation(value = "명예의 전당 사연 목록 조회", notes = "그동안 재생된 신청곡, 사연들 중 일정 좋아요 갯수를 받은 명예의 전당 내역을 조회한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공", response = PlayedMusicRes.class),
            @ApiResponse(code = 401, message = "인증 실패", response = BaseResponseBody.class),
            @ApiResponse(code = 404, message = "사용자 없음", response = BaseResponseBody.class),
            @ApiResponse(code = 500, message = "서버 오류", response = BaseResponseBody.class)
    })
    public ResponseEntity<List<PlayedMusicRes>> getHonoredMusicAndContent (@PathVariable Long roomId) {
        return ResponseEntity.status(200).body(shareMusicService.getHonoredMusicAndContent(roomId));
    }

    // TO DO - 좋아요 부분
    @GetMapping("/like/{playId}/user/{userId}")
    @ApiOperation(value = "좋아요", notes = "유저가 '좋아요'를 누른 신청곡, 사연에 대해 처리한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공", response = BaseResponseBody.class),
            @ApiResponse(code = 401, message = "인증 실패", response = BaseResponseBody.class),
            @ApiResponse(code = 404, message = "사용자 없음", response = BaseResponseBody.class),
            @ApiResponse(code = 500, message = "서버 오류", response = BaseResponseBody.class)
    })
    public ResponseEntity<? extends BaseResponseBody> likeMusicRequest (@PathVariable Long playId, @PathVariable Long userId) {
        shareMusicService.likeMusicRequest(playId, userId);
        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "Success"));
    }

    @DeleteMapping("/like/{playId}/user/{userId}")
    @ApiOperation(value = "좋아요 취소", notes = "유저가 '좋아요'를 누른 신청곡, 사연에 대해 '좋아요'를 취소한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공", response = BaseResponseBody.class),
            @ApiResponse(code = 401, message = "인증 실패", response = BaseResponseBody.class),
            @ApiResponse(code = 404, message = "사용자 없음", response = BaseResponseBody.class),
            @ApiResponse(code = 500, message = "서버 오류", response = BaseResponseBody.class)
    })
    public ResponseEntity<? extends BaseResponseBody> undoLikeMusicRequest (@PathVariable Long playId, @PathVariable Long userId) {
        shareMusicService.undoLikeMusicRequest(playId, userId);
        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "Success"));
    }
}
