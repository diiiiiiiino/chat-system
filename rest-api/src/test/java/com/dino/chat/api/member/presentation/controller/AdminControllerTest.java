package com.dino.chat.api.member.presentation.controller;

import com.dino.chat.api.authority.command.application.exception.AuthorityNotFoundException;
import com.dino.chat.api.authority.command.domain.Authority;
import com.dino.chat.api.authority.command.domain.enumeration.AuthorityEnum;
import com.dino.chat.api.common.exception.CustomIllegalArgumentException;
import com.dino.chat.api.common.exception.ValidationCode;
import com.dino.chat.api.common.exception.ValidationError;
import com.dino.chat.api.common.exception.ValidationErrorException;
import com.dino.chat.api.common.http.response.ErrorCode;
import com.dino.chat.api.helper.BaseControllerTest;
import com.dino.chat.api.helper.builder.MemberCreateHelperBuilder;
import com.dino.chat.api.member.command.application.dto.AdminCreateRequest;
import com.dino.chat.api.member.command.application.dto.MemberCreateRequest;
import com.dino.chat.api.member.command.application.dto.RequestCreateMemberRequest;
import com.dino.chat.api.member.command.application.exception.MemberNotFoundException;
import com.dino.chat.api.member.command.application.service.AdminChangeService;
import com.dino.chat.api.member.command.application.service.AdminCreateService;
import com.dino.chat.api.member.command.application.service.RequestCreateMemberService;
import com.dino.chat.api.member.command.domain.Member;
import com.dino.chat.api.member.command.domain.MemberAuthority;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
public class AdminControllerTest extends BaseControllerTest {

    @MockBean
    private AdminCreateService adminCreateService;

    @MockBean
    private AdminChangeService adminChangeService;

    @MockBean
    private RequestCreateMemberService requestCreateMemberService;

    @BeforeEach
    void beforeEach() throws Exception {
        login("admin", "qwer1234!@");
    }

    @DisplayName("관리자 생성 요청이 비정상적일 때")
    @Test
    void createAdminWithMissMemberCreateRequest() throws Exception {
        List<ValidationError> errors = new ArrayList<>();
        errors.add(ValidationError.of("memberLoginId", ValidationCode.NO_TEXT.getValue()));
        errors.add(ValidationError.of("memberPassword", ValidationCode.LENGTH.getValue()));

        AdminCreateRequest adminCreateRequest = AdminCreateRequest.of(null, "qwer1234!@", "홍길동", "01012345678");

        doThrow(new ValidationErrorException("Request has invalid values", errors))
                .when(adminCreateService).create(any(AdminCreateRequest.class));

        mvcPerform(post("/admin"), adminCreateRequest)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isNotEmpty());
    }

    @DisplayName("관리자 생성")
    @Test
    void createAdminSuccess() throws Exception {
        AdminCreateRequest adminCreateRequest = AdminCreateRequest.of("loginId", "qwer1234!@", "홍길동", "01012345678");

        mvcPerform(post("/admin"), adminCreateRequest)
                .andExpect(status().isOk());
    }

    @DisplayName("관리자로 변경하려는 회원이 존재하지 않을 경우")
    @Test
    void targetMemberNotFound() throws Exception {
        doThrow(new MemberNotFoundException("Target member not found"))
                .when(adminChangeService).change(any(Member.class), anyLong());

        mvcPerform(patch("/admin/1"), null)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").isNotEmpty());
    }

    @DisplayName("변경하려는 권한이 비활성화 된 경우")
    @Test
    void changeAuthorityIsNonActive() throws Exception {
        doThrow(new AuthorityNotFoundException("Authority not found"))
                .when(adminChangeService).change(any(Member.class), anyLong());

        mvcPerform(patch("/admin/1"), null)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").isNotEmpty());
    }

    @DisplayName("관리자 권한 변경")
    @Test
    void changeAuthority() throws Exception {
        mvcPerform(patch("/admin/1"), null)
                .andExpect(status().isOk());
    }

    @DisplayName("회원 생성 요청 목록이 null일 때")
    @Test
    void requestNull() throws Exception {
        doThrow(new CustomIllegalArgumentException("requests no element", ErrorCode.INVALID_REQUEST))
                .when(requestCreateMemberService).request(anyList());

        mvcPerform(post("/admin/member"), null)
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorCode").value("ServerError"))
                .andDo(print());
    }

    @DisplayName("회원 생성 요청 목록이 null 또는 비어있을 때")
    @Test
    void requestNullAndEmpty() throws Exception {
        doThrow(new CustomIllegalArgumentException("requests no element", ErrorCode.INVALID_REQUEST))
                .when(requestCreateMemberService).request(anyList());

        mvcPerform(post("/admin/member"), List.of())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("InvalidRequest"))
                .andDo(print());
    }

    @DisplayName("회원 생성 요청에 null 또는 빈 문자열이 포함되어 있을 때")
    @Test
    void includeNullAndEmptyValue() throws Exception {
        doThrow(new ValidationErrorException("list has null or empty value"))
                .when(requestCreateMemberService).request(anyList());

        List<RequestCreateMemberRequest> requests = List.of(RequestCreateMemberRequest.of("01012345678"), RequestCreateMemberRequest.of(" "));

        mvcPerform(post("/admin/member"), requests)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("InvalidRequest"))
                .andDo(print());
    }

    @DisplayName("회원 생성 요청에 전화번호 길이가 11자리가 아닐경우")
    @Test
    void mobileLengthIsNotEleven() throws Exception {
        doThrow(new ValidationErrorException("mobile length is not 11"))
                .when(requestCreateMemberService).request(anyList());

        List<RequestCreateMemberRequest> requests = List.of(RequestCreateMemberRequest.of("01012345678"), RequestCreateMemberRequest.of("0101234567"));

        mvcPerform(post("/admin/member"), requests)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("InvalidRequest"))
                .andDo(print());
    }

    @DisplayName("회원 생성 요청")
    @Test
    void requestCreateMemberSuccess() throws Exception {
        List<RequestCreateMemberRequest> requests = List.of(RequestCreateMemberRequest.of("01044445555"), RequestCreateMemberRequest.of("01012345678"));

        mvcPerform(post("/admin/member"), requests)
                .andExpect(status().isOk())
                .andDo(print());
    }

    private Member createAdmin(){
        List<Function<Member, MemberAuthority>> functions = List.of(
                member -> MemberAuthority.of(member, Authority.of(AuthorityEnum.ROLE_ADMIN)),
                member -> MemberAuthority.of(member, Authority.of(AuthorityEnum.ROLE_MEMBER))
        );

        Member admin = MemberCreateHelperBuilder.builder()
                .id(1L)
                .loginId("admin")
                .name("관리자")
                .functions(functions)
                .build();

        return admin;
    }
}
