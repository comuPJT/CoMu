package com.listener.comu.domain.mymusic.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

public interface MyplaylistMusicRepository extends JpaRepository<MyplaylistMusic, Long> {

    @Transactional
    void deleteMyplaylistMusicByMyplaylistId(long myplaylistId);

}
