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

package com.hyperwallet.android.model.meta.error;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * The {@code Extension} class represents Hyperwallet specific data related to error conditions for GraphQL.
 *
 * <p>The two values represented by this class are the {@code code} and {@code timestamp}. Where the code represents
 * the Hyperwallet specific error code and the timestamp represents the moment in time when the error occurred.</p>
 */
public class Extension {

    private static final String CODE = "code";
    private static final String TIMESTAMP = "timestamp";

    private final String mCode;
    private final String mTimestamp;

    Extension(@NonNull JSONObject jsonObject) throws JSONException {
        mCode = jsonObject.optString(CODE);
        mTimestamp = jsonObject.optString(TIMESTAMP);
    }

    /**
     * Returns the Hyperwallet specific error code for the {@link GqlError}.
     *
     * @return the Hyperwallet specific error code
     */
    public String getCode() {
        return mCode;
    }

    /**
     * Returns the moment in time when the {@link GqlError} was created.
     *
     * @return the timestamp when the {@code GqlError} was created, in {@literal 'yyyy-MM-dd HH:mm:ss'} format
     */
    public String getTimestamp() {
        return mTimestamp;
    }
}
