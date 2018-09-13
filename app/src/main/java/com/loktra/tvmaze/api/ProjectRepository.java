package com.loktra.tvmaze.api;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loktra.tvmaze.constants.AppConstants;
import com.loktra.tvmaze.db.ShowDatabase;
import com.loktra.tvmaze.models.Show;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ProjectRepository {

    private final String TAG = getClass().getName();
    private static ProjectRepository projectRepository;
    private OkHttpClient client;
    private Request request;
    private ShowDatabase mDatabase;

    public ProjectRepository(ShowDatabase mDatabase) {

        client = new OkHttpClient();

        request = new Request.Builder()
                .url(AppConstants.BASE_URL.concat(AppConstants.SHOW_URL))
                .build();

        this.mDatabase = mDatabase;
    }

    public synchronized static ProjectRepository getInstance(ShowDatabase mDatabase) {

        if (projectRepository == null)
            projectRepository = new ProjectRepository(mDatabase);

        return projectRepository;
    }


    public LiveData<List<Show>> getTvshowList() {
        final MutableLiveData<List<Show>> data = new MutableLiveData<>();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                data.setValue(null);

                e.printStackTrace();
                call.cancel();
            }


            @Override
            public void onResponse(Call call, Response response) throws IOException {

                // Set 10 millisec delay
                //simulateDelay();

                final String myResponse = response.body().string();

                List<Show> alShow = new Gson().fromJson(myResponse, new TypeToken<List<Show>>() {
                }.getType());

                mDatabase.daoAccess().addTvShows(alShow);

                data.postValue(alShow);

            }
        });
        return data;
    }

    // Set 10 millisec delay
    private void simulateDelay() {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
