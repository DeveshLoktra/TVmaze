package com.loktra.tvmaze.base;

import android.arch.lifecycle.MutableLiveData;
import android.os.Looper;

/**
 * Mutable Live Data with set value executed on main thread
 */
public class ThreadSafeLiveData<T> extends MutableLiveData<T> {

    public static final String TAG = ThreadSafeLiveData.class.getSimpleName();

    /**
     * Calls set value method on main thread
     *
     * @param value Data
     */
    @Override
    public void setValue(final T value) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            super.setValue(value);
            //LogHelper.logInfo(TAG, "set value called from main thread");
        } else {
            postValue(value);
        }
    }
}
