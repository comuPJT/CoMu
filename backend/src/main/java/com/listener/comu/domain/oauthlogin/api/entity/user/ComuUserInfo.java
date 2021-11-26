package com.listener.comu.domain.oauthlogin.api.entity.user;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ComuUserInfo {

    long userSeq;
    String username;

}
