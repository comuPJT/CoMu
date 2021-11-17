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

    public void modifyUserInfo(long userSeq, String username) {

        User inputUser = userRepository.getById(userSeq);
        inputUser.setUserSeq(userSeq);
        inputUser.setUsername(username);
        inputUser.setFirstVisitYn("N");
        userRepository.save(inputUser);

    }

    public void setCharacterNum(Long userSeq, Integer characterNum) {
        User inputUser = userRepository.getById(userSeq);
        inputUser.setCharacterNum(characterNum);
        userRepository.save(inputUser);


    }

    public boolean checkName(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) return true; // name이 이미 존재한다는 뜻(중복 허용 x)
        return false;
    }

}