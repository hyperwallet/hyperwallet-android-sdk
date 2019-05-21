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
import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@code GqlError} class represents a GraphQL error as defined by the June 2018 specification.
 * <pre>{@code {
 *      "message": "...",
 *      "locations": "...",
 *      "path": [...],
 *      "extensions": {...}
 *  }}</pre>
 */
public class GqlError {
    private static final String TAG = GqlError.class.getName();

    private static final String EXTENSIONS = "extensions";
    private static final String LOCATIONS = "locations";
    private static final String MESSAGE = "message";
    private static final String PATH = "path";

    private final Extension mExtension;
    private final List<Location> mLocations;
    private final String mMessage;
    private final List<String> mPaths;

    public GqlError(@NonNull JSONObject jsonObject) throws JSONException {
        mLocations = new ArrayList<>(0);
        mPaths = new ArrayList<>(0);

        mMessage = jsonObject.optString(MESSAGE);
        JSONArray locationJsonArray = jsonObject.optJSONArray(LOCATIONS);
        if (locationJsonArray != null && locationJsonArray.length() != 0) {
            for (int i = 0; i < locationJsonArray.length(); i++) {
                mLocations.add(new Location(locationJsonArray.getJSONObject(i)));
            }
        }

        JSONArray pathJsonArray = jsonObject.optJSONArray(PATH);
        if (pathJsonArray != null && pathJsonArray.length() != 0) {
            for (int i = 0; i < pathJsonArray.length(); i++) {
                mPaths.add(pathJsonArray.optString(i));
            }
        }

        JSONObject extensionJsonObject = jsonObject.optJSONObject(EXTENSIONS);
        if (extensionJsonObject != null && extensionJsonObject.length() != 0) {
            mExtension = new Extension(extensionJsonObject);
        } else {
            mExtension = null;
        }
    }

    /**
     * Returns the {@link Extension} associated with this error if available, null otherwise.
     *
     * @return the {@code Extension} associated with this error if available, null otherwise
     */
    @Nullable
    public Extension getExtensions() {
        return mExtension;
    }

    /**
     * Returns the {@link Location} associated with this error.
     *
     * @return the {@code Location} associated with this error
     */
    @NonNull
    public List<Location> getLocations() {
        return mLocations;
    }

    /**
     * Returns the error description that caused this object to be created.
     *
     * @return the error description that caused this object to be created
     */
    @NonNull
    public String getMessage() {
        return mMessage;
    }

    /**
     * Returns the {@code List} of paths that point out where in the serialized request this error occurred.
     *
     * @return the {@code List} of paths that point out where this error occurred
     */
    @NonNull
    public List<String> getPaths() {
        return mPaths;
    }
}
