package com.dino.chat.api.friend.command.domain.repository;

import com.dino.chat.api.friend.command.domain.Friend;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendRepository extends JpaRepository<Friend, Long> {
}
