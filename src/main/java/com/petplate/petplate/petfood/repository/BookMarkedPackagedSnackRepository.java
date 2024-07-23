package com.petplate.petplate.petfood.repository;

import com.petplate.petplate.petfood.domain.entity.BookMarkedFeed;
import com.petplate.petplate.petfood.domain.entity.BookMarkedPackagedSnack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface BookMarkedPackagedSnackRepository extends JpaRepository<BookMarkedPackagedSnack, Long> {
    List<BookMarkedPackagedSnack> findByUserUsername(String username);
    Optional<BookMarkedPackagedSnack> findByUserUsernameAndId(String username, Long bookMarkedPackagedSnackId);
    boolean existsByUserUsernameAndNameAndServing(String username, String name, double serving);
}
