package com.loktra.tvmaze.repository.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;

import com.loktra.tvmaze.base.BaseApplication;
import com.loktra.tvmaze.repository.models.TvShow;

@Database(entities = {TvShow.class}, version = 1)
public abstract class ShowDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "show_db";
    private static ShowDatabase INSTANCE;

    public static ShowDatabase getDatabase() {
        if (INSTANCE == null)
            INSTANCE = Room.databaseBuilder(BaseApplication.application.getApplicationContext(), ShowDatabase.class, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build();

        return INSTANCE;
    }

    public static void destroyInstanec() {
        INSTANCE = null;
    }

    public abstract ShowModelDao daoAccess();

}