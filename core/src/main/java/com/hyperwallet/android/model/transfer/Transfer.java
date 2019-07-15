package com.hyperwallet.android.model.transfer;

import static com.hyperwallet.android.model.transfer.Transfer.TransferFields.CLIENT_TRANSFER_ID;
import static com.hyperwallet.android.model.transfer.Transfer.TransferFields.CREATED_ON;
import static com.hyperwallet.android.model.transfer.Transfer.TransferFields.DESTINATION_AMOUNT;
import static com.hyperwallet.android.model.transfer.Transfer.TransferFields.DESTINATION_CURRENCY;
import static com.hyperwallet.android.model.transfer.Transfer.TransferFields.DESTINATION_TOKEN;
import static com.hyperwallet.android.model.transfer.Transfer.TransferFields.EXPIRES_ON;
import static com.hyperwallet.android.model.transfer.Transfer.TransferFields.FOREIGN_EXCHANGES;
import static com.hyperwallet.android.model.transfer.Transfer.TransferFields.MEMO;
import static com.hyperwallet.android.model.transfer.Transfer.TransferFields.NOTES;
import static com.hyperwallet.android.model.transfer.Transfer.TransferFields.SOURCE_AMOUNT;
import static com.hyperwallet.android.model.transfer.Transfer.TransferFields.SOURCE_CURRENCY;
import static com.hyperwallet.android.model.transfer.Transfer.TransferFields.SOURCE_TOKEN;
import static com.hyperwallet.android.model.transfer.Transfer.TransferFields.STATUS;
import static com.hyperwallet.android.model.transfer.Transfer.TransferFields.TOKEN;
import static com.hyperwallet.android.model.transfer.Transfer.TransferStatuses.CANCELLED;
import static com.hyperwallet.android.model.transfer.Transfer.TransferStatuses.COMPLETED;
import static com.hyperwallet.android.model.transfer.Transfer.TransferStatuses.EXPIRED;
import static com.hyperwallet.android.model.transfer.Transfer.TransferStatuses.FAILED;
import static com.hyperwallet.android.model.transfer.Transfer.TransferStatuses.IN_PROGRESS;
import static com.hyperwallet.android.model.transfer.Transfer.TransferStatuses.QUOTED;
import static com.hyperwallet.android.model.transfer.Transfer.TransferStatuses.RETURNED;
import static com.hyperwallet.android.model.transfer.Transfer.TransferStatuses.SCHEDULED;
import static com.hyperwallet.android.model.transfer.Transfer.TransferStatuses.VERIFICATION_REQUIRED;
import static com.hyperwallet.android.util.DateUtil.fromDateTimeString;
import static com.hyperwallet.android.util.DateUtil.toDateTimeFormat;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringDef;
import androidx.annotation.VisibleForTesting;

import com.hyperwallet.android.model.HyperwalletJsonModel;
import com.hyperwallet.android.util.JsonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents the object to hold  transfer data fields.
 */
public final class Transfer implements HyperwalletJsonModel, Parcelable {

    public static final class TransferFields {
        private TransferFields() {
        }

