package com.loktra.tvmaze.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverters;

import com.loktra.tvmaze.models.Show;

import java.util.ArrayList;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

// An interface class for Database access
// Data Access Object (Dao)
@Dao
@TypeConverters({RatingConverter.class, ImageConverter.class})
//@TypeConverters(ImageConverter.class)
public interface ShowModelDao {

    @Query("Select * from Show")
    LiveData<ArrayList<Show>> getAllShowList();

    @Insert(onConflict = REPLACE)
    void addTvShow(Show show);

    @Insert(onConflict = REPLACE)
    void addTvShows(LiveData<ArrayList<Show>> shows);


}
