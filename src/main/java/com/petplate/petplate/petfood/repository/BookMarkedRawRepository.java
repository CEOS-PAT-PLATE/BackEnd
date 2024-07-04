package com.petplate.petplate.petfood.repository;

import com.petplate.petplate.petfood.domain.entity.BookMarkedRaw;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookMarkedRawRepository extends JpaRepository<BookMarkedRaw, Long> {
    List<BookMarkedRaw> findByUserId(Long userId);
    List<BookMarkedRaw> findByRawId(Long rawId);
    Optional<BookMarkedRaw> findByUserIdAndRawId(Long userId, Long rawId);
}
