package com.petplate.petplate.user.repository;

import com.petplate.petplate.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {

}
