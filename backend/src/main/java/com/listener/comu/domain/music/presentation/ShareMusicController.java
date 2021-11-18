package com.listener.comu.domain.music.presentation;

import com.listener.comu.domain.music.api.ShareMusicService;
import com.listener.comu.domain.music.dto.BaseResponseBody;
import com.listener.comu.domain.music.dto.SharePlaylistMusicReq;
import com.listener.comu.domain.music.dto.SharePlaylistMusicRes;
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
@CrossOrigin("*")
@RequestMapping("/api/share")
public class ShareMusicController {

    private final ShareMusicService shareMusicService;

    public ShareMusicController(ShareMusicService shareMusicService) {
        this.shareMusicService = shareMusicService;
    }

    @GetMapping("/{roomId}")
    @ApiOperation(value = "신청곡/사연 목록 조회", notes = "현재 재생되는 곡의 이전곡 최대 15곡, 진행될 곡 최대 15곡을 반환한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공", response = SharePlaylistMusicRes.class),
            @ApiResponse(code = 401, message = "인증 실패", response = BaseResponseBody.class),
            @ApiResponse(code = 404, message = "사용자 없음", response = BaseResponseBody.class),
            @ApiResponse(code = 500, message = "서버 오류", response = BaseResponseBody.class)
    })
    public ResponseEntity<List<SharePlaylistMusicRes>> getCurrentPlayList (@PathVariable Long roomId) {
        return ResponseEntity.status(200).body(shareMusicService.getPlaylistUpAndDown(roomId));
    }

    @PostMapping("/{roomId}")
    @ApiOperation(value = "곡/사연 신청", notes = "user로부터 신청곡, 사연을 받아와 접속한 방의 재생리스트에 넣는다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공", response =  BaseResponseBody.class),
            @ApiResponse(code = 401, message = "인증 실패", response = BaseResponseBody.class),
            @ApiResponse(code = 404, message = "사용자 없음", response = BaseResponseBody.class),
            @ApiResponse(code = 500, message = "서버 오류", response = BaseResponseBody.class)
    })
    public ResponseEntity<? extends BaseResponseBody> addMusicToPlayList(@PathVariable Long roomId, @RequestBody SharePlaylistMusicReq musicPlayReq) {
        if( shareMusicService.addMusicToPlayList(roomId, musicPlayReq))
            return ResponseEntity.status(200).body(BaseResponseBody.of(200, "Success"));
        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "Failed : 15개를 초과했거나 DB접근에 실패했습니다."));
    }

    @DeleteMapping ("/{roomId}/music/{playId}")
    @ApiOperation(value = "신청곡 삭제", notes = "특정방에 있는 아직 Play되지 않은 user가 선택한 노래를 삭제한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공", response = BaseResponseBody.class),
            @ApiResponse(code = 401, message = "인증 실패", response = BaseResponseBody.class),
            @ApiResponse(code = 404, message = "사용자 없음", response = BaseResponseBody.class),
            @ApiResponse(code = 500, message = "서버 오류", response = BaseResponseBody.class)
    })
    public ResponseEntity<? extends BaseResponseBody> deleteMusicFromPlayList(@PathVariable Long roomId, @PathVariable String playId) {
        shareMusicService.deleteMusicRequestFromPlayList(roomId, playId);
        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "Success"));
    }

    @GetMapping("/{roomId}/story")
    @ApiOperation(value = "일반 사연 목록 조회", notes = "하루 내에 접수되어 재생된 신청곡과 사연을 조회한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공", response = SharePlaylistMusicRes.class),
            @ApiResponse(code = 401, message = "인증 실패", response = BaseResponseBody.class),
            @ApiResponse(code = 404, message = "사용자 없음", response = BaseResponseBody.class),
            @ApiResponse(code = 500, message = "서버 오류", response = BaseResponseBody.class)
    })
    public ResponseEntity<List<SharePlaylistMusicRes>> getPlayedPlaylist(@PathVariable Long roomId) {
        return ResponseEntity.status(200).body(shareMusicService.getPlayedPlaylist(roomId));
    }

    @GetMapping("/{roomId}/story/{storyId}")
    @ApiOperation(value = "일반 사연 상세 조회", notes = "하루 내에 접수되어 재생된 신청곡과 사연의 상세내역을 조회한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공", response = SharePlaylistMusicRes.class),
            @ApiResponse(code = 401, message = "인증 실패", response = BaseResponseBody.class),
            @ApiResponse(code = 404, message = "사용자 없음", response = BaseResponseBody.class),
            @ApiResponse(code = 500, message = "서버 오류", response = BaseResponseBody.class)
    })
    public ResponseEntity<SharePlaylistMusicRes> getPlayedMusicAndContent (@PathVariable Long roomId, @PathVariable String storyId) {
        return ResponseEntity.status(200).body(shareMusicService.getPlayedMusicFromPlayList(roomId, storyId));
    }

    @DeleteMapping("/{roomId}/story/{storyId}")
    @ApiOperation(value = "일반 사연 상세 삭제", notes = "하루동안 접수되어 재생된 신청곡과 사연을 삭제한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공", response = SharePlaylistMusicRes.class),
            @ApiResponse(code = 401, message = "인증 실패", response = BaseResponseBody.class),
            @ApiResponse(code = 404, message = "사용자 없음", response = BaseResponseBody.class),
            @ApiResponse(code = 500, message = "서버 오류", response = BaseResponseBody.class)
    })
    public ResponseEntity<? extends BaseResponseBody> deletePlayedMusicAndContent (@PathVariable Long roomId, @PathVariable String storyId) {
        shareMusicService.deletePlayedMusicFromPlayList(roomId, storyId);
        return ResponseEntity.status(200).body(BaseResponseBody.of(200,"Success"));
    }

    @GetMapping("/honor")
    @ApiOperation(value = "명예의 전당 사연 목록 조회", notes = "그동안 재생된 신청곡, 사연들 중 일정 좋아요 갯수를 받은 명예의 전당 내역을 조회한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공", response = SharePlaylistMusicRes.class),
            @ApiResponse(code = 401, message = "인증 실패", response = BaseResponseBody.class),
            @ApiResponse(code = 404, message = "사용자 없음", response = BaseResponseBody.class),
            @ApiResponse(code = 500, message = "서버 오류", response = BaseResponseBody.class)
    })
    public ResponseEntity<List<SharePlaylistMusicRes>> getHonoredPlayList () {
        return ResponseEntity.status(200).body(shareMusicService.getHonoredPlayList());
    }

    @GetMapping("/honor/{playId}")
    @ApiOperation(value = "명예의 전당 사연 상세 조회", notes = "선택한 명예의 전당 사연의 상세 내역을 조회한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공", response = SharePlaylistMusicRes.class),
            @ApiResponse(code = 401, message = "인증 실패", response = BaseResponseBody.class),
            @ApiResponse(code = 404, message = "사용자 없음", response = BaseResponseBody.class),
            @ApiResponse(code = 500, message = "서버 오류", response = BaseResponseBody.class)
    })
    public ResponseEntity<SharePlaylistMusicRes> getHonoredMusicAndContents(@PathVariable Long playId) {
        return ResponseEntity.status(200).body(shareMusicService.HonoredMusicAndContents(playId));
    }

    @DeleteMapping("/honor/{playId}")
    @ApiOperation(value = "명예의 전당 사연 삭제", notes = "선택한 명예의 전당 사연의 상세 내역을 삭제한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공", response = SharePlaylistMusicRes.class),
            @ApiResponse(code = 401, message = "인증 실패", response = BaseResponseBody.class),
            @ApiResponse(code = 404, message = "사용자 없음", response = BaseResponseBody.class),
            @ApiResponse(code = 500, message = "서버 오류", response = BaseResponseBody.class)
    })
    public ResponseEntity<? extends BaseResponseBody> deleteMusicFromHonorList(@PathVariable Long playId) {
        shareMusicService.deleteMusicFromHonorList(playId);
        return ResponseEntity.status(200).body(BaseResponseBody.of(200,"Delete Success"));
    }


    @GetMapping("/like/{playId}/user/{userId}")
    @ApiOperation(value = "좋아요", notes = "유저가 '좋아요' 아이콘이 누르면 상태가 토글된다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공", response = BaseResponseBody.class),
            @ApiResponse(code = 401, message = "인증 실패", response = BaseResponseBody.class),
            @ApiResponse(code = 404, message = "사용자 없음", response = BaseResponseBody.class),
            @ApiResponse(code = 500, message = "서버 오류", response = BaseResponseBody.class)
    })
    public ResponseEntity<? extends BaseResponseBody> likeMusicRequest (@PathVariable Long playId, @PathVariable Long userId) {
        shareMusicService.toggleLikeMusicRequest(playId, userId);
        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "Success"));
    }

//    @GetMapping("/nextmusic/{roomId}")
//    @ApiOperation(value = "재생순서", notes = "유저측에서 재생이 끝나면 다음 곡을 위한 요청이 들어온다.")
//    @ApiResponses({
//            @ApiResponse(code = 200, message = "성공", response = BaseResponseBody.class),
//            @ApiResponse(code = 401, message = "인증 실패", response = BaseResponseBody.class),
//            @ApiResponse(code = 404, message = "사용자 없음", response = BaseResponseBody.class),
//            @ApiResponse(code = 500, message = "서버 오류", response = BaseResponseBody.class)
//    })
//    public ResponseEntity getNextMusic(@PathVariable long roomId, @RequestParam String playId) {
//        return  ResponseEntity.status(200).body(shareMusicService.getNextMusic(roomId, playId));
//    }

}
