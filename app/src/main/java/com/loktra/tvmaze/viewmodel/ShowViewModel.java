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
    public LiveData<Resource<ArrayList<TvShow>>> fetchFromApi() {
        return tvShowRepository.getServerTVShowResponse();
    }

    // Fetching Data From Room
    public LiveData<List<TvShow>> fetchFromLocalDb() {
        return tvShowRepository.getLocalTVShowResponse();
    }

    // Fetching Data From Both Room DB and API
    public LiveData<List<TvShow>> fetch() {
        return tvShowRepository.getTvShowsMerged();
    }

    // Searching shows by tag
    public LiveData<List<TvShow>> search(String tag) {
        return tvShowRepository.getSearchedShows(tag);
    }

    // Filtering shows by rating
    public LiveData<List<TvShow>> filter(float start, float end) {
        return tvShowRepository.getFilteredShow(start, end);
    }
}
