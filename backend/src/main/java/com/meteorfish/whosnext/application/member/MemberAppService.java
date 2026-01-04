package com.meteorfish.whosnext.application.member;

import com.meteorfish.whosnext.domain.member.Member;
import com.meteorfish.whosnext.infrastructure.persistence.member.MemberEntity;
import com.meteorfish.whosnext.infrastructure.persistence.member.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberAppService {
    private final MemberRepository memberRepository;

    public MemberAppService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public void changeNickname(Long memberId, String newNickname) {
        MemberEntity entity = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Member member = entity.toDomain();
        member.updateNickname(newNickname);
        memberRepository.save(MemberEntity.from(member));
    }
}
