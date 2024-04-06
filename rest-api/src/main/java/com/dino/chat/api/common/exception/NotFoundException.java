package com.dino.chat.api.common.exception;

/**
 * 데이터가 존재하지 않을 경우 발생하는 예외
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
