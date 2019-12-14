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

package com.hyperwallet.android.model.graphql;

import androidx.annotation.NonNull;

import org.json.JSONObject;

/**
 * Class that represents the information related to GraphQl pagination.
 */
public class PageInfo {
    private static final String LIMIT = "limit";
    private static final String OFFSET = "offset";

    private int mLimit;
    private int mOffset;

    /**
     * Construct a {@code PageInfo} object from {@link JSONObject} representation
     *
     * @param jsonObject raw data representation
     */
    public PageInfo(@NonNull final JSONObject jsonObject) {
        mLimit = jsonObject.optInt(LIMIT, 0);
        mOffset = jsonObject.optInt(OFFSET, 0);
    }

    /**
     * @return page limit information
     */
    public int getLimit() {
        return mLimit;
    }

    /**
     * @return page offset information
     */
    public int getOffset() {
        return mOffset;
    }
}
