package com.petplate.petplate.pet.domain.repository;

import com.petplate.petplate.pet.domain.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PetRepository extends JpaRepository<Pet, Long> {
    Optional<Pet> findByName(String name);
    List<Pet> findByOwnerId(Long ownerId);
    void deleteById(Long id);
    int countByOwnerId(Long ownerId);
    boolean existsByOwnerId(Long ownerId);
}
