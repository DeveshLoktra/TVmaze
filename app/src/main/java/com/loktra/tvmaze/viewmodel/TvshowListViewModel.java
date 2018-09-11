package com.loktra.tvmaze.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.loktra.tvmaze.api.ProjectRepository;
import com.loktra.tvmaze.models.Show;

import java.util.ArrayList;

public class TvshowListViewModel extends AndroidViewModel {

    private final LiveData<ArrayList<Show>> showListLiveData;


    public TvshowListViewModel(@NonNull Application application) {
        super(application);

        showListLiveData = ProjectRepository.getInstance().getTvshowList();
    }


    public LiveData<ArrayList<Show>> getShowListLiveData() {
        return showListLiveData;
    }
}
