package com.meteorfish.whosnext.application.member;

import com.meteorfish.whosnext.domain.member.Member;
import com.meteorfish.whosnext.infrastructure.persistence.member.MemberEntity;
import com.meteorfish.whosnext.infrastructure.persistence.member.MemberRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    public CustomOAuth2UserService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(registrationId, oAuth2User.getAttributes());
        Member member = saveOrUpdate(userInfo);

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(member.getRole())),
                userInfo.getAttributes(),
                userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName()
        );
    }

    private Member saveOrUpdate(OAuth2UserInfo userInfo) {
        MemberEntity entity = memberRepository.findByEmail(userInfo.getEmail())
                .map(existingEntity -> {
                    Member m = existingEntity.toDomain();
                    m.updateNickname(userInfo.getNickname());
                    return MemberEntity.from(m);
                })
                .orElseGet(() -> {
                    Member newMember = new Member(
                            null,
                            userInfo.getEmail(),
                            userInfo.getProviderId(),
                            userInfo.getProvider(),
                            userInfo.getNickname(),
                            "ROLE_USER");
                    return MemberEntity.from(newMember);
                });

        return memberRepository.save(entity).toDomain();
    }
}
