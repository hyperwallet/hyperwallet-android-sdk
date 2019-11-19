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
 * {@code HyperwalletFieldMask} represents input field information needed on Field Formatting.
 * This aids on the formation of input widget where rules and information about the input field is described in this
 * representation
 */
public class HyperwalletFieldMask {

    private static final String DEFAULT_PATTERN = "defaultPattern";
    private static final String SCRUB_REGEX = "scrubRegex";
    private static final String CONDITIONAL_PATTERNS = "conditionalPatterns";

    private final String mDefaultPattern;
    private final String mScrubRegex;
    private final List<HyperwalletConditionalPattern> mConditionalPatterns;

    public HyperwalletFieldMask(@NonNull final JSONObject mask) {
        mDefaultPattern = mask.optString(DEFAULT_PATTERN);
        mScrubRegex = mask.optString(SCRUB_REGEX);

        JSONArray jsonArray = mask.optJSONArray(CONDITIONAL_PATTERNS);
        if (jsonArray != null) {
            mConditionalPatterns = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject maskObject = jsonArray.optJSONObject(i);
                if (maskObject != null) {
                    mConditionalPatterns.add(new HyperwalletConditionalPattern(maskObject));
                }
            }
        } else {
            mConditionalPatterns = null;
        }
    }

    public String getDefaultPattern() {
        return mDefaultPattern;
    }

    public String getScrubRegex() {
        return mScrubRegex;
    }

    public List<HyperwalletConditionalPattern> getConditionalPatterns() {
        return mConditionalPatterns;
    }

}

