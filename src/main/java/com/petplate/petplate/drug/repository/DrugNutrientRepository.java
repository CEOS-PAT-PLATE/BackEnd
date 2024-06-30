package com.petplate.petplate.drug.repository;

import com.petplate.petplate.common.EmbeddedType.StandardNutrient;
import com.petplate.petplate.drug.domain.entity.Drug;
import com.petplate.petplate.drug.domain.entity.DrugNutrient;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DrugNutrientRepository extends JpaRepository<DrugNutrient,Long> {


    List<DrugNutrient> findByDrug(Drug drug);

    @Query("select dn from DrugNutrient dn join fetch dn.drug  where dn.drug.id=:drugId")
    List<DrugNutrient> findByDrugIdWithFetchDrug(@Param("drugId") Long drugId);

    @Query("select dn from DrugNutrient dn join fetch dn.drug where dn.standardNutrient=:standardNutrient")
    List<DrugNutrient> findByStandardNutrientWithFetchDrug(@Param("standardNutrient") StandardNutrient standardNutrient);


    void deleteByDrugId(Long drugId);


}
