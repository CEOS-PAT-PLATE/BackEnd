package com.petplate.petplate.petfood.repository;

import com.petplate.petplate.petfood.domain.entity.BookMarkedFeed;
import com.petplate.petplate.petfood.domain.entity.BookMarkedRaw;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookMarkedFeedRepository extends JpaRepository<BookMarkedFeed, Long> {
    List<BookMarkedFeed> findByUserId(Long userId);
    List<BookMarkedFeed> findByUserUsername(String username);
    Optional<BookMarkedFeed> findByUserUsernameAndId(String username, Long bookMarkedFeedId);
    Optional<BookMarkedFeed> findByUserIdAndId(Long userId, Long bookMarkedFeedId);

    boolean existsByUserUsernameAndId(String username, Long bookMarkedFeedId);
    boolean existsByUserUsernameAndNameAndServing(String username, String name, double serving);
}
