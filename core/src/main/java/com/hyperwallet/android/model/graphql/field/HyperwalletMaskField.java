package com.hyperwallet.android.model.graphql.field;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HyperwalletMaskField {

    private static final String DEFAULT_PATTERN = "defaultPattern";
    private static final String SCRUB_REGEX = "scrubRegex";
    private static final String CONDITIONAL_PATTERNS = "conditionalPatterns";

    private final String mDefaultPattern;
    private final String mScrubRegex;
    private final List<HyperwalletConditionalPattern> mHyperwalletConditionalPatterns;

    public HyperwalletMaskField(@NonNull final JSONObject jsonObject) {
        mDefaultPattern = jsonObject.optString(DEFAULT_PATTERN);
        mScrubRegex = jsonObject.optString(SCRUB_REGEX);

        JSONArray jsonArray = jsonObject.optJSONArray(CONDITIONAL_PATTERNS);
        mHyperwalletConditionalPatterns = new ArrayList<>(1);
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject maskObject = jsonArray.optJSONObject(i);
                if (maskObject != null) {
                    mHyperwalletConditionalPatterns.add(new HyperwalletConditionalPattern(maskObject));
                }
            }
        }
    }

    @NonNull
    public String getDefaultPattern() {
        return mDefaultPattern;
    }

    @Nullable
    public String getScrubRegex() {
        return mScrubRegex;
    }

    @NonNull
    public List<HyperwalletConditionalPattern> getHyperwalletConditionalPatterns() {
        return mHyperwalletConditionalPatterns;
    }

    public boolean containsConditionalPattern() {
        return mHyperwalletConditionalPatterns != null && !mHyperwalletConditionalPatterns.isEmpty();
    }

    public String getConditionalPattern(@NonNull final String value) {
        for (HyperwalletConditionalPattern pattern : mHyperwalletConditionalPatterns) {
            if (value.matches(pattern.getRegex())) {
                return pattern.getPattern();
            }
        }
        return getDefaultPattern();
    }

}
