/*
 *  The MIT License (MIT)
 *  Copyright (c) 2019 Hyperwallet Systems Inc.
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    /**
     * Constructs a {@code Mask} object from {@link JSONObject} representation
     */
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

    /**
     * @return Regex used to cleanup display value to api value
     */
    public String getScrubRegex() {
        return mScrubRegex;
    }

    /**
     * @return List of {@link ConditionalPattern}
     */
    public List<ConditionalPattern> getConditionalPatterns() {
        return mConditionalPatterns;
    }

    /**
     * @return {@code True} if and only if {@link Mask} has {@link #getConditionalPatterns()} configured;
     * {@code False} otherwise.
     */
    public boolean containsConditionalPattern() {
        return mConditionalPatterns != null && !mConditionalPatterns.isEmpty();
    }

    /**
     * Get specific pattern to apply based on input passed
     *
     * @param value input string passed
     * @return Pattern to use for formatting
     */
    public String getPattern(@NonNull final String value) {
        if (containsConditionalPattern()) {
            for (ConditionalPattern conditionalPattern : mConditionalPatterns) {
                Pattern pattern = Pattern.compile(conditionalPattern.getRegex());
                Matcher matcher = pattern.matcher(value);
                while (matcher.find()) {
                    if (matcher.end() > 0) {
                        return conditionalPattern.getPattern();
                    }
                }
            }
        }
        return mDefaultPattern;
    }
}

