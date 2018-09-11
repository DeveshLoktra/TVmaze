package com.loktra.tvmaze.api;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loktra.tvmaze.constants.AppConstants;
import com.loktra.tvmaze.models.Show;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ProjectRepository {

    private static ProjectRepository projectRepository;
    private final String TAG = getClass().getName();
    private OkHttpClient client;
    private Request request;

    public ProjectRepository() {

        client = new OkHttpClient();

        request = new Request.Builder()
                .url(AppConstants.API_URL)
                .build();

    }

    public synchronized static ProjectRepository getInstance() {

        if (projectRepository == null)
            projectRepository = new ProjectRepository();

        return projectRepository;
    }


    public LiveData<ArrayList<Show>> getTvshowList() {
        final MutableLiveData<ArrayList<Show>> data = new MutableLiveData<>();

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
                simulateDelay();

                final String myResponse = response.body().string();

                ArrayList<Show> alShow = new Gson().fromJson(myResponse, new TypeToken<ArrayList<Show>>() {
                }.getType());

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
