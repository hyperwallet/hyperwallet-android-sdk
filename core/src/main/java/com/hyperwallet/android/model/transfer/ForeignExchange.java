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
        return getField(SOURCE_AMOUNT);
    }

    @Nullable
    public String getSourceCurrency() {
        return getField(SOURCE_CURRENCY);
    }

    @Nullable
    public String getDestinationAmount() {
        return getField(DESTINATION_AMOUNT);
    }

    @Nullable
    public String getDestinationCurrency() {
        return getField(DESTINATION_CURRENCY);
    }

    @Nullable
    public String getRate() {
        return getField(RATE);
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
     * Please use {@code getField(@NonNull String key, @NonNull Class<T> clazz)} if the value is not a {@link String}
     * </p>
     *
     * @param key can only be a {@link String} that represents a {@link ForeignExchange.ForeignExchangeField}
     *            name
     * @return a {@link String} value that represents the value of a {@link ForeignExchange.ForeignExchangeField}
     */
    @Nullable
    public String getField(@NonNull @ForeignExchange.ForeignExchangeField String key) {
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
