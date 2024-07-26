package com.petplate.petplate.drug.repository;

import com.petplate.petplate.drug.domain.entity.DrugDrugUsefulPart;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DrugDrugUsefulPartRepository extends JpaRepository<DrugDrugUsefulPart,Long> {


    List<DrugDrugUsefulPart> findByDrugId(final Long drugId);

    @Query("select ddup from DrugDrugUsefulPart ddup join fetch ddup.drugUsefulPart where ddup.drug.id=:drugId")
    List<DrugDrugUsefulPart> findByDrugIdWithFetchDrugUsefulPart(@Param("drugId")Long drugId);
    void deleteByDrugId(final Long drugId);
}
