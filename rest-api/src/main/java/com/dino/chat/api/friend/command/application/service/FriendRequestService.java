package com.dino.chat.api.friend.command.application.service;

import com.dino.chat.api.common.exception.NotFoundException;
import com.dino.chat.api.friend.command.domain.Friend;
import com.dino.chat.api.friend.command.domain.FriendRequest;
import com.dino.chat.api.friend.command.domain.FriendRequestState;
import com.dino.chat.api.friend.command.domain.repository.FriendRepository;
import com.dino.chat.api.friend.command.domain.repository.FriendRequestRepository;
import com.dino.chat.api.member.command.application.exception.MemberNotFoundException;
import com.dino.chat.api.member.command.domain.Member;
import com.dino.chat.api.member.command.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendRequestService {
    private final FriendRepository friendRepository;
    private final FriendRequestRepository friendRequestRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void request(Member me, Long friendId){
        Member friend = memberRepository.findById(friendId)
                        .orElseThrow(() -> new MemberNotFoundException("Friend not found"));
        
        friendRequestRepository.save(FriendRequest.of(me, friend, FriendRequestState.ACCEPT));
        
        //todo : 알림 시스템 연동
    }

    @Transactional
    public void accept(Member me, Long id) {
        FriendRequest request = friendRequestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Request not found"));

        request.accept();

        Friend friend = Friend.of(me, request.getMe());
        Friend friend2 = Friend.of(request.getMe(), me);

        friendRepository.saveAll(List.of(friend, friend2));
    }

    @Transactional
    public void reject(Long id) {
        FriendRequest request = friendRequestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Request not found"));

        request.reject();
    }
}
