package com.dino.chat.api.member.command.domain.repository;

import com.dino.chat.api.member.command.domain.Member;
import com.dino.chat.api.member.command.domain.enumeration.MemberState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByLoginIdAndState(String loginId, MemberState state);
}
