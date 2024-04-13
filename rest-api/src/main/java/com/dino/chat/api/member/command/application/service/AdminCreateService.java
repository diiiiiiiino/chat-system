package com.dino.chat.api.member.command.application.service;

import com.dino.chat.api.common.exception.ValidationError;
import com.dino.chat.api.common.exception.ValidationErrorException;
import com.dino.chat.api.common.validator.RequestValidator;
import com.dino.chat.api.member.command.application.dto.AdminCreateRequest;
import com.dino.chat.api.member.command.application.dto.MemberCreateRequest;
import com.dino.chat.api.member.command.application.validator.annotation.MemberCreateRequestQualifier;
import com.dino.chat.api.member.command.domain.Member;
import com.dino.chat.api.member.command.domain.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 관리자 생성 서비스
 */
@Service
public class AdminCreateService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final RequestValidator<MemberCreateRequest> requestValidator;

    public AdminCreateService(MemberRepository memberRepository,
                              PasswordEncoder passwordEncoder,
                              @MemberCreateRequestQualifier RequestValidator<MemberCreateRequest> requestValidator) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.requestValidator = requestValidator;
    }

    /**
     * 관리자 생성
     * @param request 관리자 생성 요청 데이터
     * @throws ValidationErrorException
     * <ul>
     *     <li>{@code request}가 {@code null}인 경우
     *     <li>{@code memberCreateRequest}가 {@code null}인 경우
     *     <li>{@code buildingInfo}가 {@code null}인 경우
     *     <li>{@code houseHoldInfos}가 {@code null}이거나 비어있는 경우
     *     <li>{@code loginId}가 {@code null}이거나 문자가 없을 경우, 길이가 1~15 아닌 경우
     *     <li>{@code password}가 {@code null}이거나 문자가 없을 경우, 길이가 8~16 아닌 경우
     *     <li>{@code name}이 {@code null}이거나 문자가 없을 경우, 길이가 1~15 아닌 경우
     *     <li>{@code mobile}이 {@code null}이거나 문자가 없을 경우, 길이가 11자리가 아닌 경우
     *     <li>{@code inviteCode}가 {@code null}이거나 문자가 없을 경우, 길이가 6자리가 아닌 경우
     *     <li>{@code houseHoldId}가 {@code null}인 경우
     *     <li>{@code buildingInfo address1}이 {@code null}이거나 문자가 없을 경우, 길이가 1 ~ 50이 아닌 경우
     *     <li>{@code buildingInfo address2}이 {@code null}이거나 문자가 없을 경우, 길이가 1 ~ 50이 아닌 경우
     *     <li>{@code buildingInfo zipNo}이 {@code null}이거나 문자가 없을 경우, 길이가 5가 아닌 경우
     *     <li>{@code buildingInfo name}이 {@code null}이거나 문자가 없을 경우, 길이가 1 ~ 20이 아닌 경우
     *     <li>{@code houseHoldInfos}에 {@code isChecked}가 {@code true}인게 1개가 아닐 경우</li>
     *     <li>{@code houseHoldInfos}에 {@code room}이 {@code null}이거나 문자가 없는게 하나라도 있을 경우</li>     
     *     <li>{@code houseHoldInfos}에 {@code room}이 길이가 1 ~ 6이 아닌 경우</li>
     * </ul>
     */
    @Transactional
    public void create(MemberCreateRequest request) {
        List<ValidationError> errors = requestValidator.validate(request);
        if(!errors.isEmpty()){
            throw new ValidationErrorException("Request has invalid values", errors);
        }

        Member admin = MemberCreateRequest.newAdmin(request, passwordEncoder);

        memberRepository.save(admin);
    }
}
