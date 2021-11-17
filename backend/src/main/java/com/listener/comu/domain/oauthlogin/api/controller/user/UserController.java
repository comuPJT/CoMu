package com.listener.comu.domain.oauthlogin.api.controller.user;

import com.listener.comu.domain.oauthlogin.api.entity.user.ComuUserInfo;
import com.listener.comu.domain.oauthlogin.api.entity.user.User;
import com.listener.comu.domain.oauthlogin.api.service.UserService;
import com.listener.comu.domain.oauthlogin.common.ApiResponse;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @ApiOperation(value = "User 객체 받아오기", notes = "토큰 값으로 User 객체를 받아온다.")
    @GetMapping
    public ApiResponse getUser() {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = userService.getUser(principal.getUsername());

        return ApiResponse.getUserSuccess("user", user, "userSeq", user.getUserSeq());
    }

    @ApiOperation(value = "username 중복 체크하기", notes = "사용자가 입력한 username이 이미 존재하는 username인지 확인한다. 사용이 가능할 때 상태코드 200을 반환한다.")
    @GetMapping("/checkName/{username}")
    public ResponseEntity checkName(@PathVariable String username) {
        boolean isPresent = userService.checkName(username);
        if (isPresent) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "사용자 캐릭터(characterNum) 등록하기", notes = "사용자의 캐릭터를 설정한다.")
    @PutMapping("/character")
    public ResponseEntity setCharacterNum(@RequestBody Map<String, Integer> request) {
        userService.setCharacterNum(Long.valueOf(request.get("userSeq")),request.get("characterNum"));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "사용자 이름(username) 등록하기", notes = "사용자의 이름을 설정한다.")
    @PutMapping
    public ResponseEntity modifyUserInfo(@RequestBody ComuUserInfo comuUserInfo) {
        userService.modifyUserInfo(comuUserInfo.getUserSeq(), comuUserInfo.getUsername());
        return new ResponseEntity<>(HttpStatus.OK);
    }





}