package com.loktra.tvmaze.repository.database.converter;

import android.arch.persistence.room.TypeConverter;

import com.loktra.tvmaze.repository.models.TvShow;

public class RatingConverter {

    @TypeConverter
    public static float tofloat(TvShow.Rating rating) {
        return rating.average;
    }

    @TypeConverter
    public static TvShow.Rating toRating(float rating) {
        return new TvShow.Rating(rating);
    }

}
