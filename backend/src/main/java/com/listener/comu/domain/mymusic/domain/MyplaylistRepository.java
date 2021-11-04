package com.listener.comu.domain.mymusic.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import javax.transaction.Transactional;
import java.util.Optional;

public interface MyplaylistRepository extends JpaRepository<Myplaylist, Long> {

    Optional<Myplaylist> getByName(String name);
    Optional<Myplaylist> getMyplaylistByUserSeqAndName(long userSeq, String name);

    @Transactional
    void deleteMyplaylistByUserSeqAndId(long userSeq, long myplaylistId);

}
