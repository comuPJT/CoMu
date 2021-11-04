package com.listener.comu.domain.mymusic.controller;

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

    @PostMapping("/{userSeq}")
    public ResponseEntity makeList(@PathVariable long userSeq, @RequestBody Map<String, String> request){
        if (myPlaylistService.makeList(userSeq, request.get("name"))) return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @PutMapping("/{userSeq}/{myplaylistId}")
    public  ResponseEntity renameList(@PathVariable long userSeq, @PathVariable long myplaylistId, @RequestBody Map<String, String> request){
        if(myPlaylistService.renameList(userSeq, myplaylistId, request.get("name"))) return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @DeleteMapping("/{userSeq}/{myplaylistId}")
    public ResponseEntity deleteList(@PathVariable long userSeq, @PathVariable long myplaylistId){
        myPlaylistService.deleteList(userSeq, myplaylistId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

//    @DeleteMapping("/{userSeq}/{myplaylistId}")






}