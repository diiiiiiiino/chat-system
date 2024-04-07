package com.dino.chat.api.member.presentation.controller;

import com.dino.chat.api.common.exception.ValidationCode;
import com.dino.chat.api.common.exception.ValidationError;
import com.dino.chat.api.common.exception.ValidationErrorException;
import com.dino.chat.api.helper.BaseControllerTest;
import com.dino.chat.api.member.command.application.dto.MemberCreateRequest;
import com.dino.chat.api.member.command.application.dto.MemberInfoChangeRequest;
import com.dino.chat.api.member.command.application.dto.PasswordChangeRequest;
import com.dino.chat.api.member.command.application.exception.ExpiredInviteCodeException;
import com.dino.chat.api.member.command.application.exception.HouseHoldNotFoundException;
import com.dino.chat.api.member.command.application.exception.InviteCodeNotFoundException;
import com.dino.chat.api.member.command.application.exception.MemberNotFoundException;
import com.dino.chat.api.member.command.application.service.MemberCreateService;
import com.dino.chat.api.member.command.application.service.MemberInfoChangeService;
import com.dino.chat.api.member.command.application.service.MemberWithdrawService;
import com.dino.chat.api.member.command.application.service.PasswordChangeService;
import com.dino.chat.api.member.command.domain.exception.PasswordNotMatchedException;
import com.dino.chat.api.member.command.domain.exception.PasswordOutOfConditionException;
import com.dino.chat.api.member.command.domain.exception.UpdatePasswordSameException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
public class MemberControllerTest extends BaseControllerTest {

    @MockBean
    private MemberCreateService memberCreateService;

    @MockBean
    private MemberInfoChangeService memberInfoChangeService;

    @MockBean
    private PasswordChangeService passwordChangeService;

    @MockBean
    private MemberWithdrawService memberWithdrawService;

    @BeforeEach
    void beforeEach() throws Exception {
        login("abcde", "qwer1234!@");
    }

    @DisplayName("회원 생성 시 유효성 체크")
    @Test
    void whenMemberCreateThenInvalidRequest() throws Exception {
        MemberCreateRequest request = new MemberCreateRequest("loginId", "qwer1234!@", "홍길동", "01012345678", "123456");

        List<ValidationError> errors = new ArrayList<>();
        errors.add(ValidationError.of("memberLoginId", ValidationCode.NO_TEXT.getValue()));
        errors.add(ValidationError.of("memberPassword", ValidationCode.NO_TEXT.getValue()));
        errors.add(ValidationError.of("houseHoldId", ValidationCode.NULL.getValue()));

        doThrow(new ValidationErrorException("Request has invalid values", errors))
                .when(memberCreateService).create(any(MemberCreateRequest.class));

        mvcPerform(post("/member"), request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isNotEmpty());
    }

