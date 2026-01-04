package com.meteorfish.whosnext.domain.member;

public class Member {
    private final Long id;
    private final String email;
    private String nickname;
    private final String socialId;
    private final String provider;
    private final String role;

    public Member(Long id, String email, String socialId, String provider, String role, String nickname) {
        validateEmail(email);
        this.id = id;
        this.email = email;
        this.socialId = socialId;
        this.provider = provider;
        this.role = role;
        this.nickname = nickname;
    }

    private void validateEmail(String email) {
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Invalid email address");
        }
    }

    public void updateNickname(String newNickname) {
        if (newNickname == null || newNickname.isBlank()) {
            throw new IllegalArgumentException("닉네임은 비어있을 수 없습니다.");
        }
        this.nickname = newNickname;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getNickname() {
        return nickname;
    }

    public String getSocialId() {
        return socialId;
    }

    public String getProvider() {
        return provider;
    }

    public String getRole() {
        return role;
    }
}
