package com.petplate.petplate.petdailymeal.dto.response;

import com.petplate.petplate.petdailymeal.domain.entity.*;
import com.petplate.petplate.petdailymeal.domain.entity.DailyFeed;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class ReadDailyMealFoodResponseDto {
    private Long dailyMealId;
    private LocalDate date;

    private List<ReadDailyRawResponseDto> dailyRaws = new ArrayList<>();
    private List<ReadDailyFeedResponseDto> dailyFeeds = new ArrayList<>();
    private List<ReadDailyPackagedSnackResponseDto> dailyPackagedSnacks = new ArrayList<>();
    private List<ReadDailyBookMarkedRawResponseDto> dailyBookMarkedRaws = new ArrayList<>();
    private List<ReadDailyBookMarkedFeedResponseDto> dailyBookMarkedFeeds = new ArrayList<>();
    private List<ReadDailyBookMarkedPackagedSnackResponseDto> dailyBookMarkedPackagedSnacks = new ArrayList<>();

    public static ReadDailyMealFoodResponseDto of(DailyMeal dailyMeal, List<DailyRaw> dailyRaws, List<DailyFeed> dailyFeeds, List<DailyPackagedSnack> dailyPackagedSnacks, List<DailyBookMarkedRaw> dailyBookMarkedRaws, List<DailyBookMarkedFeed> dailyBookMarkedFeeds, List<DailyBookMarkedPackagedSnack> dailyBookMarkedPackagedSnacks) {
        ReadDailyMealFoodResponseDto responseDto = new ReadDailyMealFoodResponseDto();
        responseDto.dailyMealId = dailyMeal.getId();
        responseDto.date = dailyMeal.getCreatedAt().toLocalDate();

        // dailyRaw
        if (!dailyRaws.isEmpty())
            dailyRaws.forEach(dailyRaw -> responseDto.dailyRaws.add(ReadDailyRawResponseDto.from(dailyRaw)));

        // dailyFeed
        if (!dailyFeeds.isEmpty())
            dailyFeeds.forEach(dailyFeed -> responseDto.dailyFeeds.add(ReadDailyFeedResponseDto.from(dailyFeed)));

        // dailyPackagedSnack
        if (!dailyPackagedSnacks.isEmpty())
            dailyPackagedSnacks.forEach(dailyPackagedSnack -> responseDto.dailyPackagedSnacks.add(ReadDailyPackagedSnackResponseDto.from(dailyPackagedSnack)));

        // dailyBookMarkedRaw
        if (!dailyBookMarkedRaws.isEmpty())
            dailyBookMarkedRaws.forEach(dailyBookMarkedRaw -> responseDto.dailyBookMarkedRaws.add(ReadDailyBookMarkedRawResponseDto.from(dailyBookMarkedRaw)));

        // dailyBookMarkedFeed
        if (!dailyBookMarkedFeeds.isEmpty())
            dailyBookMarkedFeeds.forEach(dailyBookMarkedFeed -> responseDto.dailyBookMarkedFeeds.add(ReadDailyBookMarkedFeedResponseDto.from(dailyBookMarkedFeed)));

        // dailyBookMarkedPackagedSnack
        if (!dailyBookMarkedPackagedSnacks.isEmpty())
            dailyBookMarkedPackagedSnacks.forEach(dailyBookMarkedPackagedSnack -> responseDto.dailyBookMarkedPackagedSnacks.add(ReadDailyBookMarkedPackagedSnackResponseDto.from(dailyBookMarkedPackagedSnack)));

        return responseDto;
    }

}
