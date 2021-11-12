package com.listener.comu.domain.music.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface MyMainPlaylistRepository extends JpaRepository<MyMainPlaylist, Long> {

    List<MyMainPlaylist> getMyMainPlaylistsByUserSeq(long userSeq);

    @Transactional
    void deleteMyMainPlaylistByUserSeqAndMusicIdIn(long userSeq, List<Long> musicIds);

    @Query(value = "SELECT MAX(play_order) FROM my_main_playlist", nativeQuery = true)
    int getMaxPlayOrder();

    @Transactional
    void deleteMyMainPlaylistByUserSeq(long userSeq);

}