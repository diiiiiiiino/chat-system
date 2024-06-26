package com.dino.chat.api.login.command.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginRecordRepository extends JpaRepository<LoginRecord, Long> {
}
