package com.listener.comu.domain.music.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface MyplaylistRepository extends JpaRepository<Myplaylist, Long> {

    List<Myplaylist> getByUserSeq(long userSeq);

    Optional<Myplaylist> getByName(String name);
    Optional<Myplaylist> getMyplaylistByUserSeqAndName(long userSeq, String name);

    @Transactional
    void deleteMyplaylistByUserSeqAndId(long userSeq, long myplaylistId);

    @Query(value = "SELECT thumbnail FROM music WHERE id IN (SELECT music_id FROM myplaylist_music WHERE myplaylist_id = :myPlaylistId) LIMIT 4", nativeQuery = true)
    List<String> getThumbnails(long myPlaylistId);
}
