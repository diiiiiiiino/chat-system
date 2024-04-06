package com.dino.chat.api.friend.query;

import com.dino.chat.api.friend.command.domain.FriendRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRequestQueryRepository extends JpaRepository<FriendRequest, Long> {

    @Query(value = " select fr.id " +
            " from FriendRequest fr" +
            " join fr.me m " +
            " where m = :memberId" +
            "   and fr.state = \"WAIT\" ")
    List<FriendRequestDto> findAllByMemberId(Long memberId);
}
