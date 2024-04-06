package com.dino.chat.api.member.command.application.service;

import com.dino.chat.api.common.exception.ValidationError;
import com.dino.chat.api.common.exception.ValidationErrorException;
import com.dino.chat.api.common.validator.RequestValidator;
import com.dino.chat.api.member.command.application.dto.MemberInfoChangeRequest;
import com.dino.chat.api.member.command.application.exception.MemberNotFoundException;
import com.dino.chat.api.member.command.application.validator.annotation.MemberInfoChangeRequestQualifier;
import com.dino.chat.api.member.command.domain.Member;
import com.dino.chat.api.member.command.domain.Mobile;
import com.dino.chat.api.member.command.domain.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 회원 정보 변경 서비스
 */
@Service
public class MemberInfoChangeService {

    private final MemberRepository memberRepository;
    private final RequestValidator<MemberInfoChangeRequest> validator;

    public MemberInfoChangeService(MemberRepository memberRepository,
                                   @MemberInfoChangeRequestQualifier RequestValidator<MemberInfoChangeRequest> validator) {
        this.memberRepository = memberRepository;
        this.validator = validator;
    }

    /**
     * 회원 정보 변경
     * @param memberId 회원 ID
     * @param request 회원 정보 변경 요청
     * @throws MemberNotFoundException 회원 미조회
     * @throws ValidationErrorException
     * <ul>
     *     <li>{@code name}이 {@code null}이거나 문자가 없을 경우, 길이가 1~15 아닌 경우
     *     <li>{@code mobile}이 {@code null}이거나 문자가 없을 경우, 길이가 11자리가 아닌 경우
     * </ul>
     */
    @Transactional
    public void change(Long memberId, MemberInfoChangeRequest request) {
        List<ValidationError> errors = validator.validate(request);
        RequestValidator.validateId(memberId, "memberId", errors);

        if(!errors.isEmpty()){
            throw new ValidationErrorException("Request has invalid values", errors);
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("Member not found"));

        member.changeName(request.getName());
        member.changeMobile(Mobile.of(request.getMobile()));
    }
}
