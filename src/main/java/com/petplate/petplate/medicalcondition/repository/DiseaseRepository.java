package com.petplate.petplate.medicalcondition.repository;

import com.petplate.petplate.medicalcondition.domain.entity.Allergy;
import com.petplate.petplate.medicalcondition.domain.entity.Disease;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DiseaseRepository extends JpaRepository<Disease, Long> {
    Optional<Disease> findByName(String name);
}