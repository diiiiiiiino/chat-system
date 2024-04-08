package com.dino.chat.api.member.command.application.service;

import com.dino.chat.api.common.exception.CustomIllegalArgumentException;
import com.dino.chat.api.common.exception.ValidationErrorException;
import com.dino.chat.api.invite.command.domain.repository.MemberInviteCodeRepository;
import com.dino.chat.api.member.command.application.dto.RequestCreateMemberRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

public class RequestCreateMemberServiceTest {

    private MemberInviteCodeRepository memberInviteCodeRepository;
    private AlertCreateMemberService alertCreateMemberService;
    private RequestCreateMemberService requestCreateMemberService;

    public RequestCreateMemberServiceTest() {
        memberInviteCodeRepository = mock(MemberInviteCodeRepository.class);
        alertCreateMemberService = mock(AlertCreateMemberService.class);
        requestCreateMemberService = new RequestCreateMemberService(memberInviteCodeRepository, alertCreateMemberService);
    }

    @DisplayName("회원 생성 요청 목록이 null 또는 비어있을 때")
    @ParameterizedTest
    @NullAndEmptySource
    void requestNullAndEmpty(List<RequestCreateMemberRequest> requests) {
        assertThatThrownBy(() -> requestCreateMemberService.request(requests))
                .isInstanceOf(CustomIllegalArgumentException.class)
                .hasMessage("requests no element");
    }

    @DisplayName("회원 생성 요청에 null 또는 빈 문자열이 포함되어 있을 때")
    @ParameterizedTest
    @MethodSource("nullAndEmptyCreateMemberRequestList")
    void includeNullAndEmptyValue(List<RequestCreateMemberRequest> requests) {
        assertThatThrownBy(() -> requestCreateMemberService.request(requests))
                .isInstanceOf(ValidationErrorException.class)
                .hasMessage("list has null or empty value");
    }

    @DisplayName("회원 생성 요청에 전화번호 길이가 11자리가 아닐경우")
    @ParameterizedTest
    @MethodSource("mobileLengthNotElevenList")
    void mobileLengthIsNotEleven(List<RequestCreateMemberRequest> requests) {
        assertThatThrownBy(() -> requestCreateMemberService.request(requests))
                .isInstanceOf(ValidationErrorException.class)
                .hasMessage("mobile length is not 11");
    }

    @DisplayName("회원 생성 요청")
    @Test
    void requestCreateMemberSuccess() {
        List<RequestCreateMemberRequest> requests = List.of(RequestCreateMemberRequest.of("01044445555"), RequestCreateMemberRequest.of("01012345678"));

        requestCreateMemberService.request(requests);

        verify(alertCreateMemberService, times(2)).alert(anyString(), anyString());
    }

    private static Stream<Arguments> nullAndEmptyCreateMemberRequestList(){
        return Stream.of(
                Arguments.of(List.of(RequestCreateMemberRequest.of("01012345678"), RequestCreateMemberRequest.of(" "))),
                Arguments.of(List.of(RequestCreateMemberRequest.of("01012345678"), RequestCreateMemberRequest.of(null))),
                Arguments.of(List.of(RequestCreateMemberRequest.of("01012345678"), RequestCreateMemberRequest.of("")))
        );
    }

    private static Stream<Arguments> mobileLengthNotElevenList(){
        return Stream.of(
                Arguments.of(List.of(RequestCreateMemberRequest.of("01012345678"), RequestCreateMemberRequest.of("0101234567"))),
                Arguments.of(List.of(RequestCreateMemberRequest.of("01012345678"), RequestCreateMemberRequest.of("010"))),
                Arguments.of(List.of(RequestCreateMemberRequest.of("01012345678"), RequestCreateMemberRequest.of("0")))
        );
    }
}
