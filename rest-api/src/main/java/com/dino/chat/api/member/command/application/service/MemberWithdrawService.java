package com.dino.chat.api.member.command.application.service;

import com.dino.chat.api.member.command.application.exception.HouseHoldNotFoundException;
import com.dino.chat.api.member.command.application.exception.MemberNotFoundException;
import com.dino.chat.api.member.command.domain.Member;
import com.dino.chat.api.member.command.domain.enumeration.MemberState;
import com.dino.chat.api.member.command.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 회원 탈퇴 서비스
 */
@Service
@RequiredArgsConstructor
public class MemberWithdrawService {

    private final MemberRepository memberRepository;

    /**
     * 회원 탈퇴
     * @param member 회원
     * @throws MemberNotFoundException 회원 미조회
     * @throws HouseHoldNotFoundException 세대 미조회
     */
    @Transactional
    public void withDraw(Member member) {
        member = memberRepository.findByLoginIdAndState(member.getLoginId(), MemberState.ACTIVATION)
                .orElseThrow(() -> new MemberNotFoundException("Member not found"));

        member.updateState(MemberState.DEACTIVATION);
    }
}
