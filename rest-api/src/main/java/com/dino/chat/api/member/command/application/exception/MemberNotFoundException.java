package com.dino.chat.api.member.command.application.exception;


import com.dino.chat.api.common.exception.ApplicationException;
import com.dino.chat.api.common.http.response.ErrorCode;

/**
 * 회원 미조회 예외
 */
public class MemberNotFoundException extends ApplicationException {
    public MemberNotFoundException(String message) {
        super(message, ErrorCode.MEMBER_NOT_FOUND);
    }
}
