package com.listener.comu.domain.music.presentation;

import com.listener.comu.domain.music.api.MyMainPlaylistService;
import com.listener.comu.domain.music.domain.Music;
import com.listener.comu.domain.music.dto.AddMyMusicReq;
import com.listener.comu.domain.music.dto.MyMainPlaylistRemoveMusicReq;
import com.listener.comu.domain.music.dto.SetPlayOrderReq;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RequiredArgsConstructor
@RequestMapping("/api/mymainplaylist")
@RestController
public class MyMainPlaylistController {

    final private MyMainPlaylistService myMainPlaylistService;

    @ApiOperation(value = "재생 목록 가져오기", notes = "사용자의 메인 재생 목록을 가져온다.")
    @GetMapping("/{userSeq}")
    public ResponseEntity<List<Music>> getList(@PathVariable long userSeq){
        return new ResponseEntity<>(myMainPlaylistService.getMyMainPlaylist(userSeq), HttpStatus.OK);
    }

    @ApiOperation(value = "재생 목록에 곡 추가", notes = "메인 재생 목록에 곡을 한 개 또는 여러개 추가한다.")
    @PostMapping
    public ResponseEntity addMusic(@RequestBody AddMyMusicReq request){
        myMainPlaylistService.addMusic(request.getMusicList(), request.getUserSeq());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "재생 목록에 특정 플레이리스트 추가", notes = "메인 재생 목록에 특정 플레이 리스트의 전체 곡을 추가한다.")
    @PostMapping("/playlist/{playlistId}")
    public ResponseEntity addPlaylist(@PathVariable long playlistId, @RequestBody Map<String, Long> request){
        myMainPlaylistService.addPlaylist(playlistId, request.get("userSeq"));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "재생 목록 전체 곡 삭제", notes = "메인 재생 목록의 전체 곡을 삭제한다.")
    @PutMapping("/all")
    public ResponseEntity removeAllMusic(@RequestBody Map<String, Long> request){
        myMainPlaylistService.removeAllMusic(request.get("userSeq"));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "재생 목록 곡 삭제", notes = "메인 재생 목록의 한 개 또는 여러 개 곡을 삭제한다.")
    @PutMapping
    public ResponseEntity removeMusic(@RequestBody MyMainPlaylistRemoveMusicReq request){
        myMainPlaylistService.removeMusic(request.getUserSeq(), request.getMusicIds());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "재생 목록 순서 변경", notes = "사용자가 메인 재생 목록 창을 닫을 때 변경된 순서로 곡을 저장한다.")
    @PutMapping("/setPlayOrder")
    public ResponseEntity setPlayOrder(@RequestBody SetPlayOrderReq setPlayOrderReq){
        myMainPlaylistService.setPlayOrder(setPlayOrderReq.getUserSeq(), setPlayOrderReq.getMusicIdPlayOrderDtoList());
        return new ResponseEntity<>(HttpStatus.OK);
    }



}