package com.dino.chat.api.friend.command.domain;

import com.dino.chat.api.common.entity.BaseEntity;
import com.dino.chat.api.member.command.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class FriendRequest extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member me;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member friend;

    @Enumerated(EnumType.STRING)
    private FriendRequestState state;

    private FriendRequest(Member me, Member friend, FriendRequestState state) {
        this.me = me;
        this.friend = friend;
        this.state = state;
    }

    public static FriendRequest of(Member me, Member friend, FriendRequestState state){
        return new FriendRequest(me, friend, state);
    }

    public void accept(){
        checkWaitState();
        this.state = FriendRequestState.ACCEPT;
    }

    public void reject() {
        checkWaitState();
        this.state = FriendRequestState.REJECT;
    }

    private void checkWaitState(){
        if(this.state != FriendRequestState.WAIT){
            throw new IllegalStateException("");
        }
    }
}
