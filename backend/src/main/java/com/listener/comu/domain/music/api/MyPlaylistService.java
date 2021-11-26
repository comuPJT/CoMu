package com.listener.comu.domain.music.api;

import com.listener.comu.domain.music.domain.*;
import com.listener.comu.domain.music.dto.AllPlaylistRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MyPlaylistService {

    final private MusicRepository musicRepository;
    final private MyplaylistRepository myplaylistRepository;
    final private MyplaylistMusicRepository myplaylistMusicRepository;


    // 전체 플레이리스트 가져오기
    public List<AllPlaylistRes> getAllPlaylistResList(long userSeq) {
        List<AllPlaylistRes> response = new ArrayList<>();

        List<Myplaylist> myPlaylistList = myplaylistRepository.getByUserSeq(userSeq);
        for(int i=0, size=myPlaylistList.size(); i<size; i++){
            Myplaylist myplaylist = myPlaylistList.get(i);
            List<String> thumbnails = myplaylistRepository.getThumbnails(myplaylist.getId());
            response.add(AllPlaylistRes.builder().myplaylist(myplaylist).thumbnails(thumbnails).build());
        }
        return response;
    }

    // 특정 플레이리스트 안의 곡들 가져오기
    public List<Music> getMusics(long myplaylistId) {
        return musicRepository.getMusicByMyplaylistId(myplaylistId);
    }


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
    public void addMusics(long myplaylistId, List<Music> musicList) {

        // spotify_id(음악 식별자)로 music table에 있는 노래인지 확인
        List<String> spotifyIds = new ArrayList<>();

        for (Music music: musicList){
            spotifyIds.add(music.getSpotifyId());
        }

        List<String> presentSpotifyIds = musicRepository.getPresentSpotifyIds(spotifyIds);

        for(int i=0, size=musicList.size(); i<size; i++){

            Music music = musicList.get(i);

            long musicId; // 음악 번호

            String currentId = music.getSpotifyId();
            if(!presentSpotifyIds.contains(currentId)){ // DB에 존재하지 않는 음악이면 music 테이블에 추가
                musicId = musicRepository.save(music).getId();
            } else {
                musicId = musicRepository.getMusicIdBySpotifyId(currentId);

                // 플레이 리스트에 이미 존재하면 삭제(재생 목록에 동일한 노래가 들어가지 않도록, 같은 곡을 넣을 경우 맨 아래에 추가됨). 삭제할 곡 id들 담아주기
                if(Optional.of(myplaylistMusicRepository.getById(musicId)).isPresent()){
                    myplaylistMusicRepository.deleteMyplaylistMusicByMyplaylistIdAndMusicId(myplaylistId, musicId);
                }
            }

            // myplaylist_music에 곡 추가
            myplaylistMusicRepository.save(MyplaylistMusic.builder().musicId(musicId).myplaylistId(myplaylistId).build());
        }

    }


    // 플레이리스트의 특정 곡(들) 삭제
    public void deleteMusic(long myplaylistId, List<Long> musicIds) {
        myplaylistMusicRepository.deleteMyplaylistMusicByMyplaylistIdAndMusicIdIn(myplaylistId, musicIds);
    }

}
