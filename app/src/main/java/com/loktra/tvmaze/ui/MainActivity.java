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
import android.util.TypedValue;
import android.widget.Toast;

import com.loktra.tvmaze.R;
import com.loktra.tvmaze.adapters.TvShowAdapter;
import com.loktra.tvmaze.communicator.ShowsAdapterListener;
import com.loktra.tvmaze.databinding.ActivityMainBinding;
import com.loktra.tvmaze.models.Show;
import com.loktra.tvmaze.utils.GridSpacingItemDecoration;
import com.loktra.tvmaze.viewmodel.TvshowListViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ShowsAdapterListener {

    private final String TAG = getClass().getName();

    private RecyclerView recyclerView;
    private TvShowAdapter mAdapter;
    private ActivityMainBinding binding;
    private TvshowListViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        initRecyclerView();

        viewModel = ViewModelProviders.of(this).get(TvshowListViewModel.class);
        observeViewModel();
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

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
        mAdapter = new TvShowAdapter(new ArrayList<Show>(), this);
        recyclerView.setAdapter(mAdapter);
    } // End of initRecyclerView()

    private void observeViewModel() {
        // Update the list when the data changes

        if (viewModel != null)
            viewModel.getShowListLiveData().observe(this, new Observer<List<Show>>() {
                @Override
                public void onChanged(@Nullable List<Show> shows) {

                    if (shows != null) {
                        mAdapter.setTvshowList(shows);
                    }
                }
            });
    }// End of observeViewModel()

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
