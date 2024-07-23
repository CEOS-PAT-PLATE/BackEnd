package com.petplate.petplate.pet.repository;

import com.petplate.petplate.pet.domain.entity.PetAllergy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PetAllergyRepository extends JpaRepository<PetAllergy, Long> {
    List<PetAllergy> findByPetId(Long petId);
}
