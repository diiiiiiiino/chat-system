package com.dino.chat.api.friend.query;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FriendDto {
    private Long friendId;
    private String name;

    public static FriendDto of(Long friendId, String name){
        return new FriendDto(friendId, name);
    }
}
