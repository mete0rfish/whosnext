package com.meteorfish.whosnext.api.member;

import com.meteorfish.whosnext.application.member.MemberAppService;
import com.meteorfish.whosnext.domain.member.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "Member API", description = "회원 정보 관리 API")
@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberAppService memberAppService;

    public MemberController(MemberAppService memberAppService) {
        this.memberAppService = memberAppService;
    }

    @Operation(summary = "내 정보 조회", description = "현재 로그인한 사용자의 프로필 정보를 조회합니다.")
    @GetMapping("/me")
    public ResponseEntity<MemberResponse> getMyProfile(@AuthenticationPrincipal OAuth2User principal) {
        String email = extractEmail(principal);
        Member member = memberAppService.getMemberByEmail(email);

        return ResponseEntity.ok(MemberResponse.from(member));
    }

    @Operation(summary = "닉네임 수정", description = "현재 로그인한 사용자의 닉네임을 수정합니다.")
    @PatchMapping("/me/nickname")
    public ResponseEntity<Void> updateNickname(
            @AuthenticationPrincipal OAuth2User principal,
            @RequestBody MemberNicknameUpdateRequest request) {

        String email = extractEmail(principal);
        Member member = memberAppService.getMemberByEmail(email);

        memberAppService.updateNickname(member.getId(), request.nickname());

        return ResponseEntity.noContent().build();
    }

    private String extractEmail(OAuth2User principal) {
        Map<String, Object> attributes = principal.getAttributes();
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        return (String) kakaoAccount.get("email");
    }
}
