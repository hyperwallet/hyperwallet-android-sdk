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
import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents Node Connection/Edges that links between Nodes/Vertices that represents a graph of data
 */
public class Connection<T> {

    private static final String COUNT = "count";
    private static final String NODES = "nodes";
    private static final String PAGE_INFO = "pageInfo";

    private static final long DEFAULT_COUNT = 0L;

    private final long mCount;
    @Nullable
    private final List<T> mNodes;
    @Nullable
    private PageInfo mPageInfo;

    /**
     * Constructor to build Connection based on json and class
     *
     * @param data  Json object
     * @param clazz Class name
     */
    public Connection(@NonNull final JSONObject data, @NonNull final Class clazz) throws JSONException,
            NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        mCount = data.optLong(COUNT, DEFAULT_COUNT);
        JSONObject pageInfoObject = data.optJSONObject(PAGE_INFO);
        if (pageInfoObject != null) {
            mPageInfo = new PageInfo(pageInfoObject);
        }

        Constructor<?> constructor = clazz.getConstructor(JSONObject.class);
        JSONArray jsonArray = data.optJSONArray(NODES);
        if (jsonArray != null) {
            mNodes = new ArrayList<>(jsonArray.length());
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

    @Nullable
    public PageInfo getPageInfo() {
        return mPageInfo;
    }

    public static boolean isEmpty(@Nullable final Connection connection) {
        return connection != null && connection.getNodes() != null && !connection.getNodes().isEmpty();
    }
}
