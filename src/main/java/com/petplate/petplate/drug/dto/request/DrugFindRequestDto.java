package com.petplate.petplate.drug.dto.request;

import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DrugFindRequestDto {

    @Size(min=1,message = "최소 1개 이상 입력")
    List<String> nutrients = new ArrayList<>();

}
