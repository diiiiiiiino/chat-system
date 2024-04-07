package com.dino.chat.api.member.command.application.service;

import com.dino.chat.api.authority.command.domain.enumeration.AuthorityEnum;
import com.dino.chat.api.common.component.DateUtils;
import com.dino.chat.api.common.exception.ValidationCode;
import com.dino.chat.api.common.exception.ValidationError;
import com.dino.chat.api.common.exception.ValidationErrorException;
import com.dino.chat.api.invite.command.domain.MemberInvite;
import com.dino.chat.api.invite.command.domain.repository.MemberInviteCodeRepository;
import com.dino.chat.api.member.command.application.dto.MemberCreateRequest;
import com.dino.chat.api.member.command.application.exception.ExpiredInviteCodeException;
import com.dino.chat.api.member.command.application.exception.HouseHoldNotFoundException;
import com.dino.chat.api.member.command.application.exception.InviteCodeNotFoundException;
import com.dino.chat.api.member.command.application.validator.MemberCreateRequestValidator;
import com.dino.chat.api.member.command.domain.Member;
import com.dino.chat.api.member.command.domain.MemberAuthority;
import com.dino.chat.api.member.command.domain.Mobile;
import com.dino.chat.api.member.command.domain.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MemberCreateServiceTest {

    private DateUtils dateUtils;
    private MemberInviteCodeRepository memberInviteCodeRepository;
    private MemberRepository memberRepository;
    private MemberCreateRequestValidator validator;
    private MemberCreateService memberCreateService;

    public MemberCreateServiceTest() {
        dateUtils = mock(DateUtils.class);
        memberInviteCodeRepository = mock(MemberInviteCodeRepository.class);
        memberRepository = mock(MemberRepository.class);
        validator = new MemberCreateRequestValidator();
        memberCreateService = new MemberCreateService(dateUtils, new BCryptPasswordEncoder(), memberInviteCodeRepository, memberRepository, validator);
    }

    @DisplayName("생성 요청 파라미터 유효성 오류")
    @Test
    void requestValueInvalid() {
        MemberCreateRequest memberCreateRequest = new MemberCreateRequest("", "qwer12!", "홍길동", null, "123456");

        assertThatThrownBy(() -> memberCreateService.create(memberCreateRequest))
                .isInstanceOf(ValidationErrorException.class)
                .hasMessage("Request has invalid values")
                .hasFieldOrPropertyWithValue("errors", List.of(
                        ValidationError.of("memberLoginId", ValidationCode.NO_TEXT.getValue()),
                        ValidationError.of("memberPassword", ValidationCode.LENGTH.getValue()),
                        ValidationError.of("memberMobile", ValidationCode.NO_TEXT.getValue())
                ));
    }

    @DisplayName("초대 코드가 존재하지 않는 경우")
    @Test
    void notFountInviteCode() {
        MemberCreateRequest memberCreateRequest = new MemberCreateRequest("loginId", "qwer1234!@", "홍길동", "01012345678", "123456");

        when(memberInviteCodeRepository.findByCode(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> memberCreateService.create(memberCreateRequest))
                .isInstanceOf(InviteCodeNotFoundException.class)
                .hasMessage("not found inviteCode");
    }

    @DisplayName("초대 코드가 만료된 경우")
    @Test
    void expiredInviteCode () {
        MemberCreateRequest memberCreateRequest = new MemberCreateRequest("loginId", "qwer1234!@", "홍길동", "01012345678", "123456");
        MemberInvite memberInvite = MemberInvite.of(Mobile.of("01012345678"),
                "123456",
                LocalDateTime.of(2023, 8, 7, 20, 15, 0));

        when(dateUtils.today()).thenReturn(LocalDateTime.of(2023, 8, 7, 20, 15, 1));
        when(memberInviteCodeRepository.findByCode(anyString())).thenReturn(Optional.of(memberInvite));

        assertThatThrownBy(() -> memberCreateService.create(memberCreateRequest))
                .isInstanceOf(ExpiredInviteCodeException.class)
                .hasMessage("expired inviteCode");
    }

    @DisplayName("회원 생성")
    @Test
    void createMemberSuccess() {
        MemberCreateRequest memberCreateRequest = new MemberCreateRequest("loginId", "qwer1234!@", "홍길동", "01012345678", "123456");
        MemberInvite memberInvite = MemberInvite.of(Mobile.of("01012345678"), "123456", LocalDateTime.of(2023, 8, 7, 20, 15, 0));

        when(dateUtils.today()).thenReturn(LocalDateTime.of(2023, 8, 7, 20, 15, 0));
        when(memberInviteCodeRepository.findByCode(anyString())).thenReturn(Optional.of(memberInvite));

        memberCreateService.create(memberCreateRequest);

        ArgumentCaptor<Member> memberCaptor = ArgumentCaptor.forClass(Member.class);
        BDDMockito.then(memberRepository).should().save(memberCaptor.capture());

        Member savedMember = memberCaptor.getValue();
        assertThat(savedMember.getLoginId()).isEqualTo("loginId");
        assertThat(savedMember.getPassword().match("qwer1234!@", new BCryptPasswordEncoder())).isTrue();
        assertThat(savedMember.getMobile().toString()).isEqualTo("01012345678");
        assertThat(savedMember.getName()).isEqualTo("홍길동");

        Set<String> authoritySet = savedMember.getAuthorities()
                .stream()
                .map(MemberAuthority::getAuthority)
                .collect(Collectors.toSet());

        assertThat(authoritySet).hasSize(1);
        assertThat(authoritySet).containsOnly(AuthorityEnum.ROLE_MEMBER.getName());
    }
}
