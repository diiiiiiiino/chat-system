package com.dino.chat.api.infra.security.jwt;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JWTVerifyResult {
    private boolean isVerify;
    private String loginId;
}
