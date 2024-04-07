package com.dino.chat.api.member.command.application.dto;

import com.dino.chat.api.authority.command.domain.Authority;
import com.dino.chat.api.authority.command.domain.enumeration.AuthorityEnum;
import com.dino.chat.api.member.command.domain.Member;
import com.dino.chat.api.member.command.domain.MemberAuthority;
import com.dino.chat.api.member.command.domain.Mobile;
import com.dino.chat.api.member.command.domain.Password;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.function.Function;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberCreateRequest {
    private String loginId;
    private String password;
    private String name;
    private String mobile;
    private String inviteCode;

    public static MemberCreateRequest of(String loginId, String password, String name, String mobile, String inviteCode){
        return new MemberCreateRequest(loginId, password, name, mobile, inviteCode);
    }

    public static Member newMember(MemberCreateRequest request, PasswordEncoder passwordEncoder){
        List<Function<Member, MemberAuthority>> functions = List.of(member -> MemberAuthority.of(member, Authority.of(AuthorityEnum.ROLE_MEMBER)));
        return Member.of(request.getLoginId(), Password.of(request.getPassword(), passwordEncoder), request.getName(), Mobile.of(request.getMobile()), functions);
    }
}
