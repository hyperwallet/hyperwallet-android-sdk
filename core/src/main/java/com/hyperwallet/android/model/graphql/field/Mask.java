/*
 *  The MIT License (MIT)
 *  Copyright (c) 2018 Hyperwallet Systems Inc.
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 *  associated documentation files (the "Software"), to deal in the Software without restriction,
 *  including without limitation the rights to use, copy, modify, merge, publish, distribute,
 *  sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 *  NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 *  NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 *  DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.hyperwallet.android.model.graphql.field;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * {@code Mask} represents input field information needed on Field Formatting.
 * This aids on the formation of input widget where rules and information about the input field is described in this
 * representation
 */
public class Mask {

    private static final String DEFAULT_PATTERN = "defaultPattern";
    private static final String SCRUB_REGEX = "scrubRegex";
    private static final String CONDITIONAL_PATTERNS = "conditionalPatterns";

    private final String mDefaultPattern;
    private final String mScrubRegex;
    private final List<ConditionalPattern> mConditionalPatterns;

    public Mask(@NonNull final JSONObject maskJson) {
        mDefaultPattern = maskJson.optString(DEFAULT_PATTERN);
        mScrubRegex = maskJson.optString(SCRUB_REGEX);

        JSONArray jsonArray = maskJson.optJSONArray(CONDITIONAL_PATTERNS);
        if (jsonArray != null) {
            mConditionalPatterns = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject conditionalPatternJson = jsonArray.optJSONObject(i);
                if (conditionalPatternJson != null) {
                    mConditionalPatterns.add(new ConditionalPattern(conditionalPatternJson));
                }
            }
        } else {
            mConditionalPatterns = null;
        }
    }

    public String getScrubRegex() {
        return mScrubRegex;
    }

    public List<ConditionalPattern> getConditionalPatterns() {
        return mConditionalPatterns;
    }

    private boolean containsConditionalPattern() {
        return mConditionalPatterns != null && !mConditionalPatterns.isEmpty();
    }

    public String getPattern(@NonNull final String value) {
        if (containsConditionalPattern()) {
            for (ConditionalPattern pattern : mConditionalPatterns) {
                if (value.matches(pattern.getRegex())) {
                    return pattern.getPattern();
                }
            }
        }
        return mDefaultPattern;
    }
}

