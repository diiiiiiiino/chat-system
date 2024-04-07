package com.dino.chat.api.login.command.application;

import com.dino.chat.api.helper.builder.MemberCreateHelperBuilder;
import com.dino.chat.api.login.command.application.service.LoginRecordServiceImpl;
import com.dino.chat.api.login.command.domain.LoginRecord;
import com.dino.chat.api.login.command.domain.LoginRecordRepository;
import com.dino.chat.api.member.command.application.exception.MemberNotFoundException;
import com.dino.chat.api.member.command.domain.Member;
import com.dino.chat.api.member.command.domain.enumeration.MemberState;
import com.dino.chat.api.member.command.domain.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class LoginRecordServiceTest {
    LoginRecordServiceImpl loginService;
    MemberRepository memberRepository;
    LoginRecordRepository loginRecordRepository;

    public LoginRecordServiceTest() {
        memberRepository = mock(MemberRepository.class);
        loginRecordRepository = mock(LoginRecordRepository.class);
        loginService = new LoginRecordServiceImpl(memberRepository, loginRecordRepository);
    }

    @DisplayName("로그인 시 회원 미조회")
    @Test
    void memberNotfoundWhenLoggingIn() {
        Member member = MemberCreateHelperBuilder.builder().build();

        when(memberRepository.findByLoginIdAndState(anyString(), any(MemberState.class))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> loginService.loginRecord(member, "userAgent"))
                .isInstanceOf(MemberNotFoundException.class)
                .hasMessage("Member not found");
    }

    @DisplayName("로그인 성공")
    @Test
    void loginSuccess() {
        Member member = MemberCreateHelperBuilder.builder().build();
        when(memberRepository.findByLoginIdAndState(anyString(), any(MemberState.class))).thenReturn(Optional.of(member));

        loginService.loginRecord(member, "userAgent");

        verify(loginRecordRepository, times(1)).save(any(LoginRecord.class));
    }
}
