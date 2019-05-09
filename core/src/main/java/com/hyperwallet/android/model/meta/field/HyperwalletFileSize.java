package com.hyperwallet.android.model.meta.field;

import androidx.annotation.NonNull;

import org.json.JSONObject;

/**
 * @code HyperwalletFileSize representation
 */
public class HyperwalletFileSize {

    private static final String MIN = "min";
    private static final String MAX = "max";

    private final int mMax;
    private final int mMin;

    public HyperwalletFileSize(@NonNull final JSONObject fileSize) {
        mMax = fileSize.optInt(MAX);
        mMin = fileSize.optInt(MIN);
    }

    public int getMax() {
        return mMax;
    }

    public int getMin() {
        return mMin;
    }
}
