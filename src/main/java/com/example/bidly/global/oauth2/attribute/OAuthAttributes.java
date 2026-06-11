package com.example.bidly.global.oauth2.attribute;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class OAuthAttributes {

    private String email;
    private String name;
    private String provider;
    private String providerId;

    public static OAuthAttributes of(String provider, String userNameAttributeName, Map<String, Object> attributes) {
        return switch (provider) {
            case "kakao" -> ofKakao(attributes);
            default -> ofGoogle(attributes);
        };
    }

    private static OAuthAttributes ofKakao(Map<String, Object> attributes) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        return OAuthAttributes.builder()
                .email((String) kakaoAccount.get("email"))
                .name((String) profile.get("nickname"))
                .provider("kakao")
                .providerId(String.valueOf(attributes.get("id")))
                .build();
    }

    private static OAuthAttributes ofGoogle(Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .email((String) attributes.get("email"))
                .name((String) attributes.get("name"))
                .provider("google")
                .providerId((String) attributes.get("sub"))
                .build();
    }
}