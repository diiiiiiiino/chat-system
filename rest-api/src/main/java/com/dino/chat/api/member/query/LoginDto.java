package com.dino.chat.api.member.query;

import com.dino.chat.api.member.command.domain.Member;
import lombok.Getter;

@Getter
public class LoginDto {
    private Member member;
    private Long houseHoldId;
    private Long buildingId;

    public LoginDto(Member member,
                    Long houseHoldId,
                    Long buildingId) {
        this.member = member;
        this.houseHoldId = houseHoldId;
        this.buildingId = buildingId;
    }

    public static LoginDto of(Member member,
                              Long houseHoldId,
                              Long buildingId){
        return new LoginDto(member, houseHoldId, buildingId);
    }
}