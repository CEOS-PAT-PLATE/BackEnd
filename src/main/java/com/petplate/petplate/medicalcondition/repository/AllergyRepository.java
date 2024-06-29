package com.petplate.petplate.medicalcondition.repository;

import com.petplate.petplate.medicalcondition.domain.entity.Allergy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AllergyRepository extends JpaRepository<Allergy, Long> {
    Optional<Allergy> findByName(String name);
}