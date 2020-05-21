/*
 *  The MIT License (MIT)
 *  Copyright (c) 2019 Hyperwallet Systems Inc.
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 *  associated documentation files (the "Software"), to deal in the Software without restriction,
 *  including without limitation the rights to use, copy, modify, merge, publish, distribute,
 *  sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 *  NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 *  NON INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 *  DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.hyperwallet.android.model.graphql.keyed;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hyperwallet.android.model.graphql.Connection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Represents a Connection/Edges of a Node that can also be reference by a key, this means that the {@code
 * MappedConnection}
 * edges will have the ability to reference a Node through by a specific key assigned to a Node.
 *
 * <p>To make the {@code MappedConnection} effective the Node itself must be keyed so that we will know the identity of
 * that Node when
 * placed in the graph.</p>
 *
 * <p>{@code MappedConnection} is derived from {@code Connection} that means iterating a connection without using keys
 * is also possible</p>
 */
public class MappedConnection<T extends KeyedNode> extends Connection<T> {

    private final Map<String, T> mNodes;

    /**
     * Constructor to build MappedConnection based on {@link JSONObject} representation and class type
     *
     * @param data  JSON object that represents data
     * @param clazz Target class name to construct
     */
    public MappedConnection(@NonNull final JSONObject data, @NonNull Class clazz) throws NoSuchMethodException,
            JSONException, IllegalAccessException, InstantiationException, InvocationTargetException {

        super(data, clazz);
        Constructor<?> constructor = clazz.getConstructor(JSONObject.class);
        JSONArray jsonArray = data.optJSONArray(NODES);
        if (jsonArray != null && jsonArray.length() != 0) {
            mNodes = new LinkedHashMap<>(jsonArray.length());
            for (int i = 0; i < jsonArray.length(); i++) {
                T obj = (T) constructor.newInstance(jsonArray.getJSONObject(i));
                mNodes.put(obj.getCode(), obj);
            }
        } else {
            mNodes = null;
        }
    }

    /**
     * Get Node/Vertices identified by Node key
     *
     * @param key identifying Node/Vertices
     */
    @Nullable
    public T getNode(@NonNull final String key) {
        if (mNodes != null) {
            return mNodes.get(key);
        }
        return null;
    }

    /**
     * Node/Vertices map
     *
     * @return Map of keyed nodes
     */
    @Nullable
    public Map<String, T> getNodeMap() {
        return mNodes;
    }
}
