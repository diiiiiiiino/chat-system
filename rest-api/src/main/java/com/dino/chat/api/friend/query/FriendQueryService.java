package com.dino.chat.api.friend.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendQueryService {

    private final FriendQueryRepository friendQueryRepository;

    @Transactional(readOnly = true)
    public List<FriendDto> findAllByMemberId(Long memberId){
        return friendQueryRepository.findAllByMemberId(memberId);
    }
}
