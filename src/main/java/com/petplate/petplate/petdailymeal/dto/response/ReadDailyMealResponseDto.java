package com.petplate.petplate.petdailymeal.dto.response;

import com.petplate.petplate.petdailymeal.domain.entity.*;
import com.petplate.petplate.petfood.domain.entity.Feed;
import com.petplate.petplate.petfood.dto.response.ReadFeedResponseDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ReadDailyMealResponseDto {
    private Long dailyMealId;
    private LocalDate date;

    private List<ReadDailyRawResponseDto> dailyRaws;
    private List<ReadFeedResponseDto> feeds;
    private List<ReadDailyPackagedSnackResponseDto> packagedSnacks;
    private List<ReadDailyBookMarkedRawResponseDto> dailyBookMarkedRaws;
    private List<ReadDailyBookMarkedFeedResponseDto> dailyBookMarkedFeeds;
    private List<ReadDailyBookMarkedPackagedSnackResponseDto> dailyBookMarkedPackagedSnacks;

    public static ReadDailyMealResponseDto of(DailyMeal dailyMeal, List<DailyRaw> dailyRaws, List<Feed> feeds, List<DailyPackagedSnack> packagedSnacks, List<DailyBookMarkedRaw> dailyBookMarkedRaws, List<DailyBookMarkedFeed> dailyBookMarkedFeeds, List<DailyBookMarkedPackagedSnack> dailyBookMarkedPackagedSnacks) {
        ReadDailyMealResponseDto responseDto = new ReadDailyMealResponseDto();
        responseDto.dailyMealId = dailyMeal.getId();
        responseDto.date = dailyMeal.getCreatedAt().toLocalDate();

        // dailyRaw
        dailyRaws.forEach(dailyRaw -> responseDto.dailyRaws.add(ReadDailyRawResponseDto.from(dailyRaw)));

        // dailyFeed
        feeds.forEach(feed -> responseDto.feeds.add(ReadFeedResponseDto.from(feed)));

        // dailyPackagedSnack
        packagedSnacks.forEach(packagedSnack -> responseDto.packagedSnacks.add(ReadDailyPackagedSnackResponseDto.from(packagedSnack)));

        // dailyBookMarkedRaw
        dailyBookMarkedRaws.forEach(dailyBookMarkedRaw -> responseDto.dailyBookMarkedRaws.add(ReadDailyBookMarkedRawResponseDto.from(dailyBookMarkedRaw)));

        // dailyBookMarkedFeed
        dailyBookMarkedFeeds.forEach(dailyBookMarkedFeed -> responseDto.dailyBookMarkedFeeds.add(ReadDailyBookMarkedFeedResponseDto.from(dailyBookMarkedFeed)));

        // dailyBookMarkedPackagedSnack
        dailyBookMarkedPackagedSnacks.forEach(dailyBookMarkedPackagedSnack -> responseDto.dailyBookMarkedPackagedSnacks.add(ReadDailyBookMarkedPackagedSnackResponseDto.from(dailyBookMarkedPackagedSnack)));

        return responseDto;
    }

}
