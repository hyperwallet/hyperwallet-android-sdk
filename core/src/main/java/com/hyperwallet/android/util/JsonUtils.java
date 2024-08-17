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

import com.hyperwallet.android.model.JsonModel;
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

/**
 * Internal SDK use only. This class represents Parsing JSON string object to equivalent
 * model and converting {@link JSONObject} into a Mapped key-value pair data and vice-versa
 */
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
            } else if (element instanceof JsonModel) {
                jsonArray.put(i, ((JsonModel) element).toJsonObject());
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

    /**
     * TypeReference deserialization implementation that derives the {@code T} of {@link TypeReference} based
     * from passed JSON string data
     *
     * &nbsp;<p>
     * Moreover this approach preserves the intended Type in the {@code n}th Generic form; for example
     * List&lt;T&gt; where {@code T} is Class&lt;T&gt;
     * </p>&nbsp;
     *
     * For this formal generator to work all types should follow constructor signature contract of the following:
     * <ul>
     * <li>Type Constructor({@link JSONObject})</li>
     * <li>Type Constructor({@link JSONObject}, {@link Class})</li>
     * </ul>
     *
     * @param data          JSON serialized data
     * @param typeReference Type of class specified by {@code T} that, we want to derive based from JSON response
     *                      context
     * @return the T representation equivalent from JSON data
     */
    @Nullable
    public static <T> T fromJsonString(@NonNull final String data, @NonNull final TypeReference<T> typeReference)
            throws JSONException, InvocationTargetException, NoSuchMethodException, InstantiationException,
            IllegalAccessException {
        if (typeReference.getType() instanceof Class<?>) {
            return fromJsonString(data, (Class<T>) typeReference.getType());
        }
        Class<T> rawType = (Class<T>) ((ParameterizedType) typeReference.getType()).getRawType();
        Class<T> parameterType =
                (Class<T>) ((ParameterizedType) typeReference.getType()).getActualTypeArguments()[0];
        JSONObject jsonObject = new JSONObject(data);
        Constructor<T> rawTypeConstructor = rawType.getDeclaredConstructor(JSONObject.class, parameterType.getClass());
        return rawTypeConstructor.newInstance(jsonObject, parameterType);
    }

    /**
     * Class type reference deserialization implementation that derives a simplest generic form from {@code Class<T>}
     * format
     *
     * @param jsonString JSON serialized data
     * @param fromClass  class type specified in {@code T}
     * @return the T representation equivalent from JSON data
     */
    @Nullable
    private static <T> T fromJsonString(@Nullable final String jsonString, @NonNull final Class<T> fromClass)
            throws JSONException, InvocationTargetException, NoSuchMethodException, InstantiationException,
            IllegalAccessException {
        JSONObject jsonObject = new JSONObject(jsonString);
        Constructor<T> constructor = fromClass.getDeclaredConstructor(JSONObject.class);
        return constructor.newInstance(jsonObject);
    }
}
