package com.dino.chat.api.friend.command.domain.repository;

import com.dino.chat.api.friend.command.domain.FriendRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
}
