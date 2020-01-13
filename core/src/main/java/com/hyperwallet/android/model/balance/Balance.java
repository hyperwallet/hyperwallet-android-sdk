/*
 * Copyright 2018 Hyperwallet
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and
 * to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of
 * the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING
 * BUT NOT LIMITED TO
 * THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO
 * EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN
 * AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE
 * USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 */

package com.hyperwallet.android.model.balance;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hyperwallet.android.model.JsonModel;
import com.hyperwallet.android.util.JsonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the balance fields
 */
public class Balance implements Parcelable, JsonModel {

    public static final String AMOUNT = "amount";
    public static final String CURRENCY = "currency";

    public static final Creator<Balance> CREATOR = new Creator<Balance>() {
        @Override
        public Balance createFromParcel(Parcel source) {
            final Map<String, Object> fields = new HashMap<>();
            source.readMap(fields, this.getClass().getClassLoader());
            return new Balance(fields);
        }

        @Override
        public Balance[] newArray(int size) {
            return new Balance[0];
        }
    };

    private Map<String, Object> mFields;

    /**
     * Construct a {@code Balance} object from {@link JSONObject} representation
     *
     * @param jsonObject raw data information
     */
    public Balance(@NonNull JSONObject jsonObject) throws JSONException {
        toMap(jsonObject);
    }

    /**
     * Construct a {@code Balance} object from Map of key-value pair representation
     *
     * @param fields map of key value-pair raw data information
     */
    private Balance(@NonNull final Map<String, Object> fields) {
        mFields = new HashMap<>(fields);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeMap(mFields);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * @return a {@link String} value that represents the value of a {@link #AMOUNT}
     */
    @Nullable
    public String getAmount() {
        return mFields.get(AMOUNT) != null ? (String) mFields.get(AMOUNT) : null;
    }

    /**
     * @return a {@link String} value that represents the value of a {@link #CURRENCY}
     */
    @Nullable
    public String getCurrency() {
        return mFields.get(CURRENCY) != null ? (String) mFields.get(CURRENCY) : null;
    }

    /**
     * Converts a {@code Map<String, Object>} to a {@link JSONObject}
     *
     * @return a {@link JSONObject}
     */
    @NonNull
    @Override
    public JSONObject toJsonObject() throws JSONException {
        return JsonUtils.mapToJsonObject(mFields);
    }

    /**
     * Invokes {@link #toJsonObject()} and converts {@link JSONObject} to a String
     *
     * @return String of the {@link JSONObject}
     * @throws JSONException if any errors will occurred during json parsing
     */
    @NonNull
    @Override
    public String toJsonString() throws JSONException {
        return toJsonObject().toString();
    }

    /**
     * Convert a json String to a {@code Map<String, Object>}
     *
     * @param jsonObject is a response from Rest API in {@link String} format
     */
    private void toMap(@NonNull JSONObject jsonObject) throws JSONException {
        mFields = JsonUtils.jsonObjectToMap(jsonObject);
    }
}
