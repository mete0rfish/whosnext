package com.meteorfish.whosnext.infrastructure.persistence.member;

import com.meteorfish.whosnext.domain.member.Member;
import jakarta.persistence.*;

@Entity
@Table(name = "members")
public class MemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    private String nickname;
    private String socialId;
    private String provider;
    private String role;

    protected MemberEntity() {}

    public MemberEntity(Long id, String email, String nickname, String socialId, String provider, String role) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.socialId = socialId;
        this.provider = provider;
        this.role = role;
    }

    public static MemberEntity from(Member member) {
        return new MemberEntity(
                member.getId(),
                member.getEmail(),
                member.getNickname(),
                member.getSocialId(),
                member.getProvider(),
                member.getRole()
        );
    }

    public Member toDomain() {
        return new Member(id, email, socialId, provider, role, nickname);
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
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
