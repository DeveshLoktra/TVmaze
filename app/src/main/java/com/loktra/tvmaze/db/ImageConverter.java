package com.loktra.tvmaze.db;

import android.arch.persistence.room.TypeConverter;

import com.loktra.tvmaze.models.Show;

public class ImageConverter {
    @TypeConverter
    public static String toString(Show.Image image) {
        return image.original;
    }

    @TypeConverter
    public static Show.Image toImage(String image) {
        return new Show.Image(image);
    }
}
