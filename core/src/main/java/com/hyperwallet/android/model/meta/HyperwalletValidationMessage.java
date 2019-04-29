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
package com.hyperwallet.android.model.meta;

import androidx.annotation.NonNull;

import org.json.JSONObject;

public class HyperwalletValidationMessage {
    private static final String LENGTH = "length";
    private static final String PATTERN = "pattern";
    private static final String EMPTY = "empty";

    private final String mLength;
    private final String mPattern;
    private final String mEmpty;

    public HyperwalletValidationMessage(@NonNull JSONObject selectionOption) {
        mLength = selectionOption.optString(LENGTH);
        mPattern = selectionOption.optString(PATTERN);
        mEmpty = selectionOption.optString(EMPTY);
    }

    public String getLength() {
        return mLength;
    }

    public String getPattern() {
        return mPattern;
    }

    public String getEmpty() {
        return mEmpty;
    }
}
