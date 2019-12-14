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
 * {@code ValidationMessage} represents validation error message data to be shown to user when
 * one of the following errors are raised from user input:
 *
 * <ul>
 * <li>Length of input is not met</li>
 * <li>Input pattern does not match</li>
 * <li>If in case the input data is required, then input data should not be empty and will display empty message</li>
 * </ul>
 */
public class HyperwalletValidationMessage {
    private static final String LENGTH = "length";
    private static final String PATTERN = "pattern";
    private static final String EMPTY = "empty";

    private final String mLength;
    private final String mPattern;
    private final String mEmpty;

    /**
     * Construct a {@code ValidationMessage} object from {@link JSONObject} representation
     */
    public HyperwalletValidationMessage(@NonNull final JSONObject jsonObject) {
        mLength = jsonObject.optString(LENGTH);
        mPattern = jsonObject.optString(PATTERN);
        mEmpty = jsonObject.optString(EMPTY);
    }

    /**
     * @return length error message
     */
    public String getLength() {
        return mLength;
    }

    /**
     * @return pattern error message
     */
    public String getPattern() {
        return mPattern;
    }

    /**
     * @return empty error message
     */
    public String getEmpty() {
        return mEmpty;
    }
}
