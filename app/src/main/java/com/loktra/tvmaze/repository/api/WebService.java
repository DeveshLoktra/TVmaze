package com.loktra.tvmaze.repository.api;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.JsonSyntaxException;
import com.loktra.tvmaze.base.ThreadSafeLiveData;
import com.loktra.tvmaze.repository.models.TvShow;
import com.loktra.tvmaze.utils.GsonHelper;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WebService {
    private static final String TAG = WebService.class.getSimpleName();

    //private static final MediaType JSON = MediaType.parse("application/json");
    private static WebService webService;
    private okhttp3.OkHttpClient client;

    private WebService() {
        client = new OkHttpClient();
    }

    // Initiating Webservice
    public static synchronized void init() {
        if (webService == null) {
            webService = new WebService();
        }
    }

    public static synchronized WebService getInstance() {
        if (webService == null) {
            webService = new WebService();
        }
        return webService;
    }

    @NonNull
    public OkHttpClient getClient() {
        return client;
    }


    // Making Api Calls
    public <U> LiveData<Resource<U>> genericGetApiCall(@NonNull String apiUrl,
                                                       final @NonNull Type type) {
        return genericGetApiCall(apiUrl, type, false);
    }

    public <U> LiveData<Resource<U>> genericGetApiCall(@NonNull String apiUrl,
                                                       final @NonNull Type type,
                                                       final boolean avoidCrash) {

        final ThreadSafeLiveData<Resource<U>> resource = new ThreadSafeLiveData<>();

        //OkHttp Request Build
        final Request request = new Request.Builder().url(apiUrl).get().build();

        // Show loading url
        final Resource<U> loading = Resource.loading(null);

        // setting ThreadSafeLiveData to loading
        resource.setValue(loading);

        //OkHttp Call
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d(TAG, "onFailure for request: " + call.request() + ", request could not be completed due to connectivity issue");
                Resource<U> res = Resource.error("Error occurred", null);
                resource.setValue(res);
            } // End of onFailure()

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {

                    String responseJsonString = response.body().string();

                    Log.d(TAG, "Response arrived for url : " + call.request() + " response : " + response.code());

                    ArrayList<TvShow> serverResponseObject;
                    Resource<ArrayList<TvShow>> res;

                    if (avoidCrash) {
                        try {
                            serverResponseObject = GsonHelper.getInstance().fromJson(responseJsonString, type);
                            res = Resource.success(serverResponseObject);
                        } catch (JsonSyntaxException ex) {
                            res = Resource.error(404, "Invalid server response", null);
                        }
                    } else {
                        serverResponseObject = GsonHelper.getInstance()
                                .fromJson(responseJsonString, type);
                        res = Resource.success(serverResponseObject);

                    }
                    resource.setValue((Resource<U>) res);
                } else {
                    Log.d(TAG, "Response arrived for url : " + call.request()
                            + " response code : " + response.code() + " ");
                    Resource<U> res = Resource.error("Error occurred", null);
                    resource.setValue(res);
                }

            } // End of onResponse()
        });

        return resource;
    }
}
