package com.loktra.tvmaze.utils;

import android.support.annotation.NonNull;

import com.google.gson.Gson;

/**
 * Gson helper class which holds app specific instance.
 */
public class GsonHelper {

    // Saving one instance in memory so that GC triggers are saved.
    private static Gson gson;

    @NonNull
    public static Gson getInstance() {
        if (gson == null) {
            gson = new Gson();
        }
        return gson;
    }
}
