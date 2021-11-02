package com.listener.comu.domain.oauthlogin.api.repository.user;

import com.listener.comu.domain.oauthlogin.api.entity.user.User;
import jdk.nashorn.internal.runtime.options.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUserId(String userId);
    Optional<User> findByUsername(String username);

}