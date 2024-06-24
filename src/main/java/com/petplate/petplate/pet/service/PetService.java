package com.petplate.petplate.pet.service;

import com.petplate.petplate.common.EmbeddedType.Nutrient;
import com.petplate.petplate.common.EmbeddedType.StandardNutrient;
import com.petplate.petplate.common.response.error.ErrorCode;
import com.petplate.petplate.common.response.error.exception.BadRequestException;
import com.petplate.petplate.common.response.error.exception.NotFoundException;
import com.petplate.petplate.pet.domain.Activity;
import com.petplate.petplate.pet.dto.request.AddPetRequestDto;
import com.petplate.petplate.pet.dto.request.ModifyPetInfoRequestDto;
import com.petplate.petplate.pet.dto.request.ModifyPetProfileImgRequestDto;
import com.petplate.petplate.pet.dto.response.*;
import com.petplate.petplate.pet.domain.entity.Pet;
import com.petplate.petplate.pet.repository.PetAllergyRepository;
import com.petplate.petplate.pet.repository.PetDiseaseRepository;
import com.petplate.petplate.pet.repository.PetRepository;
import com.petplate.petplate.petdailymeal.domain.entity.DailyMeal;
import com.petplate.petplate.petdailymeal.repository.DailyMealRepository;
import com.petplate.petplate.user.domain.entity.User;
import com.petplate.petplate.user.repository.UserMemberShipRepository;
import com.petplate.petplate.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PetService {
    private final UserRepository userRepository;
    private final PetRepository petRepository;
    private final UserMemberShipRepository userMemberShipRepository;
    private final PetAllergyRepository petAllergyRepository;
    private final PetDiseaseRepository petDiseaseRepository;
    private final DailyMealRepository dailyMealRepository;

    @Transactional
    public Pet addPet(Long userId, @Valid AddPetRequestDto requestDto) {
        // 해당 pk를 가지는 유저가 존재하지 않는 경우
        User owner = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND));

        // 멤버십이 존재하지 않으면서 두마리 이상의 반려견을 추가하려는 경우 => 예외 발생
        if (!userMemberShipRepository.existsByUserId(userId) && petRepository.existsByOwnerId(userId)) {
            throw new BadRequestException(ErrorCode.BAD_REQUEST);
        }

        Pet pet = Pet.builder()
                .name(requestDto.getName())
                .age(requestDto.getAge())
                .weight(requestDto.getAge())
                .activity(requestDto.getActivity())
                .isNeutering(requestDto.isNeutering())
                .owner(owner)
                .build();

        petRepository.save(pet);

        return pet;
    }

    /**
     * 유저의 모든 펫 반환
     *
     * @param userId
     * @return id, name, age, weight, activity, isNeutering, profileImgPath
     */
    public List<ReadPetResponseDto> getAllPets(Long userId) {
        List<ReadPetResponseDto> responses = new ArrayList<>();

        petRepository.findByOwnerId(userId)
                .forEach(pet -> responses.add(ReadPetResponseDto.from(pet)));
        return responses;
    }

    public ReadPetResponseDto getPet(Long userId, Long petId) {
        Pet pet = findPet(userId, petId);

        ReadPetResponseDto response = ReadPetResponseDto.from(pet);

        return response;
    }

    @Transactional
    public void updatePetInfo(Long userId, Long petId, ModifyPetInfoRequestDto requestDto) {
        Pet pet = findPet(userId, petId);

        pet.updateInfo(requestDto.getName(), requestDto.getAge(), requestDto.getWeight(), requestDto.getActivity(), requestDto.getIsNeutering());
    }

    @Transactional
    public void updateProfileImg(Long userId, Long petId, @Valid ModifyPetProfileImgRequestDto requestDto) {
        Pet pet = findPet(userId, petId);
        pet.updateProfileImg(requestDto.getProfileImg());
    }

    /**
     * 펫이 가진 모든 알러지 반환
     *
     * @param userId
     * @param petId
     * @return 알러지 이름, 알러지 설명
     */
    public List<ReadPetAllergyResponseDto> getAllAllergies(Long userId, Long petId) {
        List<ReadPetAllergyResponseDto> responses = new ArrayList<>();

        Pet pet = findPet(userId, petId);
        petAllergyRepository.findByPetId(petId).forEach(petAllergy -> {
            responses.add(ReadPetAllergyResponseDto.from(petAllergy.getAllergy()));
        });

        return responses;
    }

    /**
     * 펫이 가진 모든 질병 반환
     *
     * @param userId
     * @param petId
     * @return 질병이름, 질병 설명
     */
    public List<ReadPetDiseaseResponseDto> getAllDiseases(Long userId, Long petId) {
        List<ReadPetDiseaseResponseDto> responses = new ArrayList<>();

        Pet pet = findPet(userId, petId);
        petDiseaseRepository.findByPetId(petId).forEach(petDisease -> {
            responses.add(ReadPetDiseaseResponseDto.from(petDisease.getDisease()));
        });

        return responses;
    }

    /**
     * 반려견이 '오늘' 하루 섭취한 영양소 정보를 반환함
     *
     * @param userId
     * @param petId
     * @return
     */
    public List<ReadPetNutrientResponseDto> getPetNutrientToday(Long userId, Long petId) {
        Pet pet = findPet(userId, petId);
        LocalDate today = LocalDate.now();

        // 오늘 먹은 식사 내역이 없는 경우 예외 발생 => 이 부분은 그냥 예외가 발생하는 대신 DailyMeal을 생성해도 될듯?
        DailyMeal dailyMeal = dailyMealRepository.findByPetIdAndCreatedAt(petId, today)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND));

        Nutrient nutrient = dailyMeal.getNutrient();

        // 섭취양
        double carbonHydrate = nutrient.getCarbonHydrate();
        double protein = nutrient.getProtein();
        double fat = nutrient.getFat();
        double calcium = nutrient.getCalcium();
        double phosphorus = nutrient.getPhosphorus();
        double vitaminA = nutrient.getVitamin().getVitaminA();
        double vitaminB = nutrient.getVitamin().getVitaminB();
        double vitaminD = nutrient.getVitamin().getVitaminD();
        double vitaminE = nutrient.getVitamin().getVitaminE();

        List<ReadPetNutrientResponseDto> responses = new ArrayList<>();
        responses.add(ReadPetNutrientResponseDto.from(StandardNutrient.CARBON_HYDRATE.getName(), StandardNutrient.CARBON_HYDRATE.getUnit(), StandardNutrient.CARBON_HYDRATE.getDescription(), carbonHydrate));
        responses.add(ReadPetNutrientResponseDto.from(StandardNutrient.PROTEIN.getName(), StandardNutrient.PROTEIN.getUnit(), StandardNutrient.PROTEIN.getDescription(), protein));
        responses.add(ReadPetNutrientResponseDto.from(StandardNutrient.FAT.getName(), StandardNutrient.FAT.getUnit(), StandardNutrient.FAT.getDescription(), fat));
        responses.add(ReadPetNutrientResponseDto.from(StandardNutrient.CALCIUM.getName(), StandardNutrient.CALCIUM.getUnit(), StandardNutrient.CALCIUM.getDescription(), calcium));
        responses.add(ReadPetNutrientResponseDto.from(StandardNutrient.PHOSPHORUS.getName(), StandardNutrient.PHOSPHORUS.getUnit(), StandardNutrient.PHOSPHORUS.getDescription(), phosphorus));
        responses.add(ReadPetNutrientResponseDto.from(StandardNutrient.VITAMIN_A.getName(), StandardNutrient.VITAMIN_A.getUnit(), StandardNutrient.VITAMIN_A.getDescription(), vitaminA));
        responses.add(ReadPetNutrientResponseDto.from(StandardNutrient.VITAMIN_B.getName(), StandardNutrient.VITAMIN_B.getUnit(), StandardNutrient.VITAMIN_B.getDescription(), vitaminB));
        responses.add(ReadPetNutrientResponseDto.from(StandardNutrient.VITAMIN_D.getName(), StandardNutrient.VITAMIN_D.getUnit(), StandardNutrient.VITAMIN_D.getDescription(), vitaminD));
        responses.add(ReadPetNutrientResponseDto.from(StandardNutrient.VITAMIN_E.getName(), StandardNutrient.VITAMIN_E.getUnit(), StandardNutrient.VITAMIN_E.getDescription(), vitaminE));

        return responses;
    }

    /**
     * 반려견이 '특정 일자'에 하루 섭취한 영양소 정보를 반환함
     *
     * @param userId
     * @param petId
     * @param date
     * @return
     */
    public List<ReadPetNutrientResponseDto> getPetNutrient(Long userId, Long petId, LocalDate date) {
        Pet pet = findPet(userId, petId);

        // 오늘 먹은 식사 내역이 없는 경우 예외 발생 => 이 부분은 그냥 예외가 발생하는 대신 DailyMeal을 생성해도 될듯?
        DailyMeal dailyMeal = dailyMealRepository.findByPetIdAndCreatedAt(petId, date)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND));

        Nutrient nutrient = dailyMeal.getNutrient();

        // 섭취양
        double carbonHydrate = nutrient.getCarbonHydrate();
        double protein = nutrient.getProtein();
        double fat = nutrient.getFat();
        double calcium = nutrient.getCalcium();
        double phosphorus = nutrient.getPhosphorus();
        double vitaminA = nutrient.getVitamin().getVitaminA();
        double vitaminB = nutrient.getVitamin().getVitaminB();
        double vitaminD = nutrient.getVitamin().getVitaminD();
        double vitaminE = nutrient.getVitamin().getVitaminE();

        List<ReadPetNutrientResponseDto> responses = new ArrayList<>();
        responses.add(ReadPetNutrientResponseDto.from(StandardNutrient.CARBON_HYDRATE.getName(), StandardNutrient.CARBON_HYDRATE.getUnit(), StandardNutrient.CARBON_HYDRATE.getDescription(), carbonHydrate));
        responses.add(ReadPetNutrientResponseDto.from(StandardNutrient.PROTEIN.getName(), StandardNutrient.PROTEIN.getUnit(), StandardNutrient.PROTEIN.getDescription(), protein));
        responses.add(ReadPetNutrientResponseDto.from(StandardNutrient.FAT.getName(), StandardNutrient.FAT.getUnit(), StandardNutrient.FAT.getDescription(), fat));
        responses.add(ReadPetNutrientResponseDto.from(StandardNutrient.CALCIUM.getName(), StandardNutrient.CALCIUM.getUnit(), StandardNutrient.CALCIUM.getDescription(), calcium));
        responses.add(ReadPetNutrientResponseDto.from(StandardNutrient.PHOSPHORUS.getName(), StandardNutrient.PHOSPHORUS.getUnit(), StandardNutrient.PHOSPHORUS.getDescription(), phosphorus));
        responses.add(ReadPetNutrientResponseDto.from(StandardNutrient.VITAMIN_A.getName(), StandardNutrient.VITAMIN_A.getUnit(), StandardNutrient.VITAMIN_A.getDescription(), vitaminA));
        responses.add(ReadPetNutrientResponseDto.from(StandardNutrient.VITAMIN_B.getName(), StandardNutrient.VITAMIN_B.getUnit(), StandardNutrient.VITAMIN_B.getDescription(), vitaminB));
        responses.add(ReadPetNutrientResponseDto.from(StandardNutrient.VITAMIN_D.getName(), StandardNutrient.VITAMIN_D.getUnit(), StandardNutrient.VITAMIN_D.getDescription(), vitaminD));
        responses.add(ReadPetNutrientResponseDto.from(StandardNutrient.VITAMIN_E.getName(), StandardNutrient.VITAMIN_E.getUnit(), StandardNutrient.VITAMIN_E.getDescription(), vitaminE));

        return responses;
    }

    /**
     * 반려견이 '오늘' 섭취한 영양소를 적정 섭취량에 대한 비율로 반환함
     * 예) 체중에 대해서 계산한 단백질 적정량이 100g인데 총 200g을 섭취한 경우 protein = 2가 반환
     *
     * @param userId
     * @param petId
     * @return 영양소 비율
     */
    public List<ReadPetNutrientRatioResponseDto> getPetNutrientRatioToday(Long userId, Long petId) {
        Pet pet = findPet(userId, petId);
        LocalDate today = LocalDate.now();

        // 오늘 먹은 식사 내역이 없는 경우 예외 발생 => 이 부분은 그냥 예외가 발생하는 대신 DailyMeal을 생성해도 될듯?
        DailyMeal dailyMeal = dailyMealRepository.findByPetIdAndCreatedAt(petId, today)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND));

        List<ReadPetNutrientRatioResponseDto> responses = new ArrayList<>();

        // 비율 계산
        Map<StandardNutrient, Double> nutrientsMap =
                StandardNutrient.getNutrientsMap(dailyMeal.getNutrient(), pet.getWeight(), pet.getActivity());

        nutrientsMap.forEach((nutrient, ratio) ->{
            responses.add(ReadPetNutrientRatioResponseDto.of(nutrient.getName(), ratio));
        });

        return responses;
    }

    /**
     * 반려견이 '특정 일자'에 섭취한 영양소를 적정 섭취량에 대한 비율로 반환함
     * 예) 체중에 대해서 계산한 단백질 적정량이 100g인데 총 200g을 섭취한 경우 protein = 2가 반환
     *
     * @param userId
     * @param petId
     * @param date
     * @return 영양소 비율
     */
    public List<ReadPetNutrientRatioResponseDto> getPetNutrientRatio(Long userId, Long petId, LocalDate date) {
        Pet pet = findPet(userId, petId);

        // 오늘 먹은 식사 내역이 없는 경우 예외 발생 => 이 부분은 그냥 예외가 발생하는 대신 DailyMeal을 생성해도 될듯?
        DailyMeal dailyMeal = dailyMealRepository.findByPetIdAndCreatedAt(petId, date)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND));

        List<ReadPetNutrientRatioResponseDto> responses = new ArrayList<>();

        // 비율 계산
        Map<StandardNutrient, Double> nutrientsMap =
                StandardNutrient.getNutrientsMap(dailyMeal.getNutrient(), pet.getWeight(), pet.getActivity());

        nutrientsMap.forEach((nutrient, ratio) ->{
            responses.add(ReadPetNutrientRatioResponseDto.of(nutrient.getName(), ratio));
        });

        return responses;
    }

    /**
     * '오늘' 가장 많이 섭취한 영양소 반환(단, 가장 많이 섭취했다 하여서 과잉 영양소는 아닐 수도 있다)
     *
     * @param userId
     * @param petId
     * @return
     */
    public ReadPetNutrientResponseDto getMostSufficientNutrientToday(Long userId, Long petId) {
        Pet pet = findPet(userId, petId);
        LocalDate today = LocalDate.now();

        // 오늘 먹은 식사 내역이 없는 경우 예외 발생 => 이 부분은 그냥 예외가 발생하는 대신 DailyMeal을 생성해도 될듯?
        DailyMeal dailyMeal = dailyMealRepository.findByPetIdAndCreatedAt(petId, today)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND));

        ReadPetNutrientResponseDto response = new ReadPetNutrientResponseDto();

        Nutrient nutrient = dailyMeal.getNutrient();
        StandardNutrient mostSufficientNutrient = StandardNutrient.findMostSufficientNutrient(nutrient, pet.getWeight(), pet.getActivity());


        return ReadPetNutrientResponseDto.from(mostSufficientNutrient.getName(), mostSufficientNutrient.getUnit(), mostSufficientNutrient.getDescription(), nutrient.getNutrientWeightByName(mostSufficientNutrient.getName()));
    }

    /**
     * '특정일'에 가장 많이 섭취한 영양소 반환(단, 가장 많이 섭취했다 하여서 과잉 영양소는 아닐 수도 있다)
     *
     * @param userId
     * @param petId
     * @param date
     * @return
     */
    public ReadPetNutrientResponseDto getMostSufficientNutrient(Long userId, Long petId, LocalDate date) {
        Pet pet = findPet(userId, petId);

        // 오늘 먹은 식사 내역이 없는 경우 예외 발생 => 이 부분은 그냥 예외가 발생하는 대신 DailyMeal을 생성해도 될듯?
        DailyMeal dailyMeal = dailyMealRepository.findByPetIdAndCreatedAt(petId, date)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND));

        ReadPetNutrientResponseDto response = new ReadPetNutrientResponseDto();

        Nutrient nutrient = dailyMeal.getNutrient();
        StandardNutrient mostSufficientNutrient = StandardNutrient.findMostSufficientNutrient(nutrient, pet.getWeight(), pet.getActivity());


        return ReadPetNutrientResponseDto.from(mostSufficientNutrient.getName(), mostSufficientNutrient.getUnit(), mostSufficientNutrient.getDescription(), nutrient.getNutrientWeightByName(mostSufficientNutrient.getName()));
    }

    /**
     * '오늘' 가장 적게 섭취한 영양소 반환(단, 가장 적게 섭취했다 하여서 부족 영양소는 아닐 수도 있다)
     *
     * @param userId
     * @param petId
     * @return
     */
    public ReadPetNutrientResponseDto getMostDeficientNutrientToday(Long userId, Long petId) {
        Pet pet = findPet(userId, petId);
        LocalDate today = LocalDate.now();

        DailyMeal dailyMeal = dailyMealRepository.findByPetIdAndCreatedAt(petId, today)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND));

        ReadPetNutrientResponseDto response = new ReadPetNutrientResponseDto();

        Nutrient nutrient = dailyMeal.getNutrient();
        StandardNutrient mostDefficientNutrient = StandardNutrient.findMostDeficientNutrient(nutrient, pet.getWeight(), pet.getActivity());

        return ReadPetNutrientResponseDto.from(mostDefficientNutrient.getName(), mostDefficientNutrient.getUnit(), mostDefficientNutrient.getDescription(), nutrient.getNutrientWeightByName(mostDefficientNutrient.getName()));
    }

    /**
     * '특정일'에 가장 적게 섭취한 영양소 반환(단, 가장 적게 섭취했다 하여서 부족 영양소는 아닐 수도 있다)
     *
     * @param userId
     * @param petId
     * @param date
     * @return
     */
    public ReadPetNutrientResponseDto getMostDeficientNutrient(Long userId, Long petId, LocalDate date) {
        Pet pet = findPet(userId, petId);

        DailyMeal dailyMeal = dailyMealRepository.findByPetIdAndCreatedAt(petId, date)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND));

        ReadPetNutrientResponseDto response = new ReadPetNutrientResponseDto();

        Nutrient nutrient = dailyMeal.getNutrient();
        StandardNutrient mostDefficientNutrient = StandardNutrient.findMostDeficientNutrient(nutrient, pet.getWeight(), pet.getActivity());

        return ReadPetNutrientResponseDto.from(mostDefficientNutrient.getName(), mostDefficientNutrient.getUnit(), mostDefficientNutrient.getDescription(), nutrient.getNutrientWeightByName(mostDefficientNutrient.getName()));
    }

    /**
     * 반려견이 오늘 먹은 총 칼로리 반환
     *
     * @param userId
     * @param petId
     * @return kcal
     */
    public ReadPetKcalResponseDto getPetKcalToday(Long userId, Long petId) {
        Pet pet = findPet(userId, petId);
        LocalDate today = LocalDate.now();

        DailyMeal dailyMeal = dailyMealRepository.findByPetIdAndCreatedAt(petId, today)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND));

        return ReadPetKcalResponseDto.of(dailyMeal.getKcal());
    }

    /**
     * 반려견이 '특정 일자'에 먹은 총 칼로리 반환
     *
     * @param userId
     * @param petId
     * @param date
     * @return kcal
     */
    public ReadPetKcalResponseDto getPetKcal(Long userId, Long petId, LocalDate date) {
        Pet pet = findPet(userId, petId);

        DailyMeal dailyMeal = dailyMealRepository.findByPetIdAndCreatedAt(petId, date)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND));

        return ReadPetKcalResponseDto.of(dailyMeal.getKcal());
    }

    /**
     * 반려견이 먹어야할 적정 칼로리 반환
     *
     * @param userId
     * @param petId
     * @return
     */
    public ReadPetKcalResponseDto getPetProperKcal(Long userId, Long petId) {
        Pet pet = findPet(userId, petId);

        double properKcal = pet.getActivity().getProperKcal(pet.getWeight());
        return ReadPetKcalResponseDto.of(properKcal);
    }

    /**
     * 반려견의 적정 섭취 칼로리 대비 '오늘' 섭취한 칼로리에 대한 비율을 반환함
     *
     * @param userId
     * @param petId
     * @return
     */
    public ReadPetKcalRatioResponseDto getPetKcalRatioToday(Long userId, Long petId) {
        Pet pet = findPet(userId, petId);
        LocalDate today = LocalDate.now();

        DailyMeal dailyMeal = dailyMealRepository.findByPetIdAndCreatedAt(petId, today)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND));

        double properKcalRatio = pet.getActivity().getProperKcalRatio(pet.getWeight(), dailyMeal.getKcal());

        return ReadPetKcalRatioResponseDto.of(properKcalRatio);
    }

    /**
     * 반려견의 적정 섭취 칼로리 대비 '특정 일자'에 섭취한 칼로리에 대한 비율을 반환함
     *
     * @param userId
     * @param petId
     * @param date
     * @return
     */
    public ReadPetKcalRatioResponseDto getPetKcalRatio(Long userId, Long petId, LocalDate date) {
        Pet pet = findPet(userId, petId);
        DailyMeal dailyMeal = dailyMealRepository.findByPetIdAndCreatedAt(petId, date)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND));

        double properKcalRatio = pet.getActivity().getProperKcalRatio(pet.getWeight(), dailyMeal.getKcal());

        return ReadPetKcalRatioResponseDto.of(properKcalRatio);
    }

    private Pet findPet(Long userId, Long petId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND));

        // 조회하려는 반려견이 본인의 반려견이 아닌 경우 예외 발생
        if (!pet.getOwner().getId().equals(userId)) {
            throw new BadRequestException(ErrorCode.BAD_REQUEST);
        }

        return pet;
    }
}
