package com.listener.comu.domain.music.presentation;

import com.listener.comu.domain.music.domain.Music;
import com.listener.comu.domain.music.domain.Myplaylist;
import com.listener.comu.domain.music.dto.AddMyMusicReq;
import com.listener.comu.domain.music.dto.MyPlaylistRequest;
import com.listener.comu.domain.music.api.MyPlaylistService;
import com.listener.comu.domain.oauthlogin.api.controller.user.UserController;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RequiredArgsConstructor
@RequestMapping("/api/myplaylist")
@RestController
public class MyPlaylistController {

    final private MyPlaylistService myPlaylistService;

    @ApiOperation(value = "저장된 전체 플레이리스트 가져오기", notes = "사용자가 생성한 플레이리스트 목록 전체를 가져온다.")
    @GetMapping("/all/{userSeq}")
    public ResponseEntity<List<Myplaylist>> getList(@PathVariable long userSeq){
        List<Myplaylist> myPlaylistList = myPlaylistService.getList(userSeq);
        return new ResponseEntity<>(myPlaylistList, HttpStatus.OK);
    }

    @ApiOperation(value = "플레이리스트에 저장된 곡들 가져오기", notes = "특정 플레이리스트에 저장된 전체 곡들을 가져온다.")
    @GetMapping("/{myplaylistId}")
    public ResponseEntity<Map<String, Object>> getMusics(@PathVariable long myplaylistId){
        Map<String, Object> response = new HashMap<>();

        List<Music> musics = myPlaylistService.getMusics(myplaylistId);

        response.put("musics", musics);
        response.put("numberOfMusics", musics.size());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiOperation(value = "새로운 플레이리스트 생성", notes = "새로운 플레이리스트를 생성한다.")
    @PostMapping
    public ResponseEntity makeList(@RequestBody MyPlaylistRequest myPlaylistRequest){
        if (myPlaylistService.makeList(myPlaylistRequest.getUserSeq(), myPlaylistRequest.getName())) return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @ApiOperation(value = "플레이리스트에 음악 추가", notes = "특정 플레이리스트에 한 개 또는 여러 개의 곡을 추가한다.")
    @PostMapping("/{myplaylistId}/music")
    public ResponseEntity addMusics(@PathVariable long myplaylistId, @RequestBody List<Music> musicList){
        myPlaylistService.addMusics(myplaylistId, musicList);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "플레이리스트 이름 수정", notes = "플레이리스트의 이름을 수정할 수 있다.")
    @PutMapping("/{myplaylistId}")
    public  ResponseEntity renameList(@PathVariable long myplaylistId, @RequestBody MyPlaylistRequest myPlaylistRequest){
        if(myPlaylistService.renameList(myPlaylistRequest.getUserSeq(), myplaylistId, myPlaylistRequest.getName())) return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @ApiOperation(value = "플레이리스트 삭제", notes = "특정 플레이리스트를 삭제한다.")
    @DeleteMapping("/{myplaylistId}/{userSeq}")
    public ResponseEntity deleteList(@PathVariable long myplaylistId, @PathVariable long userSeq){
        myPlaylistService.deleteList(userSeq, myplaylistId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "플레이리스트에 저장된 곡 삭제", notes = "특정 플레이리스트에 저장된 한 개 또는 여러 개의 곡을 삭제한다.")
    @PutMapping("/{myplaylistId}/music")
    public ResponseEntity deleteMusic(@PathVariable long myplaylistId, @RequestBody MyPlaylistRequest myPlaylistRequest){
        myPlaylistService.deleteMusic(myplaylistId, myPlaylistRequest.getMusicIds());
        return new ResponseEntity<>(HttpStatus.OK);
    }


}