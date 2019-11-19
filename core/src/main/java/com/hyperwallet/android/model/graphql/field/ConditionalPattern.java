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

import org.json.JSONObject;

/**
 * {@code ConditionalPattern} represents the input field format regex pattern needed on creation of an
 * account.
 * This aids on the input widget where rules and information about the input field is described in this
 * representation
 */
public class ConditionalPattern {

    private static final String PATTERN = "pattern";
    private static final String REGEX = "regex";

    private final String mPattern;
    private final String mRegex;

    public ConditionalPattern(@NonNull final JSONObject conditionalPatternJson) {
        mPattern = conditionalPatternJson.optString(PATTERN);
        mRegex = conditionalPatternJson.optString(REGEX);
    }

    public String getPattern() {
        return mPattern;
    }

    public String getRegex() {
        return mRegex;
    }
}

