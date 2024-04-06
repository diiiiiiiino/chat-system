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
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Friend extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "me")
    private Member me;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friend")
    private Member friend;

    private Friend(Member me, Member friend) {
        this.me = me;
        this.friend = friend;
    }

    public static Friend of(Member me, Member friend){
        return new Friend(me, friend);
    }
}
