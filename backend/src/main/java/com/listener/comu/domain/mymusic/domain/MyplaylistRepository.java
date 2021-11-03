package com.listener.comu.domain.mymusic.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MyplaylistRepository extends JpaRepository<Myplaylist, Long> {

    Optional<Myplaylist> getByName(String name);

}
