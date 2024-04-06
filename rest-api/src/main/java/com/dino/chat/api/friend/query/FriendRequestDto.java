package com.dino.chat.api.friend.query;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FriendRequestDto {
    private Long id;

    public static FriendRequestDto of(Long id){
        return new FriendRequestDto(id);
    }
}
