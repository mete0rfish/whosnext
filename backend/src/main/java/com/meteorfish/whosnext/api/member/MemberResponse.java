package com.meteorfish.whosnext.api.member;

import com.meteorfish.whosnext.domain.member.Member;

public record MemberResponse(
        Long id,
        String email,
        String nickname,
        String provider
) {
    public static MemberResponse from(Member member) {
        return new MemberResponse(
                member.getId(),
                member.getEmail(),
                member.getNickname(),
                member.getProvider()
        );
    }
}
