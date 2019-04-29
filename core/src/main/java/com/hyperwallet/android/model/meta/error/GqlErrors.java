/*
 * The MIT License (MIT)
 * Copyright (c) 2018 Hyperwallet Systems Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.hyperwallet.android.model.meta.error;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@code GqlErrors} class is a container for the list of {@link GqlError}s.
 *
 * <p>The {@code JSONObject} passed in will have to contain a valid set of name/value pairs as described by the June
 * 2018
 * GraphQL specification.</p>
 */
public class GqlErrors {

    private static final String ERRORS = "errors";

    private final List<GqlError> mGqlErrors;

    /**
     * Creates a new list of {@link GqlError}s based on the data contained within the {@code JSONObject}.
     *
     * <p>The {@code JSONObject} passed in will have to contain a valid set of name/value pairs as described by the
     * June 2018 GraphQL specification.</p>
     *
     * @param jsonObject the {@code JSONObject} that will be used to create the {@code GqlErrors}, must be non null
     * @throws JSONException if the {@code JSONObject} is malformed
     */
    public GqlErrors(@NonNull JSONObject jsonObject) throws JSONException {
        mGqlErrors = new ArrayList<>(0);
        JSONArray jsonArray = jsonObject.getJSONArray(ERRORS);
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.length(); i++) {
                mGqlErrors.add(new GqlError(jsonArray.getJSONObject(i)));
            }
        }
    }

    /**
     * Returns the list of {@link GqlError}s.
     *
     * @return the list of {@code GqlError}s
     */
    @NonNull
    public List<GqlError> getGQLErrors() {
        return mGqlErrors;
    }
}
