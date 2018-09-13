package com.loktra.tvmaze.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.loktra.tvmaze.models.Show;

@Database(entities = {Show.class}, version = 1)
public abstract class ShowDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "show_db";
    private static ShowDatabase INSTANCE;

    public static ShowDatabase getDatabase(Context context) {
        if (INSTANCE == null)
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), ShowDatabase.class, DATABASE_NAME)
                    //.fallbackToDestructiveMigration()
                    .build();

        return INSTANCE;
    }

    public static void destroyInstanec() {
        INSTANCE = null;
    }

    public abstract ShowModelDao daoAccess();

}