package com.listener.comu.domain.music.presentation;

import com.listener.comu.domain.music.api.MyMainPlaylistService;
import com.listener.comu.domain.music.domain.Music;
import com.listener.comu.domain.music.dto.MyMainPlayllistAddMusicReq;
import com.listener.comu.domain.music.dto.MyMainPlaylistRemoveMusicReq;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RequiredArgsConstructor
@RequestMapping("/mymainplaylist")
@RestController
public class MyMainPlaylistController {

    final private MyMainPlaylistService myMainPlaylistService;

    @GetMapping
    public ResponseEntity<List<Music>> getList(@RequestBody Map<String, Long> request){
        return new ResponseEntity<>(myMainPlaylistService.getMyMainPlaylist(request.get("userSeq")), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity addMusic(@RequestBody MyMainPlayllistAddMusicReq request){
        myMainPlaylistService.addMusic(request.getMusicList(), request.getUserSeq());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/all")
    public ResponseEntity removeAllMusic(@RequestBody Map<String, Long> request){
        myMainPlaylistService.removeAllMusic(request.get("userSeq"));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity removeMusic(@RequestBody MyMainPlaylistRemoveMusicReq request){
        myMainPlaylistService.removeMusic(request.getUserSeq(), request.getMusicIds());
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
