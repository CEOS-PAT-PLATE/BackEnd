package com.petplate.petplate.drug.repository;

import com.petplate.petplate.drug.domain.entity.DrugUsefulPart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DrugUsefulPartRepository extends JpaRepository<DrugUsefulPart,Long> {

    boolean existsByName(String name);

}
