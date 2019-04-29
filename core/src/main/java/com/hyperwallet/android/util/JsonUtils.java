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
package com.hyperwallet.android.util;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;

import com.hyperwallet.android.model.TypeReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/*
 *  This Utility is hidden on SDK integration, it is for internal use only.
 * */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public final class JsonUtils {

    /**
     * Converts a {@link JSONObject} to a {@code Map<String, Object>}
     *
     * @param jsonObject JSON object representation
     * @return Immutable reference of Map<String, Object>
     * @throw JSONException
     */
    @NonNull
    public static Map<String, Object> jsonObjectToMap(@NonNull JSONObject jsonObject) throws JSONException {
        Map<String, Object> map = new HashMap<>();
        Iterator<String> keyIterator = jsonObject.keys();
        while (keyIterator.hasNext()) {
            String key = keyIterator.next();
            Object value = jsonObject.opt(key);

            if (value == null || value == JSONObject.NULL) {
                map.put(key, null);
            } else if (value instanceof JSONObject) {
                map.put(key, jsonObjectToMap((JSONObject) value));
            } else if (value instanceof JSONArray) {
                map.put(key, mapJsonArrayToList((JSONArray) value));
            } else {
                map.put(key, value);
            }
        }
        return map;
    }

    /**
     * Converts a {@code HashMap<String, Object>} into a {@link JSONObject}.
     *
     * @param map Data representation of key/values
     * @return {@link JSONObject}
     * @throw JSONException
     */
    @NonNull
    public static JSONObject mapToJsonObject(@NonNull Map<String, Object> map)
            throws JSONException {
        JSONObject jsonObject = new JSONObject();
        for (String key : map.keySet()) {
            if (key == null) {
                continue;
            }

            Object value = map.get(key);

            if (value == null || value == JSONObject.NULL) {
                jsonObject.put(key, JSONObject.NULL);
            } else if (value instanceof Map<?, ?>) {
                Map<String, Object> mapValue = (Map<String, Object>) value;
                jsonObject.put(key, mapToJsonObject(mapValue));
            } else if (value instanceof List) {
                JSONArray jsonArray = getJSONArray((List) value);
                jsonObject.put(key, jsonArray);
            } else if (value instanceof Number || value instanceof Boolean) {
                jsonObject.put(key, value);
            } else {
                jsonObject.put(key, value.toString());
            }
        }
        return jsonObject;
    }

    private static JSONArray getJSONArray(List value) throws JSONException {
        JSONArray jsonArray = new JSONArray();
        List elements = value;
        for (int i = 0; i < elements.size(); i++) {
            Object element = elements.get(i);
            if (element instanceof Map) {
                jsonArray.put(i, mapToJsonObject((Map<String, Object>) element));
            } else if (element instanceof List) {
                getJSONArray((List) element);
            }
        }
        return jsonArray;
    }

    /**
     * Convert a {@link JSONArray} to {@code List<Object>}
     */
    @NonNull
    private static List mapJsonArrayToList(@NonNull JSONArray jsonArray) throws JSONException {
        List<Object> objectList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            if (jsonArray.isNull(i)) {
                continue;
            }

            Object ob = jsonArray.get(i);

            if (ob instanceof JSONArray) {
                objectList.add(mapJsonArrayToList((JSONArray) ob));
            } else if (ob instanceof JSONObject) {
                Map<String, Object> objectMap = jsonObjectToMap((JSONObject) ob);
                objectList.add(objectMap);
            } else {
                objectList.add(ob);
            }
        }
        return Collections.unmodifiableList(objectList);
    }

    @Nullable
    public static <T> T fromJsonString(@NonNull String data, @NonNull TypeReference<T> typeReference)
            throws JSONException, InvocationTargetException, NoSuchMethodException, InstantiationException,
            IllegalAccessException {
        if (typeReference.getType() instanceof Class<?>) {
            return fromJsonString(data, (Class<T>) typeReference.getType());
        }
        Class<T> rawType = (Class<T>) ((ParameterizedType) typeReference.getType()).getRawType();
        Class<T> parameterType =
                (Class<T>) ((ParameterizedType) typeReference.getType()).getActualTypeArguments()[0];
        JSONObject jsonObject = new JSONObject(data);
        Constructor<T> rawTypeConstructor = rawType.getConstructor(JSONObject.class, parameterType.getClass());
        return rawTypeConstructor.newInstance(jsonObject, parameterType);
    }

    @Nullable
    private static <T> T fromJsonString(@Nullable String jsonString, @NonNull Class<T> fromClass)
            throws JSONException, InvocationTargetException, NoSuchMethodException, InstantiationException,
            IllegalAccessException {
        JSONObject jsonObject = new JSONObject(jsonString);
        Constructor<T> constructor = fromClass.getConstructor(JSONObject.class);
        return constructor.newInstance(jsonObject);
    }
}
