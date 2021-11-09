package com.listener.comu.domain.music.api;

import com.listener.comu.domain.music.domain.Music;
import com.listener.comu.domain.music.domain.MusicRepository;
import com.listener.comu.domain.music.domain.MyMainPlaylist;
import com.listener.comu.domain.music.domain.MyMainPlaylistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MyMainPlaylistService {

    final private MyMainPlaylistRepository myMainPlaylistRepository;
    final private MusicRepository musicRepository;

    // 재생 목록 가져오기
    public List<Music> getMyMainPlaylist(long userSeq){

        System.out.println(userSeq);
        List<MyMainPlaylist> myMainPlaylists= myMainPlaylistRepository.getMyMainPlaylistsByUserSeq(userSeq);
        Collections.sort(myMainPlaylists, new Comparator<MyMainPlaylist>() {
            @Override
            public int compare(MyMainPlaylist o1, MyMainPlaylist o2) {
                return o1.getPlayOrder() - o2.getPlayOrder();
            }
        });

        List<Long> musicIds = new ArrayList<>();
        for (MyMainPlaylist myMainPlaylist: myMainPlaylists){
            musicIds.add(myMainPlaylist.getMusicId());
        }

        if (musicIds.isEmpty()){
            return new ArrayList<Music>();
        }

        System.out.println("여기까지는 들어옵니다");
        List<Music> response = musicRepository.getSortedMusicsById(musicIds);
        return response;
    }

    // 재생 목록에 곡(한 개 또는 여러 개) 추가
    public void addMusic(List<Music> musicList, long userSeq) {

        // spotify_id(음악 식별자)로 music table에 있는 노래인지 확인
        List<String> spotifyIds = new ArrayList<>();

        for (Music music: musicList){
            spotifyIds.add(music.getSpotifyId());
        }

        List<String> presentSpotifyIds = musicRepository.getPresentSpotifyIds(spotifyIds);

        // ★★★ 재생 목록에 있는 노래면 삭제하고 맨 아래에다가 넣어놓기 ★★★

        for(int i=0, size=musicList.size(); i<size; i++){

            Music music = musicList.get(i);

            long musicId; // 음악 번호

            // DB에 존재하지 않는 음악이면 music 테이블에 추가
            String currentId = music.getSpotifyId();
            if(!presentSpotifyIds.contains(currentId)){
                musicId = musicRepository.save(music).getId();
            } else {
                musicId = musicRepository.getMusicIdBySpotifyId(currentId);
            }
            int playOrder = (int)myMainPlaylistRepository.count();

            // my_main_playlist에 연결 관계 추가
            myMainPlaylistRepository.save(MyMainPlaylist.builder().musicId(musicId).userSeq(userSeq).playOrder(playOrder).build());

        }

    }

    // 재생 목록 전체 곡 삭제

    // 재생 목록 곡(한 개 또는 여러 개) 삭제


}
