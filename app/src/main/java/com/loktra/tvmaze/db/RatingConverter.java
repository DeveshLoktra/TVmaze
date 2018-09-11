package com.loktra.tvmaze.db;

import android.arch.persistence.room.TypeConverter;

import com.loktra.tvmaze.models.Show;

public class RatingConverter {

    @TypeConverter
    public static float tofloat(Show.Rating rating) {
        return rating.average;
    }

    @TypeConverter
    public static Show.Rating toRating(float rating) {
        return new Show.Rating(rating);
    }

}
