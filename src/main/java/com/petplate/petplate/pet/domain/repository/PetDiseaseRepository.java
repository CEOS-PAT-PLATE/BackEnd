package com.petplate.petplate.pet.domain.repository;

import com.petplate.petplate.pet.domain.entity.PetDisease;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PetDiseaseRepository extends JpaRepository<PetDisease, Long> {
    List<PetDisease> findByPetId(Long petId);
}