    @DisplayName("회원 생성 시 초대코드 미조회")
    @Test
    void inviteCodeNotFound() throws Exception {
        MemberCreateRequest request = new MemberCreateRequest("loginId", "qwer1234!@", "홍길동", "01012345678", "123456");

        doThrow(new InviteCodeNotFoundException("not found inviteCode"))
                .when(memberCreateService).create(any(MemberCreateRequest.class));

        mvcPerform(post("/member"), request)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("InviteCodeNotFound"));
    }

    @DisplayName("회원 생성 시 초대코드 만료")
    @Test
    void inviteCodeExpired() throws Exception {
        MemberCreateRequest request = new MemberCreateRequest("loginId", "qwer1234!@", "홍길동", "01012345678", "123456");

        doThrow(new ExpiredInviteCodeException("expired inviteCode"))
                .when(memberCreateService).create(any(MemberCreateRequest.class));

        mvcPerform(post("/member"), request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("InviteCodeExpired"));
    }

    @DisplayName("회원 생성 시 비밀번호 정책에 맞지 않을 때")
    @Test
    void passwordInvalid() throws Exception {
        MemberCreateRequest request = new MemberCreateRequest("loginId", "qwer1234", "홍길동", "01012345678", "123456");

        doThrow(new PasswordOutOfConditionException("Has no special characters"))
                .when(memberCreateService).create(any(MemberCreateRequest.class));

        mvcPerform(post("/member"), request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("PasswordOutOfCondition"));
    }

    @DisplayName("회원 생성 성공")
    @Test
    void memberCreateSuccess() throws Exception {
        MemberCreateRequest request = new MemberCreateRequest("loginId", "qwer1234!@", "홍길동", "01012345678", "123456");

        mvcPerform(post("/member"), request)
                .andExpect(status().isOk());
    }

    @DisplayName("회원 정보 수정 시 유효성 에러")
    @Test
    void whenMemberUpdateThenInvalidRequest() throws Exception {
        MemberInfoChangeRequest request = MemberInfoChangeRequest.builder()
                .name("홍길동")
                .mobile("01012345678")
                .build();

        List<ValidationError> errors = new ArrayList<>();
        errors.add(ValidationError.of("memberName", ValidationCode.NO_TEXT.getValue()));
        errors.add(ValidationError.of("memberMobile", ValidationCode.NO_TEXT.getValue()));

        doThrow(new ValidationErrorException("Request has invalid values", errors))
                .when(memberInfoChangeService).change(any(), any(MemberInfoChangeRequest.class));

        mvcPerform(patch("/member"), request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isNotEmpty());
    }

    @DisplayName("회원 정보 수정 시 회원 미조회")
    @Test
    void whenMemberUpdateThenMemberNotFound() throws Exception {
        MemberInfoChangeRequest request = MemberInfoChangeRequest.builder()
                .name("홍길동")
                .mobile("01012345678")
                .build();

        doThrow(new MemberNotFoundException("Member not found"))
                .when(memberInfoChangeService).change(any(), any(MemberInfoChangeRequest.class));

        mvcPerform(patch("/member"), request)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("MemberNotFound"));
    }

    @DisplayName("회원 비밀번호 수정 시 유효성 에러")
    @Test
    void whenPasswordUpdateThenInvalidRequest() throws Exception {
        PasswordChangeRequest request = PasswordChangeRequest.builder()
                .orgPassword("qwer1234!!")
                .newPassword("qwer1234@@")
                .build();

        List<ValidationError> errors = new ArrayList<>();
        errors.add(ValidationError.of("memberOrgPassword", ValidationCode.NO_TEXT.getValue()));

        doThrow(new ValidationErrorException("has no text", errors))
                .when(passwordChangeService).change(any(), any(PasswordChangeRequest.class));

        mvcPerform(patch("/member/password"), request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isNotEmpty());
    }

    @DisplayName("회원 비밀번호 수정 시 회원 미조회")
    @Test
    void whenPasswordUpdateThenMemberNotFound() throws Exception {
        PasswordChangeRequest request = PasswordChangeRequest.builder()
                .orgPassword("qwer1234!!")
                .newPassword("qwer1234@@")
                .build();

        doThrow(new MemberNotFoundException("Member not found"))
                .when(passwordChangeService).change(any(), any(PasswordChangeRequest.class));

        mvcPerform(patch("/member/password"), request)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("MemberNotFound"));
    }

    @DisplayName("회원 비밀번호 수정 시 비밀번호가 틀렸을 경우")
    @Test
    void whenPasswordUpdateThenPasswordNotMatched() throws Exception {
        PasswordChangeRequest request = PasswordChangeRequest.builder()
                .orgPassword("qwer1234!!")
                .newPassword("qwer1234@@")
                .build();

        doThrow(new PasswordNotMatchedException("password is not matched"))
                .when(passwordChangeService).change(any(), any(PasswordChangeRequest.class));

        mvcPerform(patch("/member/password"), request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("PasswordNotMatched"));
    }

    @DisplayName("회원 비밀번호 수정 시 기존 비밀번호와 변경 비밀번호가 같을때")
    @Test
    void whenPasswordUpdateThenPasswordSame() throws Exception {
        PasswordChangeRequest request = PasswordChangeRequest.builder()
                .orgPassword("qwer1234!!")
                .newPassword("qwer1234@@")
                .build();

        doThrow(new UpdatePasswordSameException("origin and update password same"))
                .when(passwordChangeService).change(any(), any(PasswordChangeRequest.class));

        mvcPerform(patch("/member/password"), request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("UpdatePasswordSame"));
    }

    @DisplayName("회원 비밀번호 수정 성공")
    @Test
    void whenPasswordUpdateThenSuccess() throws Exception {
        PasswordChangeRequest request = PasswordChangeRequest.builder()
                .orgPassword("qwer1234!!")
                .newPassword("qwer1234@@")
                .build();

        mvcPerform(patch("/member/password"), request)
                .andExpect(status().isOk());
    }

    @DisplayName("회원 탈퇴 시 회원 미조회")
    @Test
    void whenMemberWithdrawThenMemberNotFound() throws Exception {
        doThrow(new MemberNotFoundException("Member not found"))
                .when(memberWithdrawService).withDraw(any());

        mvcPerform(post("/member/withdraw"), null)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("MemberNotFound"));
    }

    @DisplayName("회원 탈퇴 성공")
    @Test
    void whenMemberWithdrawSuccess() throws Exception {
        mvcPerform(post("/member/withdraw"), null)
                .andExpect(status().isOk());
    }
}
