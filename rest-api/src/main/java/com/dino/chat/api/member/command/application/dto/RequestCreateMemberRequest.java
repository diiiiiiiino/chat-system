package com.dino.chat.api.member.command.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RequestCreateMemberRequest {
    String mobile;

    public static RequestCreateMemberRequest of(String mobile) {
        return new RequestCreateMemberRequest(mobile);
    }
}
