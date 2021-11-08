package com.listener.comu.domain.music.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MyMainPlaylistRepository extends JpaRepository<MyMainPlaylist, Long> {

    List<MyMainPlaylist> getMyMainPlaylistsByUserSeq(long userSeq);
}
