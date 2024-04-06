package com.dino.chat.api.authority.command.domain.repositoy;


import com.dino.chat.api.authority.command.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    List<Authority> findAllByIsActive(boolean isActive);
    List<Authority> findAllByNameInAndIsActive(Collection<String> names, boolean isActive);
}
