package com.loktra.tvmaze.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.loktra.tvmaze.R;
import com.loktra.tvmaze.adapters.TvShowAdapter;
import com.loktra.tvmaze.base.BaseActivity;
import com.loktra.tvmaze.communicator.FilterEventListener;
import com.loktra.tvmaze.communicator.ShowsAdapterListener;
import com.loktra.tvmaze.databinding.ActivityMainBinding;
import com.loktra.tvmaze.repository.api.Resource;
import com.loktra.tvmaze.repository.models.TvShow;
import com.loktra.tvmaze.utils.GridSpacingItemDecoration;
import com.loktra.tvmaze.utils.PaginationScrollListener;
import com.loktra.tvmaze.viewmodel.ShowViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements ShowsAdapterListener, SwipeRefreshLayout.OnRefreshListener {

    private final String TAG = getClass().getName();

    private RecyclerView recyclerView;
    private TvShowAdapter mAdapter;
    private ActivityMainBinding binding;
    private ShowViewModel viewModel;
    private GridLayoutManager layoutManager;

    private static final int PAGE_START = 0;
    private static final int TOTAL_PAGES = 154;
    private static int CURRENT_PAGE = PAGE_START;

    private boolean isLoading = false;
    private boolean isLastPage = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initBinding();
        initRecyclerView();
        loadLocalAndMakeApiCall(PAGE_START);
    }

    //Instantiated Data Binding and View Model
    private void initBinding() {

        viewModel = ViewModelProviders.of(this).get(ShowViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        binding.content.swipeRefreshLayout.setOnRefreshListener(this);

        binding.setFilterEvent(new FilterEventListener() {
            @Override
            public void onClickFilter() {
                loadFilteredShow();
            }
        });
    }


    /**
     * Renders RecyclerView with Grid in 2 or 3 columns
     */
    private void initRecyclerView() {
        recyclerView = binding.content.recyclerView;

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            layoutManager = new GridLayoutManager(this, 2);

            recyclerView.setLayoutManager(layoutManager);

            recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        } else {

            layoutManager = new GridLayoutManager(this, 3);

            recyclerView.setLayoutManager(layoutManager);

            recyclerView.addItemDecoration(new GridSpacingItemDecoration(3, dpToPx(10), true));
        }

        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (mAdapter.getItemViewType(position)) {

                    case TvShowAdapter.TVSHOW:
                        return 1;

                    case TvShowAdapter.LOADING:
                        return 2;

                    case TvShowAdapter.INFO:
                        return 2;

                    default:
                        return 1;
                }
            }
        });

        //recyclerView.setHasFixedSize(true); // why?? infinite scroll // tv api fixed that is why it is working
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
        mAdapter = new TvShowAdapter(this);
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnScrollListener(new PaginationScrollListener(binding, layoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                CURRENT_PAGE += 1;

                //loadLocalAndMakeApiCall(CURRENT_PAGE);
                loadNextPage(CURRENT_PAGE);
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
    } // End of initRecyclerView()

    /**
     * Accessing Local Data And Making Api Call For Updated Data
     */
    private void loadLocalAndMakeApiCall(final int page) {
        // Update the list when the data changes

        showProgressbar();

        if (viewModel != null)
            viewModel.fetch(page).observe(this, new Observer<List<TvShow>>() {
                @Override
                public void onChanged(@Nullable List<TvShow> tvShows) {
                    if (tvShows != null) {
                        Log.d("Init main with list : ", tvShows.toString());

                        /*if (page == 0)
                            mAdapter.setTvshowList(tvShows);
                        else*/
                        mAdapter.setTvshowList(tvShows);

                        hideProgressbar();

                        if (CURRENT_PAGE <= TOTAL_PAGES) mAdapter.addLoadingFooter();
                        else isLastPage = true;

                    } else {
                        Log.e(TAG, "Null reponse");
                    }
                }
            });

    }// End of observeViewModel()

    private void loadNextPage(final int page) {
        mAdapter.addLoadingFooter();

        if (viewModel != null)

            viewModel.fetchFromApi(page).observe(this, new Observer<Resource<ArrayList<TvShow>>>() {
                @Override
                public void onChanged(@Nullable Resource<ArrayList<TvShow>> arrayListResource) {

                    switch (arrayListResource.status) {
                        case SUCCESS:

                            if (arrayListResource.data != null) {

                                mAdapter.addTvshowList(arrayListResource.data);

                                mAdapter.removeLoadingFooter();
                                isLoading = false;

                                if (CURRENT_PAGE <= TOTAL_PAGES) mAdapter.addLoadingFooter();
                                else isLastPage = true;

                            } else {
                                Log.e(TAG, "Null reponse");
                            }

                            break;
                    }
                }
            });
    }

    @Override
    public ProgressBar getProgressBar() {
        return binding.mainProgressbar;
    }

    /**
     * Tv-Show item click
     *
     * @param show
     */
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        MenuItem searchViewItem = menu.findItem(R.id.search);
        final SearchView searchViewAndroidActionBar = (SearchView) MenuItemCompat.getActionView(searchViewItem);
        searchViewAndroidActionBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchViewAndroidActionBar.clearFocus();

                String tag = "%" + query + "%";
                loadSearchedShowsFromRoom(tag);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {

                String tag = "%" + query + "%";
                loadSearchedShowsFromRoom(tag);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Searching Tv-Shows from Room local Db using 'tag'
     *
     * @param tag
     */
    private void loadSearchedShowsFromRoom(String tag) {
        if (viewModel != null) {
            viewModel.search(tag).observe(this, new Observer<List<TvShow>>() {
                @Override
                public void onChanged(@Nullable List<TvShow> tvShows) {
                    if (tvShows != null) {
                        mAdapter.setTvshowList(tvShows);
                    } else {
                        Log.d(TAG, "Null response while searching in loadSearchedShowsFromRoom()");
                    }
                }
            });
        }
    }// End of loadSearchedShowsFromRoom()


    /**
     * Rating filter from 9 and above
     */
    private void loadFilteredShow() {

        float minRating = 8;
        float maxRating = 10;

        layoutManager.scrollToPositionWithOffset(0, 0);

        mAdapter.addInfoHeader(minRating);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mAdapter.removeInfoHeader();
            }
        }, 3000);

        if (viewModel != null) {
            viewModel.filter(minRating, maxRating).observe(this, new Observer<List<TvShow>>() {
                @Override
                public void onChanged(@Nullable List<TvShow> tvShows) {
                    if (tvShows != null) {
                        mAdapter.setTvshowList(tvShows);
                    } else {
                        Log.d(TAG, "Null response while searching in loadFilteredShow()");
                    }
                }
            });
        }
    }

    /**
     * Swipe to refresh
     */
    @Override
    public void onRefresh() {
        Log.d(TAG, "onRefresh called");

        if (viewModel != null)
            viewModel.fetchFromApi(PAGE_START).observe(this, new Observer<Resource<ArrayList<TvShow>>>() {
                @Override
                public void onChanged(@Nullable Resource<ArrayList<TvShow>> tvShows) {

                    if (tvShows != null) {
                        Log.d("Init main with list : ", tvShows.toString());

                        switch (tvShows.status) {
                            case SUCCESS:
                                mAdapter.setTvshowList(tvShows.data);
                                binding.content.swipeRefreshLayout.setRefreshing(false);
                                break;
                        }


                    } else {
                        Log.e(TAG, "Null reponse");
                    }
                }
            });
    }

} // End of MainActivity