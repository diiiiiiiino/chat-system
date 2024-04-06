package com.dino.chat.api.friend.query;

import com.dino.chat.api.friend.command.domain.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendQueryRepository extends JpaRepository<Friend, Long> {

    @Query(value = " select ff.id, ff.name " +
            " from Friend f" +
            " join f.friend ff " +
            " where f.me = :memberId ")
    List<FriendDto> findAllByMemberId(Long memberId);
}
