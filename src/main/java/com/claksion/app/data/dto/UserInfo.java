package com.claksion.app.data.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserInfo {
    private String oauthId;
    private String profileImg;
    private String email;

    public UserInfo(OauthType oauthType, String id, String email, String profileImg) {
        this.oauthId = oauthType + "_" + id;
        this.email = email;
        this.profileImg = profileImg;
    }
}

