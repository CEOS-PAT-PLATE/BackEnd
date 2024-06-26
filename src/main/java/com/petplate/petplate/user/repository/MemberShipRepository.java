package com.petplate.petplate.user.repository;

import com.petplate.petplate.user.domain.entity.MemberShip;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberShipRepository extends JpaRepository<MemberShip, Long> {
}
