package com.loktra.tvmaze.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.loktra.tvmaze.R;
import com.loktra.tvmaze.adapters.TvShowAdapter;
import com.loktra.tvmaze.base.BaseActivity;
import com.loktra.tvmaze.communicator.ShowsAdapterListener;
import com.loktra.tvmaze.databinding.ActivityMainBinding;
import com.loktra.tvmaze.repository.models.TvShow;
import com.loktra.tvmaze.utils.GridSpacingItemDecoration;
import com.loktra.tvmaze.viewmodel.ShowViewModel;

import java.util.List;

public class MainActivity extends BaseActivity implements ShowsAdapterListener {

    private final String TAG = getClass().getName();

    private RecyclerView recyclerView;
    private TvShowAdapter mAdapter;
    private ActivityMainBinding binding;
    private ShowViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initBinding();
        initRecyclerView();
        loadLocalAndMakeApiCall();
    }

    //Instantiated Data Binding and View Model
    private void initBinding() {

        viewModel = ViewModelProviders.of(this).get(ShowViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
    }


    /**
     * Renders RecyclerView with Grid in 2 or 3 columns
     */
    private void initRecyclerView() {
        recyclerView = binding.content.recyclerView;

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

            recyclerView.addItemDecoration(new GridSpacingItemDecoration(3, dpToPx(10), true));
        }

        recyclerView.setHasFixedSize(true); // why?? infinite scroll // tv api fixed that is why it is working
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
        mAdapter = new TvShowAdapter(this);
        recyclerView.setAdapter(mAdapter);
    } // End of initRecyclerView()

    //Accessing Local Data And Making Api Call For Updated Data
    private void loadLocalAndMakeApiCall() {
        // Update the list when the data changes

        showProgressbar();

        if (viewModel != null)
            viewModel.fetch().observe(this, new Observer<List<TvShow>>() {
                @Override
                public void onChanged(@Nullable List<TvShow> tvShows) {
                    if (tvShows != null) {
                        Log.d("Init main with list : ", tvShows.toString());
                        mAdapter.setTvshowList(tvShows);

                        hideProgressbar();
                    } else {
                        Log.e(TAG, "Null reponse");
                    }
                }
            });
    }// End of observeViewModel()
    @Override
    public ProgressBar getProgressBar() {
        return binding.mainProgressbar;
    }

    @Override
    public void onTvShowClick(TvShow show) {
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
