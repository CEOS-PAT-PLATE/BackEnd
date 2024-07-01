package com.petplate.petplate.drug.repository;

import com.petplate.petplate.common.EmbeddedType.StandardNutrient;
import com.petplate.petplate.drug.domain.entity.Drug;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DrugRepository extends JpaRepository<Drug,Long> {

    @Query("select d from Drug d join d.drugNutrientList dn where dn.standardNutrient in :standardNutrients group by d order by count(dn) desc ")
    List<Drug> findUserProperDrugList(@Param("standardNutrients") List<StandardNutrient> standardNutrients);

}
