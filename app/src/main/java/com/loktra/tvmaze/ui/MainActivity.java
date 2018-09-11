package com.loktra.tvmaze.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loktra.tvmaze.R;
import com.loktra.tvmaze.adapters.TvShowAdapter;
import com.loktra.tvmaze.communicator.ShowsAdapterListener;
import com.loktra.tvmaze.constants.AppConstants;
import com.loktra.tvmaze.databinding.ActivityMainBinding;
import com.loktra.tvmaze.models.Show;
import com.loktra.tvmaze.models.TvShow;
import com.loktra.tvmaze.utils.GridSpacingItemDecoration;
import com.loktra.tvmaze.viewmodel.TvshowListViewModel;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements ShowsAdapterListener {

    private final String TAG = getClass().getName();

    private RecyclerView recyclerView;
    private TvShowAdapter mAdapter;
    private ActivityMainBinding binding;
    private TvShow show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        initRecyclerView();

        //  Asynchronous OkHttp Calls
        //okhttpCall();

        final TvshowListViewModel viewModel = ViewModelProviders.of(this).get(TvshowListViewModel.class);
        observeViewModel(viewModel);
    }

    /**
     * Renders RecyclerView with Grid Images in 3 columns
     */
    private void initRecyclerView() {
        recyclerView = binding.content.recyclerView;

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 4));

            recyclerView.addItemDecoration(new GridSpacingItemDecoration(4, dpToPx(10), true));
        }

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
        mAdapter = new TvShowAdapter(getshowes(), this);
        recyclerView.setAdapter(mAdapter);
    }

    private void observeViewModel(TvshowListViewModel viewModel) {
        // Update the list when the data changes

        viewModel.getShowListLiveData().observe(this, new Observer<ArrayList<Show>>() {
            @Override
            public void onChanged(@Nullable ArrayList<Show> shows) {

                if (shows != null) {
                    mAdapter.setTvshowList(shows);
                }
            }
        });
    }

    private ArrayList<Show> getshowes() {
        ArrayList<Show> showes = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            Show show = new Show("Name", new Show.Rating(4.5f), new Show.Image("https://api.androidhive.info/images/nature/" + i + ".jpg"));
            showes.add(show);
        }
        return showes;
    }

    //  Asynchronous OkHttp Calls
    void okhttpCall() {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(AppConstants.API_URL)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String myResponse = response.body().string();

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e(TAG, myResponse);


                        ArrayList<Show> alShow = new ArrayList<>();
                        alShow = new Gson().fromJson(myResponse, new TypeToken<ArrayList<Show>>() {
                        }.getType());
                        mAdapter = new TvShowAdapter(alShow, MainActivity.this);
                        recyclerView.setAdapter(mAdapter);


                        /*try {
                            JSONArray data = new JSONArray(myResponse);

                            ArrayList<TvShow> shows = new ArrayList<>();

                            for (int i = 0; i < data.length(); i++){

                                TvShow show = new TvShow();

                                JSONObject jsonObject = data.getJSONObject(i);

                                show.setName(jsonObject.getString("name"));
                                show.setImageUrl(jsonObject.getJSONObject("image").getString("original"));
                                show.setRating(jsonObject.getJSONObject("rating").getString("average"));

                                shows.add(show);
                            }


                            mAdapter = new TvShowAdapter(shows, MainActivity.this);
                            recyclerView.setAdapter(mAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }*/


                    }

                });

            }
        });
    } // End of run()


    @Override
    public void onTvShowClick(Show show) {
        Toast.makeText(getApplicationContext(), "Show clicked! " + show.getName(), Toast.LENGTH_SHORT).show();
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

}