        public static final String TOKEN = "token";
        public static final String STATUS = "status";
        public static final String CREATED_ON = "createdOn";
        public static final String CLIENT_TRANSFER_ID = "clientTransferId";
        public static final String SOURCE_TOKEN = "sourceToken";
        public static final String SOURCE_AMOUNT = "sourceAmount";
        public static final String SOURCE_CURRENCY = "sourceCurrency";
        public static final String DESTINATION_TOKEN = "destinationToken";
        public static final String DESTINATION_AMOUNT = "destinationAmount";
        public static final String DESTINATION_CURRENCY = "destinationCurrency";
        public static final String FOREIGN_EXCHANGES = "foreignExchanges";
        public static final String NOTES = "notes";
        public static final String MEMO = "memo";
        public static final String EXPIRES_ON = "expiresOn";
    }

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            TOKEN,
            STATUS,
            CREATED_ON,
            CLIENT_TRANSFER_ID,
            SOURCE_TOKEN,
            SOURCE_AMOUNT,
            SOURCE_CURRENCY,
            DESTINATION_TOKEN,
            DESTINATION_AMOUNT,
            DESTINATION_CURRENCY,
            FOREIGN_EXCHANGES,
            NOTES,
            MEMO,
            EXPIRES_ON
    })
    public @interface TransferField {
    }

    public static final class TransferStatuses {
        private TransferStatuses() {
        }

        public static final String QUOTED = "QUOTED";
        public static final String SCHEDULED = "SCHEDULED";
        public static final String IN_PROGRESS = "IN_PROGRESS";
        public static final String VERIFICATION_REQUIRED = "VERIFICATION_REQUIRED";
        public static final String COMPLETED = "COMPLETED";
        public static final String CANCELLED = "CANCELLED";
        public static final String RETURNED = "RETURNED";
        public static final String FAILED = "FAILED";
        public static final String EXPIRED = "EXPIRED";
    }

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            QUOTED,
            SCHEDULED,
            IN_PROGRESS,
            VERIFICATION_REQUIRED,
            COMPLETED,
            CANCELLED,
            RETURNED,
            FAILED,
            EXPIRED
    })
    public @interface TransferStatus {
    }

    private Map<String, Object> mFields;

    /**
     * Constructor to build Transfer, based on json object
     *
     * @param jsonObject json object with transfer data fields
     */
    public Transfer(@NonNull final JSONObject jsonObject) throws JSONException {
        toMap(jsonObject);
        if (mFields.get(FOREIGN_EXCHANGES) instanceof List)  {
            List<Map<String, Object>> rawMaps = (List<Map<String, Object>>) mFields.get(FOREIGN_EXCHANGES);
            List<ForeignExchange> foreignExchanges = new ArrayList<>(1);
            for (Map<String, Object> field : rawMaps) {
                foreignExchanges.add(new ForeignExchange(field));
            }
            mFields.put(FOREIGN_EXCHANGES, foreignExchanges);
        }
    }

    /**
     * Constructor to build Transfer, based on map
     *
     * @param fields map contains transfer data fields
     */
    public Transfer(@NonNull final Map<String, Object> fields) {
        super();
        setFields(fields);
    }

    @Nullable
    public String getToken() {
        return getFieldValueToString(TOKEN);
    }

    @Nullable
    public String getStatus() {
        return getFieldValueToString(STATUS);
    }

    @Nullable
    public Date getCreatedOn() {
        return getDateValue(CREATED_ON);
    }

    @Nullable
    public String getClientTransferId() {
        return getFieldValueToString(CLIENT_TRANSFER_ID);
    }

    @Nullable
    public String getSourceToken() {
        return getFieldValueToString(SOURCE_TOKEN);
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
    public String getDestinationToken() {
        return getFieldValueToString(DESTINATION_TOKEN);
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
    public List<ForeignExchange> getForeignExchanges() {
        return (List<ForeignExchange>) mFields.get(FOREIGN_EXCHANGES);
    }

    @Nullable
    public String getNotes() {
        return getFieldValueToString(NOTES);
    }

    @Nullable
    public String getMemo() {
        return getFieldValueToString(MEMO);
    }

    @Nullable
    public Date getExpiresOn() {
        return getDateValue(EXPIRES_ON);
    }


    public static final Creator<Transfer> CREATOR =
            new Creator<Transfer>() {
                @Override
                public Transfer createFromParcel(Parcel source) {
                    final Map<String, Object> fields = new HashMap<>();
                    source.readMap(fields, this.getClass().getClassLoader());
                    return new Transfer(fields);
                }

                @Override
                public Transfer[] newArray(int size) {
                    return new Transfer[0];
                }
            };

    protected void setFields(@NonNull Map<String, Object> fields) {
        mFields = fields;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeMap(mFields);
    }

    /**
     * Returns a {@link String} value of a {@code Map<String, String>}
     * <p>
     * Please use {@code getFieldValueToString(@NonNull String key, @NonNull Class<T> clazz)} if the value is not a {@link String}
     * </p>
     *
     * @param key can only be a {@link String} that represents a {@link Transfer.TransferField} name
     * @return a {@link String} value that represents the value of a {@link TransferField}
     */
    @Nullable
    String getFieldValueToString(@NonNull @Transfer.TransferField String key) {
        return mFields.get(key) != null ? (String) mFields.get(key) : null;
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
     * Converts a {@code Map<String, Object>} to a {@link JSONObject}
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
     * Returns the valid Date type or null in case the content is invalid
     *
     * @param queryKey the key to get the object in the query map
     * @return the valid Date value or null
     */
    @VisibleForTesting
    @Nullable
    protected final Date getDateValue(@NonNull final String queryKey) {
        if (containsKeyAndHasValue(queryKey)) {
            return fromDateTimeString((String) mFields.get(queryKey));
        }
        return null;
    }

    /**
     * Checks the query contains the key and has value
     *
     * @param key the query key to access the object in the map
     * @return the key has valid data to use
     */
    protected boolean containsKeyAndHasValue(@NonNull final String key) {
        return mFields.containsKey(key) && mFields.get(key) != null;
    }

    /**
     * Builder Class for the {@link Transfer}
     */
    public static class Builder {
        private Map<String, Object> mFields;

        public Builder() {
            mFields = new HashMap<>();
        }

        public Builder token(@NonNull final String token) {
            mFields.put(TOKEN, token);
            return this;
        }

        public Builder status(@NonNull @TransferStatus final String status) {
            mFields.put(TOKEN, status);
            return this;
        }

        public Builder createdOn(@NonNull final Date createdOn) {
            mFields.put(CREATED_ON, toDateTimeFormat(createdOn));
            return this;
        }

        public Builder clientTransferID(@NonNull final String clientTransferID) {
            mFields.put(CLIENT_TRANSFER_ID, clientTransferID);
            return this;
        }

        public Builder sourceToken(@NonNull final String sourceToken) {
            mFields.put(SOURCE_TOKEN, sourceToken);
            return this;
        }

        public Builder sourceAmount(@NonNull final String sourceAmount) {
            mFields.put(SOURCE_AMOUNT, sourceAmount);
            return this;
        }

        public Builder sourceCurrency(@NonNull final String sourceCurrency) {
            mFields.put(SOURCE_CURRENCY, sourceCurrency);
            return this;
        }

        public Builder destinationToken(@NonNull final String destinationToken) {
            mFields.put(DESTINATION_TOKEN, destinationToken);
            return this;
        }

        public Builder destinationAmount(@NonNull final String destinationAmount) {
            mFields.put(DESTINATION_AMOUNT, destinationAmount);
            return this;
        }

        public Builder destinationCurrency(@NonNull final String destinationCurrency) {
            mFields.put(DESTINATION_CURRENCY, destinationCurrency);
            return this;
        }

        public Builder foreignExchanges(@NonNull final List<ForeignExchange> foreignExchanges) {
            mFields.put(FOREIGN_EXCHANGES, foreignExchanges);
            return this;
        }

        public Builder notes(@NonNull final String notes) {
            mFields.put(NOTES, notes);
            return this;
        }

        public Builder memo(@NonNull final String memo) {
            mFields.put(MEMO, memo);
            return this;
        }

        public Transfer build() {
            return new Transfer(mFields);
        }
    }
}
