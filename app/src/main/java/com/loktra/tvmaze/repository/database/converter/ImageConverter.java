package com.loktra.tvmaze.repository.database.converter;

import android.arch.persistence.room.TypeConverter;

import com.loktra.tvmaze.repository.models.TvShow;

public class ImageConverter {
    @TypeConverter
    public static String toString(TvShow.Image image) {
        return image.original;
    }

    @TypeConverter
    public static TvShow.Image toImage(String image) {
        return new TvShow.Image(image);
    }
}
