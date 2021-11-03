package com.listener.comu.domain.mymusic.service;

import com.listener.comu.domain.mymusic.domain.Myplaylist;
import com.listener.comu.domain.mymusic.domain.MyplaylistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MyPlaylistService {

    final private MyplaylistRepository myplaylistRepository;

    // 플레이리스트 생성
    public boolean makeList(long userSeq, String name){

        Optional<Myplaylist> myplaylistOptional = myplaylistRepository.getByName(name);

        System.out.println("받아왔나??");
        if (myplaylistOptional.isPresent()) return false;

        myplaylistRepository.save(Myplaylist.builder().name(name).userSeq(userSeq).build());
        return true;
    }

}
