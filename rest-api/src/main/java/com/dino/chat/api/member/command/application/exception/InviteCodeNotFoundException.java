package com.dino.chat.api.member.command.application.exception;

import com.dino.chat.api.common.exception.ApplicationException;
import com.dino.chat.api.common.http.response.ErrorCode;

/**
 * 초대코드 미조회 예외
 */
public class InviteCodeNotFoundException extends ApplicationException {
    public InviteCodeNotFoundException(String message) {
        super(message, ErrorCode.INVITE_CODE_NOT_FOUND);
    }
}
