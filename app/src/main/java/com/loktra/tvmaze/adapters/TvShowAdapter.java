package com.loktra.tvmaze.adapters;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loktra.tvmaze.R;
import com.loktra.tvmaze.communicator.ShowsAdapterListener;
import com.loktra.tvmaze.databinding.ItemInfoBinding;
import com.loktra.tvmaze.databinding.ItemProgressBinding;
import com.loktra.tvmaze.databinding.TvshowItemViewBinding;
import com.loktra.tvmaze.repository.models.FilterInfo;
import com.loktra.tvmaze.repository.models.TvShow;

import java.util.ArrayList;
import java.util.List;

public class TvShowAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // View Types
    public static final int TVSHOW = 0;
    public static final int LOADING = 1;
    public static final int INFO = 2;

    private List<TvShow> showList;
    private LayoutInflater layoutInflater;
    private ShowsAdapterListener listener;
    // flag for footer ProgressBar (i.e. last item of list)
    public boolean isLoadingAdded = false;
    public boolean isInfoAdded = false;
    private FilterInfo filterInfo;

    public TvShowAdapter(ShowsAdapterListener listener) {
        this.listener = listener;

        showList = new ArrayList<TvShow>();
        filterInfo = new FilterInfo();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;

        if (layoutInflater == null)
            layoutInflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case TVSHOW:
                TvshowItemViewBinding showBinding = DataBindingUtil.inflate(layoutInflater, R.layout.tvshow_item_view, parent, false);
                viewHolder = new TvshowVH(showBinding);
                break;

            case LOADING:
                ItemProgressBinding itemProgressBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_progress, parent, false);
                viewHolder = new LoadingVH(itemProgressBinding);
                break;

            case INFO:
                ItemInfoBinding itemInfoBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_info, parent, false);
                viewHolder = new InfoVH(itemInfoBinding);
                break;
        }

        return viewHolder;
    }  // End of onCreateViewHolder()

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        switch (getItemViewType(position)) {
            case TVSHOW:
                TvshowVH tvshowVH = (TvshowVH) holder;
                tvshowVH.tvshowItemViewBinding.setShow(showList.get(position));
                tvshowVH.tvshowItemViewBinding.ivTvShow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null)
                            listener.onTvShowClick(showList.get(position));
                    }
                });
                break;

            case INFO:
                InfoVH infoVH = (InfoVH) holder;
                infoVH.itemInfoBinding.setFilter(filterInfo);
                break;

            case LOADING:
                LoadingVH loadingVH = (LoadingVH) holder;
                break;
        }

    }  // End of onBindViewHolder()

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && isInfoAdded) {
            return INFO;
        } else {
            return (position == showList.size() - 1 && isLoadingAdded) ? LOADING : TVSHOW;
        }
    }

    @Override
    public int getItemCount() {
        return showList == null ? 0 : showList.size();
    }

    public void setTvshowList(List<TvShow> showList) {
        this.showList = showList;
        notifyDataSetChanged();
    }

    public void addTvshowList(List<TvShow> showList) {

        this.showList.addAll(showList);
        notifyDataSetChanged();
    }

    public void add(TvShow show) {
        showList.add(show);
        notifyItemInserted(showList.size() - 1);
    }

    public void addLoadingFooter() {

        if (!isLoadingAdded) {
            isLoadingAdded = true;
            add(new TvShow());
        }
    }

    public void removeLoadingFooter() {
        if (isLoadingAdded) {
            isLoadingAdded = false;

            int position = showList.size() - 1;
            TvShow show = getItem(position);

            if (show != null) {
                showList.remove(position);
                notifyItemRemoved(position);
            }
        }
    }

    public void addInfoHeader(float minRating) {

        String info = "Filter Applied: Rating " + minRating + " and above";
        filterInfo.setInfo(info);

        if (!isInfoAdded) {
            isInfoAdded = true;
            //showList.add(0, new TvShow());
        }
    }

    public void removeInfoHeader() {

        if (isInfoAdded) {
            isInfoAdded = false;

            TvShow show = getItem(0);

            if (show != null) {
                showList.remove(show);
                notifyItemRemoved(0);
            }
        }
    }

    public TvShow getItem(int position) {
        return showList.get(position);
    }

    /**
     * View Holders
     */

    protected class LoadingVH extends RecyclerView.ViewHolder {
        private ItemProgressBinding itemProgressBinding;

        public LoadingVH(ItemProgressBinding itemProgressBinding) {
            super(itemProgressBinding.getRoot());
            this.itemProgressBinding = itemProgressBinding;
        }
    } // End of LoadingVH class

    protected class InfoVH extends RecyclerView.ViewHolder {
        private ItemInfoBinding itemInfoBinding;

        public InfoVH(ItemInfoBinding itemInfoBinding) {
            super(itemInfoBinding.getRoot());
            this.itemInfoBinding = itemInfoBinding;
        }
    } // End of InfoVH class

    protected class TvshowVH extends RecyclerView.ViewHolder {
        private TvshowItemViewBinding tvshowItemViewBinding;

        public TvshowVH(TvshowItemViewBinding tvshowItemViewBinding) {
            super(tvshowItemViewBinding.getRoot());
            this.tvshowItemViewBinding = tvshowItemViewBinding;
        }
    } // End of TvshowVH class
}
