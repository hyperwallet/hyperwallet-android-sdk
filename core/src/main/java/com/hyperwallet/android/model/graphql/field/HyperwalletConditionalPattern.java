package com.hyperwallet.android.model.graphql.field;

import androidx.annotation.NonNull;

import org.json.JSONObject;

public class HyperwalletConditionalPattern {

    private static final String PATTERN = "pattern";
    private static final String REGEX = "regex";

    private final String mPattern;
    private final String mRegex;

    public HyperwalletConditionalPattern(@NonNull final JSONObject jsonObject) {
        mPattern = jsonObject.optString(PATTERN);
        mRegex = jsonObject.optString(REGEX);
    }

    @NonNull
    public String getPattern() {
        return mPattern;
    }

    @NonNull
    public String getRegex() {
        return mRegex;
    }
}
