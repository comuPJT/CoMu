package com.listener.comu.domain.music.presentation;

import com.listener.comu.domain.music.domain.Music;
import com.listener.comu.domain.music.domain.Myplaylist;
import com.listener.comu.domain.music.dto.MyPlaylistRequest;
import com.listener.comu.domain.music.api.MyPlaylistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RequiredArgsConstructor
@RequestMapping("/api/myplaylist")
@RestController
public class MyPlaylistController {

    final private MyPlaylistService myPlaylistService;

    @GetMapping
    public ResponseEntity<List<Myplaylist>> getList(@RequestBody Map<String, Long> request){
        List<Myplaylist> myPlaylistList = myPlaylistService.getList(request.get("userSeq"));
        return new ResponseEntity<>(myPlaylistList, HttpStatus.OK);
    }

    @GetMapping("/{myplaylistId}")
    public ResponseEntity<List<Music>> getMusics(@PathVariable long myplaylistId){
        List<Music> musics = myPlaylistService.getMusics(myplaylistId);
        return new ResponseEntity<>(musics, HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity makeList(@RequestBody MyPlaylistRequest myPlaylistRequest){
        if (myPlaylistService.makeList(myPlaylistRequest.getUserSeq(), myPlaylistRequest.getName())) return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @PutMapping("/{myplaylistId}")
    public  ResponseEntity renameList(@PathVariable long myplaylistId, @RequestBody MyPlaylistRequest myPlaylistRequest){
        if(myPlaylistService.renameList(myPlaylistRequest.getUserSeq(), myplaylistId, myPlaylistRequest.getName())) return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @DeleteMapping("/{myplaylistId}")
    public ResponseEntity deleteList(@PathVariable long myplaylistId, @RequestBody MyPlaylistRequest myPlaylistRequest){
        myPlaylistService.deleteList(myPlaylistRequest.getUserSeq(), myplaylistId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{myplaylistId}/music")
    public ResponseEntity deleteMusic(@PathVariable long myplaylistId, @RequestBody MyPlaylistRequest myPlaylistRequest){
        myPlaylistService.deleteMusic(myplaylistId, myPlaylistRequest.getMusicIds());
        return new ResponseEntity<>(HttpStatus.OK);
    }






}