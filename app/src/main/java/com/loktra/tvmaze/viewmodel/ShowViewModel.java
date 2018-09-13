package com.loktra.tvmaze.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.loktra.tvmaze.repository.TVShowRepository;
import com.loktra.tvmaze.repository.api.Resource;
import com.loktra.tvmaze.repository.models.TvShow;

import java.util.ArrayList;
import java.util.List;

public class ShowViewModel extends ViewModel {

    private static final String TAG = ShowViewModel.class.getSimpleName();

    private List<TvShow> responseList;

    private TVShowRepository tvShowRepository;

    public ShowViewModel() {
        // Called by Android itself.
        tvShowRepository = new TVShowRepository();
    }

    // Fetching Data From Api
    public LiveData<Resource<ArrayList<TvShow>>> fetchTVShowDataFromApi() {
        return tvShowRepository.getServerTVShowResponse();
    }

    // Fetching Data From Room
    public LiveData<List<TvShow>> fetchTVShowDataFromLocal() {
        return tvShowRepository.getLocalTVShowResponse();
    }

    // Both DB and API
    public LiveData<List<TvShow>> fetch() {
        return tvShowRepository.getTvShowsMerged();
    }

    // Saving Response From Api To Shared Preference and Setting Response To ResponseList
    public void setResponse(List<TvShow> responseList, boolean saveResponse) {

        if (saveResponse) {
            tvShowRepository.saveTVShowResponse(responseList);
        }

        this.responseList = responseList;
    }

    public List<TvShow> getResponseList() {
        return responseList;
    }

    public void setResponseList(ArrayList<TvShow> responseList) {
        setResponse(responseList, false);
    }
}
