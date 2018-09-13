package com.loktra.tvmaze.repository.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.databinding.BindingAdapter;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.loktra.tvmaze.R;
import com.loktra.tvmaze.repository.database.converter.ImageConverter;
import com.loktra.tvmaze.repository.database.converter.RatingConverter;

@Entity
public class TvShow {

    @NonNull
    @PrimaryKey(autoGenerate = false)
    @SerializedName("id")
    @Expose
    public String id;

    @SerializedName("name")
    @Expose
    public String name;

    //@Ignore  // ignore this field during Room persistance
    @TypeConverters(RatingConverter.class)
    @SerializedName("rating")
    @Expose
    public Rating rating;

    //@Ignore // ignore the field during Room persistance
    @TypeConverters(ImageConverter.class)
    @SerializedName("image")
    @Expose
    public Image image;


    public TvShow(String name, Rating rating, Image image) {
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Entity(foreignKeys = {
            @ForeignKey(
                    entity = TvShow.class,
                    parentColumns = "id",
                    childColumns = "rating_fk"
            )})
    public static class Rating {
        @SerializedName("average")
        @Expose
        public float average;

        @ColumnInfo(name = "rating_fk")
        private int ratingIdFk;

        public Rating(float average) {
            this.average = average;
        }
    }

    @Entity(foreignKeys = {
            @ForeignKey(
                    entity = TvShow.class,
                    parentColumns = "id",
                    childColumns = "image_fk"
            )})
    public static class Image {

        /*@SerializedName("medium")
        @Expose
        public String medium;*/

        @SerializedName("original")
        @Expose
        public String original;
        @ColumnInfo(name = "image_fk")
        private int imageIdFk;

        public Image(String original) {
            this.original = original;
        }
    }
}

