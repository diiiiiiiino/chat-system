package com.dino.chat.api.friend.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendRequestQueryService {

    private final FriendRequestQueryRepository friendRequestQueryRepository;

    @Transactional(readOnly = true)
    public List<FriendRequestDto> findAllByMemberId(Long memberId){
        return friendRequestQueryRepository.findAllByMemberId(memberId);
    }
}
