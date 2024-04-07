package com.dino.chat.api.member.command.application.service;

import com.dino.chat.api.helper.builder.MemberCreateHelperBuilder;
import com.dino.chat.api.member.command.application.exception.HouseHoldNotFoundException;
import com.dino.chat.api.member.command.application.exception.MemberNotFoundException;
import com.dino.chat.api.member.command.domain.Member;
import com.dino.chat.api.member.command.domain.enumeration.MemberState;
import com.dino.chat.api.member.command.domain.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MemberWithdrawServiceTest {

    private MemberRepository memberRepository;
    private MemberWithdrawService memberWithdrawService;

    public MemberWithdrawServiceTest() {
        memberRepository = mock(MemberRepository.class);
        this.memberWithdrawService = new MemberWithdrawService(memberRepository);
    }

    @DisplayName("탈퇴 하려는 회원이 존재하지 않을 때")
    @Test
    void withDrawMemberNotFound() {
        Member member = MemberCreateHelperBuilder.builder().build();
        assertThatThrownBy(() -> memberWithdrawService.withDraw(member))
                .isInstanceOf(MemberNotFoundException.class)
                .hasMessage("Member not found");
    }

    @DisplayName("회원 탈퇴 성공")
    @Test
    void memberWithdraw() {
        Member member = MemberCreateHelperBuilder.builder().id(1L).build();

        when(memberRepository.findByLoginIdAndState(anyString(), any(MemberState.class))).thenReturn(Optional.of(member));

        memberWithdrawService.withDraw(member);

        assertThat(member.getState()).isEqualTo(MemberState.DEACTIVATION);
    }
}
