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
import com.loktra.tvmaze.models.Show;

import java.util.ArrayList;

public class TvShowAdapter extends RecyclerView.Adapter<TvShowAdapter.MyViewHolder> {

    private ArrayList<Show> showList;
    private LayoutInflater layoutInflater;
    private ShowsAdapterListener listener;

    public TvShowAdapter(ShowsAdapterListener listener) {
        this.listener = listener;
    }

    public TvShowAdapter(ArrayList<Show> showList, ShowsAdapterListener listener) {
        this.showList = showList;
        this.listener = listener;
    }

    public void setTvshowList(ArrayList<Show> showList) {
        this.showList = showList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (layoutInflater == null)
            layoutInflater = LayoutInflater.from(parent.getContext());

        TvshowItemViewBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.tvshow_item_view, parent, false);

        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.binding.setShow(showList.get(position));

        holder.binding.ivTvShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (listener != null)
                    listener.onTvShowClick(showList.get(position));

            }
        });
    }

    @Override
    public int getItemCount() {
        return showList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TvshowItemViewBinding binding;

        public MyViewHolder(final TvshowItemViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


}
