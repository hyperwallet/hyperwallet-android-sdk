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
import androidx.annotation.Nullable;

import com.hyperwallet.android.exception.HyperwalletException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for presenting Connection in the TransferMethodConfiguration object @see {@link TransferMethodConfiguration}
 */
public class Connection<T> {

    protected static final String NODES = "nodes";

    private static final String TAG = Connection.class.getName();
    private static final String COUNT = "count";
    private static final long DEFAULT_COUNT = 0L;

    private final long mCount;
    @Nullable
    private final List<T> mNodes;

    /**
     * Constructor to build Connection based on json and class
     *
     * @param data  Json object
     * @param clazz Class name
     */
    public Connection(@NonNull JSONObject data, @NonNull Class clazz) throws NoSuchMethodException, SecurityException,
            IllegalAccessException, IllegalArgumentException, InstantiationException, InvocationTargetException,
            JSONException {
        mCount = data.optLong(COUNT, DEFAULT_COUNT);

        Constructor<?> constructor = clazz.getConstructor(JSONObject.class);
        JSONArray jsonArray = data.optJSONArray(NODES);
        if (jsonArray != null) {
            mNodes = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                mNodes.add((T) constructor.newInstance(jsonArray.getJSONObject(i)));
            }
        } else {
            mNodes = null;
        }
    }

    public long getCount() {
        return mCount;
    }

    @Nullable
    public List<T> getNodes() {
        return mNodes;
    }
}
