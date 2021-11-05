package com.listener.comu.domain.music.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface MyplaylistRepository extends JpaRepository<Myplaylist, Long> {

    List<Myplaylist> getByUserSeq(long userSeq);

    Optional<Myplaylist> getByName(String name);
    Optional<Myplaylist> getMyplaylistByUserSeqAndName(long userSeq, String name);

    @Transactional
    void deleteMyplaylistByUserSeqAndId(long userSeq, long myplaylistId);

}
