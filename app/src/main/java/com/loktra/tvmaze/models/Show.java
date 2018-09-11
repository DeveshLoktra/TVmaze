package com.loktra.tvmaze.models;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.loktra.tvmaze.R;

public class Show {

    @SerializedName("name")
    @Expose
    public String name;

    @SerializedName("rating")
    @Expose
    public Rating rating;

    @SerializedName("image")
    @Expose
    public Image image;

    public Show(String name, Rating rating, Image image) {
        this.name = name;
        this.rating = rating;
        this.image = image;
    }

    @BindingAdapter("imageUrl")
    public static void loadImage(ImageView view, String imageUrl) {
        Context context = view.getContext();

        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background);

        Glide.with(context)
                .setDefaultRequestOptions(options)
                .load(imageUrl)
                .into(view);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static class Rating {
        @SerializedName("average")
        @Expose
        public float average;

        public Rating(float average) {
            this.average = average;
        }
    }

    public static class Image {

        /*@SerializedName("medium")
        @Expose
        public String medium;*/

        @SerializedName("original")
        @Expose
        public String original;

        public Image(String original) {
            this.original = original;
        }
    }
}
