package com.listener.comu.domain.mymusic.service;

import com.listener.comu.domain.mymusic.domain.Myplaylist;
import com.listener.comu.domain.mymusic.domain.MyplaylistMusicRepository;
import com.listener.comu.domain.mymusic.domain.MyplaylistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MyPlaylistService {

    final private MyplaylistRepository myplaylistRepository;
    final private MyplaylistMusicRepository myplaylistMusicRepository;

    // 플레이리스트 생성
    public boolean makeList(long userSeq, String name){

        Optional<Myplaylist> myplaylistOptional = myplaylistRepository.getByName(name);

        if (myplaylistOptional.isPresent()) return false;
        myplaylistRepository.save(Myplaylist.builder().name(name).userSeq(userSeq).build());

        return true;
    }

    // 플레이리스트 이름 수정
    public boolean renameList(long userSeq, long myplaylistId, String name) {
        if(myplaylistRepository.getMyplaylistByUserSeqAndName(userSeq, name).isPresent()) return false;

        Myplaylist mpl = myplaylistRepository.getById(myplaylistId);
        mpl.setName(name);
        myplaylistRepository.save(mpl);

        myplaylistRepository.save(Myplaylist.builder().id(myplaylistId).name(name).userSeq(userSeq).build());
        return true;
    }

    // 플레이리스트 삭제
    public void deleteList(long userSeq, long myplaylistId) {
        myplaylistMusicRepository.deleteMyplaylistMusicByMyplaylistId(myplaylistId);
        myplaylistRepository.deleteMyplaylistByUserSeqAndId(userSeq, myplaylistId);
    }

    // 플레이리스트에 곡 추가

    // 플레이리스트의 특정 곡(들) 삭제




}