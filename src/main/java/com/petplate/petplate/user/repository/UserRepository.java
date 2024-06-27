package com.petplate.petplate.user.repository;

import com.petplate.petplate.user.domain.SocialType;
import com.petplate.petplate.user.domain.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User,Long> {

    @Query("select u from User u where u.socialType=:socialType and u.username=:username")
    Optional<User> findBySocialTypeAndUsername(@Param("socialType")SocialType socialType,@Param("username") String username);

    Optional<User> findByUsername(String username);

}
