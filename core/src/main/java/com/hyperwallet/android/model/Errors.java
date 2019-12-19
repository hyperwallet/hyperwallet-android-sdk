/*
 * Copyright 2018 Hyperwallet
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
 * to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of
 * the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
 * THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.hyperwallet.android.model;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the object to hold list of Errors
 */
public class Errors {

    private static final String ERRORS = "errors";
    private List<Error> mErrors = new ArrayList<>(0);

    private Errors() {

    }

    /**
     * Construct a {@code Errors} object from {@link JSONObject} representation
     *
     * @param jsonObject raw data representation
     */
    public Errors(@NonNull JSONObject jsonObject) throws JSONException {
        fromJson(jsonObject);
    }

    /**
     * Construct a {@code Errors} object from List of {@link Error}
     *
     * @param errors list of {@link Error}
     */
    public Errors(@NonNull List<Error> errors) {
        mErrors = errors;
    }

    /**
     * Create an empty instance.
     *
     * @return Errors
     */
    public static Errors getEmptyInstance() {
        return new Errors();
    }

    @NonNull
    public List<Error> getErrors() {
        return mErrors;
    }

    private void fromJson(@NonNull JSONObject jsonObject) throws JSONException {
        JSONArray jsonArray = jsonObject.getJSONArray(ERRORS);
        for (int i = 0; i < jsonArray.length(); i++) {
            mErrors.add(new Error(jsonArray.getJSONObject(i)));
        }
    }

    public boolean containsInputError() {
        boolean hasInputError = false;
        for (Error error : mErrors) {
            if (error.getFieldName() != null
                    && !error.getFieldName().isEmpty()) {
                hasInputError = true;
                break;
            }
        }
        return hasInputError;
    }
}
