package com.petplate.petplate.petfood.repository;

import com.petplate.petplate.petfood.domain.entity.Raw;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RawRepository extends JpaRepository<Raw, Long> {

    @Query("select r from Raw r where r.name like %:keyword%")
    List<Raw> findByKeyword(@Param("keyword") String keyword);

    Optional<Raw> findByName(String name);

    boolean existsByName(String name);
}
