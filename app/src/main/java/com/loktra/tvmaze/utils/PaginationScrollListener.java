package com.loktra.tvmaze.utils;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public abstract class PaginationScrollListener extends RecyclerView.OnScrollListener {

    RecyclerView.LayoutManager layoutManager;

    /**
     * @param linearLayoutManager
     */
    public PaginationScrollListener(LinearLayoutManager linearLayoutManager) {
        this.layoutManager = linearLayoutManager;
    }

    /**
     * @param gridLayoutManager
     */
    public PaginationScrollListener(GridLayoutManager gridLayoutManager) {
        this.layoutManager = gridLayoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int firstVisibleItemPosition = 0;   // position of first view
        int visibleItemCount = 0;   // number of items visible in current view
        int totalItemCount = 0;  // Total number of items connected with adapter


        if (layoutManager instanceof LinearLayoutManager) {
            firstVisibleItemPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
            visibleItemCount = layoutManager.getChildCount();
            totalItemCount = layoutManager.getItemCount();

        } else if (layoutManager instanceof GridLayoutManager) {
            firstVisibleItemPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
            visibleItemCount = layoutManager.getChildCount();
            totalItemCount = layoutManager.getItemCount();
        }

        if (!isLoading() && !isLastPage()) {
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0) {
                loadMoreItems();
            }
        }
    }

    protected abstract void loadMoreItems();

    public abstract int getTotalPageCount();

    public abstract boolean isLastPage();

    public abstract boolean isLoading();

}
