package com.hyperwallet.android.model.receipt;

import static com.hyperwallet.android.model.receipt.HyperwalletReceipt.ReceiptFields.TYPE;

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
import java.util.List;
import java.util.Map;

public final class HyperwalletReceipt implements HyperwalletJsonModel, Parcelable {

    public interface Entries {
        String CREDIT = "CREDIT";
        String DEBIT = "DEBIT";
    }

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            Entries.CREDIT,
            Entries.DEBIT
    })
    public @interface Entry {
    }


    /**
     * Common Receipt field keys
     */
    public interface ReceiptFields {
        String JOURNAL_ID = "journalId";
        String TYPE = "type";
        String CREATED_ON = "createdOn";
        String ENTRY = "entry";
        String SOURCE_TOKEN = "sourceToken";
        String DESTINATION_TOKEN = "destinationToken";
        String AMOUNT = "amount";
        String FEE = "fee";
        String FOREIGN_EXCHANGE_RATE = "foreignExchangeRate";
        String FOREIGN_EXCHANGE_CURRENCY = "foreignExchangeCurrency";
        String CURRENCY = "currency";
        String DETAILS = "details";
    }

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            ReceiptFields.JOURNAL_ID,
            TYPE,
            ReceiptFields.CREATED_ON,
            ReceiptFields.ENTRY,
            ReceiptFields.SOURCE_TOKEN,
            ReceiptFields.DESTINATION_TOKEN,
            ReceiptFields.AMOUNT,
            ReceiptFields.FEE,
            ReceiptFields.FOREIGN_EXCHANGE_RATE,
            ReceiptFields.FOREIGN_EXCHANGE_CURRENCY,
            ReceiptFields.CURRENCY,
            ReceiptFields.DETAILS
    })
    public @interface ReceiptField {
    }

    private Map<String, Object> mFields;

    public HyperwalletReceipt(@NonNull JSONObject jsonObject) throws JSONException {
        toMap(jsonObject);
    }

    private HyperwalletReceipt(@NonNull Map<String, Object> fields) {
        super();
        setFields(fields);
    }

    @Nullable
    public String getJournalId() {
        return (String) mFields.get(ReceiptFields.JOURNAL_ID);
    }

    @Nullable
    public String getType() {
        return (String) mFields.get(ReceiptFields.TYPE);
    }

    @Nullable
    public String getCreatedOn() {
        return (String) mFields.get(ReceiptFields.CREATED_ON);
    }

    @Nullable
    public String getEntry() {
        return (String) mFields.get(ReceiptFields.JOURNAL_ID);
    }

    @Nullable
    public String getSourceToken() {
        return (String) mFields.get(ReceiptFields.SOURCE_TOKEN);
    }

    @Nullable
    public String getDestinationToken() {
        return (String) mFields.get(ReceiptFields.DESTINATION_TOKEN);
    }

    @Nullable
    public String getAmount() {
        return (String) mFields.get(ReceiptFields.AMOUNT);
    }

    @Nullable
    public String getFee() {
        return (String) mFields.get(ReceiptFields.FEE);
    }

    @Nullable
    public String getForeignExchangeRate() {
        return (String) mFields.get(ReceiptFields.FEE);
    }

    @Nullable
    public String getForeignExchangeCurrency() {
        return (String) mFields.get(ReceiptFields.FEE);
    }

    @Nullable
    public String getCurrency() {
        return (String) mFields.get(ReceiptFields.CURRENCY);
    }

    @Nullable
    public List<HyperwalletReceiptDetails> getDetails() {
        return (List<HyperwalletReceiptDetails>) mFields.get(ReceiptFields.DETAILS);
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
     * @param key can only be a {@link String} that represents a {@link ReceiptField} name
     * @return a {@link String} value that represents the value of a {@link ReceiptField}
     */
    @Nullable
    public String getField(@NonNull @HyperwalletReceipt.ReceiptField String key) {
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

    public static final Creator<HyperwalletReceipt> CREATOR =
            new Creator<HyperwalletReceipt>() {
                @Override
                public HyperwalletReceipt createFromParcel(Parcel source) {
                    final Map<String, Object> fields = new HashMap<>();
                    source.readMap(fields, this.getClass().getClassLoader());
                    return new HyperwalletReceipt(fields);
                }

                @Override
                public HyperwalletReceipt[] newArray(int size) {
                    return new HyperwalletReceipt[0];
                }
            };

    protected void setFields(@NonNull Map<String, Object> fields) {
        mFields = fields;
    }

    public static class Builder {
        private Map<String, Object> mFields;

        public Builder() {
            mFields = new HashMap<>();
        }

        public HyperwalletReceipt.Builder journalId(@NonNull final String journalId) {
            mFields.put(ReceiptFields.JOURNAL_ID, journalId);
            return this;
        }

        public HyperwalletReceipt.Builder type(@Nullable final String type) {
            mFields.put(ReceiptFields.TYPE, type);
            return this;
        }

        public HyperwalletReceipt.Builder createdOn(@Nullable final String createdOn) {
            mFields.put(ReceiptFields.CREATED_ON, createdOn);
            return this;
        }

        public HyperwalletReceipt.Builder entry(@Nullable @Entry final String entry) {
            mFields.put(ReceiptFields.ENTRY, entry);
            return this;
        }

        public HyperwalletReceipt.Builder sourceToken(@Nullable String sourceToken) {
            mFields.put(ReceiptFields.SOURCE_TOKEN, sourceToken);
            return this;
        }

        public HyperwalletReceipt.Builder destinationToken(@Nullable final String destinationToken) {
            mFields.put(ReceiptFields.DESTINATION_TOKEN, destinationToken);
            return this;
        }

        public HyperwalletReceipt.Builder fee(@Nullable final String fee) {
            mFields.put(ReceiptFields.DESTINATION_TOKEN, fee);
            return this;
        }

        public HyperwalletReceipt.Builder foreignExchangeRate(@Nullable final String foreignExchangeRate) {
            mFields.put(ReceiptFields.FOREIGN_EXCHANGE_RATE, foreignExchangeRate);
            return this;
        }

        public HyperwalletReceipt.Builder foreignExchangeCurrency(@Nullable final String foreignExchangeCurrency) {
            mFields.put(ReceiptFields.FOREIGN_EXCHANGE_CURRENCY, foreignExchangeCurrency);
            return this;
        }

        public HyperwalletReceipt.Builder currency(@Nullable final String currency) {
            mFields.put(ReceiptFields.CURRENCY, currency);
            return this;
        }

        public HyperwalletReceipt.Builder details(@Nullable final List<HyperwalletReceiptDetails> details) {
            mFields.put(ReceiptFields.DETAILS, details);
            return this;
        }

        public HyperwalletReceipt build() {
            return new HyperwalletReceipt(mFields);
        }
    }
}
