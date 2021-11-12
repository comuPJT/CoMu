package com.listener.comu.domain.music.api;

import com.listener.comu.domain.music.domain.Music;
import com.listener.comu.domain.music.domain.MusicRepository;
import com.listener.comu.domain.music.domain.MyMainPlaylist;
import com.listener.comu.domain.music.domain.MyMainPlaylistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

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

        return musicRepository.getSortedMusicsById(musicIds);
    }

    // 재생 목록에 곡(한 개 또는 여러 개) 추가
    public void addMusic(List<Music> musicList, long userSeq) {

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
                
                // 재생 목록에 이미 존재하면 삭제(재생 목록에 동일한 노래가 들어가지 않도록, 같은 곡을 넣을 경우 맨 아래에 추가됨)
                removeMusic(userSeq, Arrays.asList(musicId));
            }
            int playOrder = (int)myMainPlaylistRepository.count();

            // my_main_playlist에 연결 관계 추가
            myMainPlaylistRepository.save(MyMainPlaylist.builder().musicId(musicId).userSeq(userSeq).playOrder(playOrder).build());

        }

    }


    // 재생 목록 전체 곡 삭제

    // 재생 목록 곡(한 개 또는 여러 개) 삭제
    public void removeMusic(long userSeq, List<Long> musicIds) {
        myMainPlaylistRepository.deleteMyMainPlaylistByUserSeqAndMusicIdIn(userSeq, musicIds);
    }

}
