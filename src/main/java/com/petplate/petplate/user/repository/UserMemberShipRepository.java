package com.petplate.petplate.user.repository;

import com.petplate.petplate.user.domain.entity.UserMemberShip;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserMemberShipRepository extends JpaRepository<UserMemberShip, Long> {
    boolean existsByUserId(Long userId);
    boolean existsByUserUsername(String username);
}
