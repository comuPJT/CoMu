package com.listener.comu.domain.music.presentation;

import com.listener.comu.domain.music.api.MyMainPlaylistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RequiredArgsConstructor
@RequestMapping("/mymainplaylist")
@RestController
public class MyMainPlaylistController {

    final private MyMainPlaylistService myMainPlaylistService;

}
