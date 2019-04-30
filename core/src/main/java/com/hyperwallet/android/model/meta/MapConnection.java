package com.hyperwallet.android.model.meta;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Class for presenting Connection map in the TransferMethodConfiguration object @see {@link
 * TransferMethodConfiguration}
 */
public final class MapConnection<T extends Node> extends Connection<T> {

    private static final String TAG = MapConnection.class.getName();

    @Nullable
    private final LinkedHashMap<String, T> mNodeMap;

    /**
     * Constructor to build Connection map based on json and class
     *
     * @param data  Json object
     * @param clazz Class name
     */
    public MapConnection(@NonNull JSONObject data, @NonNull Class clazz) throws NoSuchMethodException,
            SecurityException, IllegalAccessException, IllegalArgumentException, InstantiationException,
            InvocationTargetException, JSONException {
        super(data, clazz);
        Constructor<?> constructor = clazz.getConstructor(JSONObject.class);
        JSONArray jsonArray = data.optJSONArray(NODES);
        if (jsonArray != null) {
            mNodeMap = new LinkedHashMap<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                T obj = (T) constructor.newInstance(jsonArray.getJSONObject(i));
                mNodeMap.put(obj.getCode(), obj);
            }
        } else {
            mNodeMap = null;
        }
    }

    @Nullable
    public Map<String, T> getNodeMap() {
        return mNodeMap;
    }
}
