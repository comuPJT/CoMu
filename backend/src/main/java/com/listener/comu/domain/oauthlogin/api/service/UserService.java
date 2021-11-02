package com.listener.comu.domain.oauthlogin.api.service;

import com.listener.comu.domain.oauthlogin.api.entity.user.User;
import com.listener.comu.domain.oauthlogin.api.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User getUser(String userId) {
        return userRepository.findByUserId(userId);
    }

    public boolean modifyUserInfo(String userId, String username, String character) {
        Optional<User> user = userRepository.findByUsername(username);

        if (user.isPresent()) return false;
        User inputUser = new User();
        inputUser.setUserId(userId);
        inputUser.setUsername(username);
        inputUser.setProfileImageUrl(character);
        inputUser.setFirstVisitYn("N");
        userRepository.save(inputUser);

        return true;
    }

}
