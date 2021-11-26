package com.listener.comu.domain.music.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface MyplaylistMusicRepository extends JpaRepository<MyplaylistMusic, Long> {

    // myplaylistId로 삭제
    @Transactional
    void deleteMyplaylistMusicByMyplaylistId(long myplaylistId);

    // myplaylistId와 musicIds로 삭제
    @Transactional
    void deleteMyplaylistMusicByMyplaylistIdAndMusicIdIn(long myplaylistId, List<Long> musicIds);

    // myplaylistId와 musicId로 삭제
    @Transactional
    void deleteMyplaylistMusicByMyplaylistIdAndMusicId(long myplaylistId, long musicId);
}
