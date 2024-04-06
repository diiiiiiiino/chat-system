package com.dino.chat.api.friend.command.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FriendRequestState {
    WAIT("WAIT"),
    ACCEPT("ACCEPT"),
    REJECT("REJECT");

    private String value;
}
