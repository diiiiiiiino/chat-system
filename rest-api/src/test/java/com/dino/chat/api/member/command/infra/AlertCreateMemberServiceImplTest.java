package com.dino.chat.api.member.command.infra;

import com.dino.chat.api.common.exception.CustomIllegalArgumentException;
import com.dino.chat.api.member.command.application.service.AlertCreateMemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

public class AlertCreateMemberServiceImplTest {

    private AlertCreateMemberService alertCreateMemberServiceImpl;

    public AlertCreateMemberServiceImplTest() {
        alertCreateMemberServiceImpl = new AlertCreateMemberServiceImpl();
    }

    @DisplayName("전화번호가 null 또는 빈 문자열일때")
    @ParameterizedTest
    @NullAndEmptySource
    void mobileNullAndEmpty(String mobile) {
        String inviteCode = "123456";

        Assertions.assertThatThrownBy(() -> alertCreateMemberServiceImpl.alert(mobile, inviteCode))
                .isInstanceOf(CustomIllegalArgumentException.class)
                .hasMessage("memberMobile has no text");
    }

    @DisplayName("초대코드가 null 또는 빈 문자열일때")
    @ParameterizedTest
    @NullAndEmptySource
    void inviteCodeNullAndEmpty(String inviteCode) {
        String mobile = "01012345678";

        Assertions.assertThatThrownBy(() -> alertCreateMemberServiceImpl.alert(mobile, inviteCode))
                .isInstanceOf(CustomIllegalArgumentException.class)
                .hasMessage("memberInviteCode has no text");
    }
}
