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

package com.hyperwallet.android.model.transfer;

import static com.hyperwallet.android.model.transfer.ForeignExchange.ForeignExchangeFields.DESTINATION_AMOUNT;
import static com.hyperwallet.android.model.transfer.ForeignExchange.ForeignExchangeFields.DESTINATION_CURRENCY;
import static com.hyperwallet.android.model.transfer.ForeignExchange.ForeignExchangeFields.RATE;
import static com.hyperwallet.android.model.transfer.ForeignExchange.ForeignExchangeFields.SOURCE_AMOUNT;
import static com.hyperwallet.android.model.transfer.ForeignExchange.ForeignExchangeFields.SOURCE_CURRENCY;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringDef;

import com.hyperwallet.android.model.HyperwalletJsonModel;
import com.hyperwallet.android.util.JsonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents the object to hold foreign exchange data fields, is used in transfer object see:{@link Transfer}
 */
public final class ForeignExchange implements HyperwalletJsonModel, Parcelable {

    public static final class ForeignExchangeFields {
        private ForeignExchangeFields() {
        }

        public static final String SOURCE_AMOUNT = "sourceAmount";
        public static final String SOURCE_CURRENCY = "sourceCurrency";
        public static final String DESTINATION_AMOUNT = "destinationAmount";
        public static final String DESTINATION_CURRENCY = "destinationCurrency";
        public static final String RATE = "rate";
    }

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            SOURCE_AMOUNT,
            SOURCE_CURRENCY,
            DESTINATION_AMOUNT,
            DESTINATION_CURRENCY,
            RATE
    })
    public @interface ForeignExchangeField {
    }

    private Map<String, Object> mFields;

    public ForeignExchange(@NonNull final JSONObject jsonObject) throws JSONException {
        toMap(jsonObject);
    }

    public ForeignExchange(@NonNull final Map<String, Object> fields) {
        super();
        setFields(fields);
    }

    @Nullable
    public String getSourceAmount() {
        return getFieldValueToString(SOURCE_AMOUNT);
    }

    @Nullable
    public String getSourceCurrency() {
        return getFieldValueToString(SOURCE_CURRENCY);
    }

    @Nullable
    public String getDestinationAmount() {
        return getFieldValueToString(DESTINATION_AMOUNT);
    }

    @Nullable
    public String getDestinationCurrency() {
        return getFieldValueToString(DESTINATION_CURRENCY);
    }

    @Nullable
    public String getRate() {
        return getFieldValueToString(RATE);
    }

    public Map<String, Object> getFields() {
        return mFields;
    }

    /* Converts a {@code Map<String, Object>} to a {@link JSONObject}
     *
     * @return a {@link JSONObject}
     */
    @Override
    @NonNull
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

    /**
     * Returns a {@link String} value of a {@code Map<String, String>}
     * <p>
     * Please use {@code getFieldValueToString(@NonNull String key, @NonNull Class<T> clazz)} if the value is not a {@link String}
     * </p>
     *
     * @param key can only be a {@link String} that represents a {@link ForeignExchange.ForeignExchangeField}
     *            name
     * @return a {@link String} value that represents the value of a {@link ForeignExchange.ForeignExchangeField}
     */
    @Nullable
    public String getFieldValueToString(@NonNull @ForeignExchange.ForeignExchangeField String key) {
        return mFields.get(key) != null ? (String) mFields.get(key) : null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeMap(mFields);
    }

    public static final Creator<ForeignExchange> CREATOR =
            new Creator<ForeignExchange>() {
                @Override
                public ForeignExchange createFromParcel(Parcel source) {
                    final Map<String, Object> fields = new HashMap<>();
                    source.readMap(fields, this.getClass().getClassLoader());
                    return new ForeignExchange(fields);
                }

                @Override
                public ForeignExchange[] newArray(int size) {
                    return new ForeignExchange[0];
                }
            };

    protected void setFields(@NonNull Map<String, Object> fields) {
        mFields = fields;
    }

}
