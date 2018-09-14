package com.loktra.tvmaze.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.loktra.tvmaze.constants.AppConstants;
import com.loktra.tvmaze.repository.api.Resource;
import com.loktra.tvmaze.repository.api.WebService;
import com.loktra.tvmaze.repository.database.ShowDatabase;
import com.loktra.tvmaze.repository.database.ShowModelDao;
import com.loktra.tvmaze.repository.models.TvShow;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TVShowRepository {

    private static final String TAG = TVShowRepository.class.getSimpleName();

    private ShowModelDao mTVShowDao;
    private WebService webService;

    private LiveData<List<TvShow>> mAllTVShow;//, mSearchShowLiveData;

    private MediatorLiveData<List<TvShow>> mergedData = new MediatorLiveData<>();

    public TVShowRepository() {

        webService = WebService.getInstance();

        // This should happen after login/app start up depending on use case.
        ShowDatabase db = ShowDatabase.getDatabase();

        // Getting dao access
        mTVShowDao = db.daoAccess();

        // get all tvshow from Room DB
        mAllTVShow = mTVShowDao.getAllShowList();
    }

    // Getting Local Tv Show Data From Shared Preference
   /* public LiveData<ArrayList<TVShowResponse>> getLocalTVShowResponse(){
        Type type = new TypeToken<ArrayList<TVShowResponse>>(){}.getType();
        String json = SharedPrefUtil.getStringFromPreferences(SharedPrefConstants.TVSHOW_RESPONSE);
        ArrayList<TVShowResponse> tvShowResponse;
        MutableLiveData<ArrayList<TVShowResponse>> tvShowResponseLD = new MutableLiveData<>();
        if(json != null){
            tvShowResponse = GsonHelper.getInstance().fromJson(json,type);
            tvShowResponseLD.setValue(tvShowResponse);
        }

        Log.d(TAG,"Fetching Response From TVShow Local Database");

        return tvShowResponseLD;
    }*/

    // Fetching Tv Show Data From Api
    public LiveData<Resource<ArrayList<TvShow>>> getServerTVShowResponse() {
        Log.d(TAG, "Making TVShow APi Call");

        Uri builtUri = Uri.parse(AppConstants.FETCH_TV_SHOW);

        Type type = new TypeToken<ArrayList<TvShow>>() {
        }.getType();

        return WebService.getInstance().genericGetApiCall(builtUri.toString(), type);
    }

    // Getting Local Tv Show Data From Shared Preference
    public LiveData<List<TvShow>> getLocalTVShowResponse() {

        Log.d(TAG, "Fetching Response From TVShow Local Database");
        Log.d("datassssstt", "" + mAllTVShow.getValue() + " livedata : " + mAllTVShow);
        return mAllTVShow;
    }

//    // Saving Data Fetched From Api To Shared Preference
//    @WorkerThread
//    public static void saveTVShowResponse(@NonNull ArrayList<TVShowResponse> tvShowResponseList) {
//
//        Type type = new TypeToken<ArrayList<TVShowResponse>>(){}.getType();
//        SharedPrefUtil.saveStringToPreferences(SharedPrefConstants.TVSHOW_RESPONSE, GsonHelper.getInstance().toJson(tvShowResponseList, type));
//        Log.d(TAG, "Saving Tv Show response to db");
//    }

    /**
     * Saving Data Fetched From Api To Room Database
     *
     * @param tvShowResponseList
     */

    public void saveTVShowResponse(@NonNull final List<TvShow> tvShowResponseList) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "Inserting tv list in Room db");
                mTVShowDao.addTvShows(tvShowResponseList);
            }
        };
        new Thread(runnable).start();

        Log.d(TAG, "" + tvShowResponseList);

        //new InsertAsynkTask(mTVShowDao).execute(tvShowResponseList);

        Log.d(TAG, "Saving Tv Show response to Room db");
    }

    public LiveData<List<TvShow>> getTvShowsMerged() {

        mergedData.removeSource(getLocalTVShowResponse());
        mergedData.removeSource(getServerTVShowResponse());

        // Add source 1
        mergedData.addSource(getLocalTVShowResponse(), new Observer<List<TvShow>>() {
            @Override
            public void onChanged(@Nullable List<TvShow> tvShowResponseList) {
                Log.d(TAG, "got from Room db, setting to merger");
                mergedData.setValue(tvShowResponseList);
                Log.d("data", tvShowResponseList.toString());
            }
        });


        // Add source 2
        mergedData.addSource(getServerTVShowResponse(), new Observer<Resource<ArrayList<TvShow>>>() {
            @Override
            public void onChanged(@Nullable Resource<ArrayList<TvShow>> arrayListResource) {
                Log.d(TAG, arrayListResource.message);

                switch (arrayListResource.status) {
                    case SUCCESS:
                        // Api sends to activity, actually DB should
                        List<TvShow> list = arrayListResource.data;
                        saveTVShowResponse(list);
                        Log.d(TAG, "got from api, setting to merger " + list);
                        mergedData.setValue(list);
                        break;
                }
            }
        });

        return mergedData;
    }

    public LiveData<List<TvShow>> getSearchedShows(String tag) {
        Log.d(TAG, "Searching TvShows in Room DB with tag : " + tag);

        // get all tvshow from Room DB
        //mSearchShowLiveData = mTVShowDao.getSearchedTvShows(tag);
        mAllTVShow = mTVShowDao.getSearchedTvShows(tag);

        //return mSearchShowLiveData;
        return mAllTVShow;
    }

    public LiveData<List<TvShow>> getFilteredShow(float start, float end) {
        Log.d(TAG, "Filtering TvShows in Room DB by rating start: " + start + " & end: " + end);

        //mSearchShowLiveData = mTVShowDao.getFilteredTvShows(start, end);
        //return mSearchShowLiveData;

        mAllTVShow = mTVShowDao.getFilteredTvShows(start, end);
        return mAllTVShow;
    }

}
