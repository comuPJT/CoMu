package com.listener.comu.domain.mymusic.controller;

import com.listener.comu.domain.mymusic.dto.MyPlaylistRequest;
import com.listener.comu.domain.mymusic.service.MyPlaylistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin("*")
@RequiredArgsConstructor
@RequestMapping("/myplaylist")
@RestController
public class MyPlaylistController {

    final private MyPlaylistService myPlaylistService;

    

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