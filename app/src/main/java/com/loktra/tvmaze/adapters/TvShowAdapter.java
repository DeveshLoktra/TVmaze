package com.loktra.tvmaze.adapters;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loktra.tvmaze.R;
import com.loktra.tvmaze.communicator.ShowsAdapterListener;
import com.loktra.tvmaze.databinding.TvshowItemViewBinding;
import com.loktra.tvmaze.repository.models.TvShow;

import java.util.List;

public class TvShowAdapter extends RecyclerView.Adapter<TvShowAdapter.TvShowViewHolder> {

    private List<TvShow> showList;
    private LayoutInflater layoutInflater;
    private ShowsAdapterListener listener;

    public TvShowAdapter(ShowsAdapterListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public TvShowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (layoutInflater == null)
            layoutInflater = LayoutInflater.from(parent.getContext());

        TvshowItemViewBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.tvshow_item_view, parent, false);

        return new TvShowViewHolder(binding);
    }  // End of onCreateViewHolder()

    @Override
    public void onBindViewHolder(@NonNull TvShowViewHolder holder, final int position) {

        holder.binding.setShow(showList.get(position));

        holder.binding.ivTvShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onTvShowClick(showList.get(position));
            }
        });
    }  // End of onBindViewHolder()

    @Override
    public int getItemCount() {
        return showList == null ? 0 : showList.size();
    }

    public void setTvshowList(List<TvShow> showList) {
        this.showList = showList;
        notifyDataSetChanged();
    }// End of setTvshowList()

    public class TvShowViewHolder extends RecyclerView.ViewHolder {

        private TvshowItemViewBinding binding;

        public TvShowViewHolder(final TvshowItemViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    } // End of class TvShowViewHolder

}
