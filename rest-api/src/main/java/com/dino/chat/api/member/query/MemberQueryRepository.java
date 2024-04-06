package com.dino.chat.api.member.query;

import com.dino.chat.api.member.command.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberQueryRepository extends JpaRepository<Member, Long> {
    @Query(value = " select m.loginId, " +
                   "        m.name, " +
                   "        m.mobile " +
                    " from Member m " +
                    " where m.id = :memberId ")
    Optional<MemberDto> findByMemberId(Long memberId);
}
