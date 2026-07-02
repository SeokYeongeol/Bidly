package com.example.bidly.global.oauth2.service;

import com.example.bidly.domain.member.entity.Member;
import com.example.bidly.domain.member.repository.MemberRepository;
import com.example.bidly.domain.member.role.MemberRole;
import com.example.bidly.global.oauth2.attribute.OAuthAttributes;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(request);

        String provider = request.getClientRegistration().getRegistrationId();
        String userNameAttributeName = request.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        OAuthAttributes attributes = OAuthAttributes.of(provider, userNameAttributeName, oAuth2User.getAttributes());

        Member member = saveOrUpdate(attributes);

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(member.getRole().name())),
                oAuth2User.getAttributes(),
                userNameAttributeName
        );
    }

    // 최초 로그인이면 저장, 기존 회원이면 업데이트
    private Member saveOrUpdate(OAuthAttributes attributes) {
        return memberRepository.findByEmail(attributes.getEmail())
                .orElseGet(() -> {
                    // 임시 닉네임 생성 (중복 없을 때 까지)
                    String tempName = generateTempName();

                    return memberRepository.save(Member.builder()
                            .email(attributes.getEmail())
                            .name(tempName)
                            .provider(attributes.getProvider())
                            .providerId(attributes.getProviderId())
                            .role(MemberRole.ROLE_MEMBER)
                            .build());
                });
    }

    // 임시 닉네임 생성 메소드
    private String generateTempName() {
        String tempName;
        do {
            tempName = "U" + UUID.randomUUID().toString().replace("-", "").substring(0, 9);
        } while (memberRepository.existsByName(tempName));
        return tempName;
    }
}