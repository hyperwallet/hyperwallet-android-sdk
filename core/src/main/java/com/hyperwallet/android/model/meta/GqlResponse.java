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
package com.hyperwallet.android.model.meta;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hyperwallet.android.model.meta.error.GqlError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the root level response for all GraphQL queries
 */
public class GqlResponse<T> {

    private static final String ERRORS = "errors";
    private static final String DATA = "data";

    private final T mData;
    private final List<GqlError> mErrors;

    /**
     * Constructor to build GqlResponse based on json and class type
     *
     * @param response JSON object that represents data
     * @param clazz    Class name
     */
    public GqlResponse(@NonNull final JSONObject response, @NonNull final Class clazz)
            throws ReflectiveOperationException, JSONException {
        Constructor<?> constructor = clazz.getConstructor(JSONObject.class);
        mData = (T) constructor.newInstance(response.get(DATA));

        JSONArray jsonArray = response.optJSONArray(ERRORS);
        if (jsonArray != null) {
            mErrors = new ArrayList<>(jsonArray.length());
            for (int i = 0; i < jsonArray.length(); i++) {
                mErrors.add(new GqlError(jsonArray.getJSONObject(i)));
            }
        } else {
            mErrors = null;
        }
    }

    /**
     * Represents list of errors, empty list means no errors
     *
     * @return list of {@code GqlError}
     */
    @Nullable
    public List<GqlError> getErrors() {
        return mErrors;
    }

    /**
     * Represents data response form GraphQL query
     *
     * @return Nodes that represents GraphQL query node
     */
    @NonNull
    public T getData() {
        return mData;
    }
}
