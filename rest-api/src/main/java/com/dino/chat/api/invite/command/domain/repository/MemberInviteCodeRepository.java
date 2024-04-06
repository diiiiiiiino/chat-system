package com.dino.chat.api.invite.command.domain.repository;

import com.dino.chat.api.invite.command.domain.MemberInvite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberInviteCodeRepository extends JpaRepository<MemberInvite, Long> {
    Optional<MemberInvite> findByCode(String code);
}
