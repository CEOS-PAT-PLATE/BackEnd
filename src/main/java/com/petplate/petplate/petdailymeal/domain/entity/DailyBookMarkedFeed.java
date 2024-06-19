package com.petplate.petplate.petdailymeal.domain.entity;

import com.petplate.petplate.petfood.domain.entity.BookMarkedFeed;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class DailyBookMarkedFeed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "daily_book_marked_feed_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_marked_feed_id",nullable = false)
    private BookMarkedFeed bookMarkedFeed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "daily_meal_id",nullable = false)
    private DailyMeal dailyMeal;

    @Builder
    public DailyBookMarkedFeed(BookMarkedFeed bookMarkedFeed, DailyMeal dailyMeal) {
        this.bookMarkedFeed = bookMarkedFeed;
        this.dailyMeal = dailyMeal;
    }
}
