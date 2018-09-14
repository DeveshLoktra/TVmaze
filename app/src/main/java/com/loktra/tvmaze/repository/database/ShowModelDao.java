package com.loktra.tvmaze.repository.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverters;

import com.loktra.tvmaze.repository.database.converter.ImageConverter;
import com.loktra.tvmaze.repository.database.converter.RatingConverter;
import com.loktra.tvmaze.repository.models.TvShow;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

// An interface class for Database access
// Data Access Object (Dao)
@Dao
@TypeConverters({RatingConverter.class, ImageConverter.class})
public interface ShowModelDao {

    @Query("Select * from TvShow")
    LiveData<List<TvShow>> getAllShowList();

    @Insert(onConflict = REPLACE)
    void addTvShow(TvShow show);

    @Insert(onConflict = REPLACE)
    void addTvShows(List<TvShow> shows);

    @Query("SELECT * FROM TvShow WHERE name LIKE :tag")
    LiveData<List<TvShow>> getSearchedTvShows(String tag);

    @Query("SELECT * FROM TvShow WHERE rating BETWEEN :start AND :end")
    LiveData<List<TvShow>> getFilteredTvShows(float start, float end);
}
