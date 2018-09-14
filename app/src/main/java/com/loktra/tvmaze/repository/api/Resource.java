package com.loktra.tvmaze.repository.api;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class Resource<T> {

    @NonNull
    public final Status status;

    @Nullable
    public final T data;

    @NonNull
    public final String message;

    private int serverCode;

    private Resource(@NonNull Status status, @Nullable T data, @NonNull String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    private Resource(@NonNull Status status, @Nullable T data, @NonNull String message, int serverCode) {
        this.status = status;
        this.data = data;
        this.message = message;
        this.serverCode = serverCode;
    }

    public static <T> Resource<T> success(@NonNull T data) {
        return new Resource<>(Status.SUCCESS, data, "Success");
    }

    public static <T> Resource<T> error(@NonNull String msg, @Nullable T data) {
        return new Resource<>(Status.ERROR, data, msg);
    }

    public static <T> Resource<T> error(int serverCode, @NonNull String msg, @Nullable T data) {
        return new Resource<>(Status.ERROR, data, msg, serverCode);
    }

    public static <T> Resource<T> loading(@Nullable T data) {
        return new Resource<>(Status.LOADING, data, "Loading");
    }
}
