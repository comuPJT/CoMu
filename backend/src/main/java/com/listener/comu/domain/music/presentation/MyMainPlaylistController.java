package com.listener.comu.domain.music.presentation;

import com.listener.comu.domain.music.api.MyMainPlaylistService;
import com.listener.comu.domain.music.domain.Music;
import com.listener.comu.domain.music.dto.AddMyMusicReq;
import com.listener.comu.domain.music.dto.MyMainPlaylistRemoveMusicReq;
import com.listener.comu.domain.music.dto.SetPlayOrderDto;
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

    @GetMapping("/{userSeq}")
    public ResponseEntity<List<Music>> getList(@PathVariable long userSeq){
        return new ResponseEntity<>(myMainPlaylistService.getMyMainPlaylist(userSeq), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity addMusic(@RequestBody AddMyMusicReq request){
        myMainPlaylistService.addMusic(request.getMusicList(), request.getUserSeq());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/playlist/{playlistId}")
    public ResponseEntity addPlaylist(@PathVariable long playlistId, @RequestBody Map<String, Long> request){
        myMainPlaylistService.addPlaylist(playlistId, request.get("userSeq"));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 메인 재생 목록 전체 곡 삭제
    @PutMapping("/all")
    public ResponseEntity removeAllMusic(@RequestBody Map<String, Long> request){
        myMainPlaylistService.removeAllMusic(request.get("userSeq"));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 메인 재생 목록에서 곡 한 개 또는 여러 개 삭제
    @PutMapping
    public ResponseEntity removeMusic(@RequestBody MyMainPlaylistRemoveMusicReq request){
        myMainPlaylistService.removeMusic(request.getUserSeq(), request.getMusicIds());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 메인 재생 목록의 순서가 변경되었을 때 순서 적용
//    @PutMapping
//    public ResponseEntity setPlayOrder(@RequestBody SetPlayOrderDto setPlayOrderDto){
//        myMainPlaylistService.setPlayOrder(setPlayOrderDto.getUserSeq(), setPlayOrderDto.getMyMainPlaylistList());
//        return new ResponseEntity<>(HttpStatus.OK);
//    }



}
