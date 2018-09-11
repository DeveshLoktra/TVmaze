package com.loktra.tvmaze.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.loktra.tvmaze.api.ProjectRepository;
import com.loktra.tvmaze.db.ShowDatabase;
import com.loktra.tvmaze.models.Show;

import java.util.ArrayList;

public class TvshowListViewModel extends AndroidViewModel {

    private LiveData<ArrayList<Show>> showListLiveData;
    private ShowDatabase mDatabase;

    public TvshowListViewModel(@NonNull Application application) {
        super(application);

        mDatabase = ShowDatabase.getDatabase(this.getApplication());

        // get data from Room
        //showListLiveData = mDatabase.itemAndShowModel().getAllShowList();

        // get data from TVmaze API
        showListLiveData = ProjectRepository.getInstance().getTvshowList();

        //new UpdateDbAsynkTask(mDatabase).execute(showListLiveData);
    }

    public LiveData<ArrayList<Show>> getShowListLiveData() {
        return showListLiveData;
    }


    private static class UpdateDbAsynkTask extends AsyncTask<LiveData<ArrayList<Show>>, Void, Void> {

        private ShowDatabase database;

        public UpdateDbAsynkTask(ShowDatabase database) {
            this.database = database;
        }

        @Override
        protected Void doInBackground(LiveData<ArrayList<Show>>... liveData) {
            database.itemAndShowModel().addTvShows(liveData[0]);
            return null;
        }
    }
}
