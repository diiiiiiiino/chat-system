package com.dino.chat.api.member.command.application.dto;

import com.dino.chat.api.member.command.application.security.SecurityMember;
import com.dino.chat.api.member.command.domain.Member;
import com.dino.chat.api.util.VerifyUtil;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberResponse {
    private Long id;
    private String name;
    private String mobile;

    /**
     * @param securityMember 인증완료 회원
     * @return {@code Http Response} 회원
     * @throws NullPointerException {@code securityMember}가 {@code null}일때
     */
    public static MemberResponse from(SecurityMember securityMember){
        VerifyUtil.verifyNull(securityMember, "securityMember");

        Member member = securityMember.getMember();

        return MemberResponse.builder()
                .id(member.getId())
                .name(member.getName())
                .mobile(member.getMobile().getValue())
                .build();
    }
}
