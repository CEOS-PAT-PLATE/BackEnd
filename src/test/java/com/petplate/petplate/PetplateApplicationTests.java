package com.petplate.petplate;

import com.petplate.petplate.common.EmbeddedType.Nutrient;
import com.petplate.petplate.common.EmbeddedType.StandardNutrient;
import com.petplate.petplate.common.EmbeddedType.Vitamin;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PetplateApplicationTests {

	@Test
	void contextLoads() {

		Nutrient nutrient=Nutrient.builder()
				.carbonHydrate((float) 24)
				.fat((float) 100)
				.vitamin(Vitamin.builder().vitaminD((float) 3.6).vitaminA((float) 3.5).vitaminB( (float) 3.2).vitaminE((float)2.1).build())
				.phosphorus((float) 1.4)
				.calcium((float)9.8)
				.protein((float) 50)
				.build();

		//가장 부족한 영양소 이름
		StandardNutrient deficientNutrient = StandardNutrient.findDeficientNutrient(nutrient);
		System.out.println(deficientNutrient.getName());

		//가장 많은 영양소 이름
		StandardNutrient sufficientNutrient = StandardNutrient.findSufficientNutrient(nutrient);
		System.out.println(sufficientNutrient.getName());



	}

}
