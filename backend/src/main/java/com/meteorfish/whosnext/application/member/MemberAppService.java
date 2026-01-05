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

    @Transactional(readOnly = true)
    public Member getMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."))
                .toDomain();
    }

    @Transactional(readOnly = true)
    public Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."))
                .toDomain();
    }

    @Transactional
    public void updateNickname(Long memberId, String newNickname) {
        MemberEntity entity = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Member member = entity.toDomain();
        member.updateNickname(newNickname);
        memberRepository.save(MemberEntity.from(member));
    }
}
