package com.listener.comu.domain.oauthlogin.api.controller.user;

import com.listener.comu.domain.oauthlogin.api.entity.user.ComuUserInfo;
import com.listener.comu.domain.oauthlogin.api.entity.user.User;
import com.listener.comu.domain.oauthlogin.api.service.UserService;
import com.listener.comu.domain.oauthlogin.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ApiResponse getUser() {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = userService.getUser(principal.getUsername());

        return ApiResponse.getUserSuccess("user", user, "userSeq", user.getUserSeq());
    }

    // UserSeq 가져오는 부분 테스트용
    public User getUserForIdentification(){
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return userService.getUser(principal.getUsername());
    }
    ////////////////////

    @PutMapping
    public ResponseEntity modifyUserInfo(@RequestBody ComuUserInfo comuUserInfo) {
        boolean response = userService.modifyUserInfo(comuUserInfo.getUserSeq(), comuUserInfo.getUsername(), comuUserInfo.getCharacterNum());
        if (!response) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

}