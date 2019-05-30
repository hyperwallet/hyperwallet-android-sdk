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

package com.hyperwallet.android.model.receipt;

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
 * Represents the receipt fields
 */
public final class Receipt implements HyperwalletJsonModel, Parcelable {

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

    public interface ReceiptTypes {
        String PAYMENT = "PAYMENT"; // todo: check
        String CARD_ACTIVATION_FEE = "CARD_ACTIVATION_FEE";
        String CARD_ACTIVATION_FEE_WAIVER = "CARD_ACTIVATION_FEE_WAIVER";
        String CARD_FEE = "CARD_FEE";
        String MANUAL_TRANSFER_TO_PREPAID_CARD = "MANUAL_TRANSFER_TO_PREPAID_CARD";
        String PREPAID_CARD_BALANCE_INQUIRY_FEE = "PREPAID_CARD_BALANCE_INQUIRY_FEE";
        String PREPAID_CARD_CASH_ADVANCE = "PREPAID_CARD_CASH_ADVANCE";
        String PREPAID_CARD_DISPUTED_CHARGE_REFUND = "PREPAID_CARD_DISPUTED_CHARGE_REFUND";
        String PREPAID_CARD_DISPUTE_DEPOSIT = "PREPAID_CARD_DISPUTE_DEPOSIT";
        String PREPAID_CARD_DOMESTIC_CASH_WITHDRAWAL_FEE = "PREPAID_CARD_DOMESTIC_CASH_WITHDRAWAL_FEE";
        String PREPAID_CARD_EXCHANGE_RATE_DIFFERENCE = "PREPAID_CARD_EXCHANGE_RATE_DIFFERENCE";
        String PREPAID_CARD_MANUAL_UNLOAD = "PREPAID_CARD_MANUAL_UNLOAD";
        String PREPAID_CARD_OVERSEAS_CASH_WITHDRAWAL_FEE = "PREPAID_CARD_OVERSEAS_CASH_WITHDRAWAL_FEE";
        String PREPAID_CARD_PIN_CHANGE_FEE = "PREPAID_CARD_PIN_CHANGE_FEE";
        String PREPAID_CARD_REFUND = "PREPAID_CARD_REFUND";
        String PREPAID_CARD_REPLACEMENT_FEE = "PREPAID_CARD_REPLACEMENT_FEE";
        String PREPAID_CARD_SALE = "PREPAID_CARD_SALE";
        String PREPAID_CARD_SALE_REVERSAL = "PREPAID_CARD_SALE_REVERSAL";
        String PREPAID_CARD_UNLOAD = "PREPAID_CARD_UNLOAD";
        String TRANSFER_TO_PREPAID_CARD = "TRANSFER_TO_PREPAID_CARD";
    }

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            ReceiptTypes.PAYMENT,
            ReceiptTypes.CARD_ACTIVATION_FEE,
            ReceiptTypes.CARD_ACTIVATION_FEE_WAIVER,
            ReceiptTypes.CARD_FEE,
            ReceiptTypes.MANUAL_TRANSFER_TO_PREPAID_CARD,
            ReceiptTypes.PREPAID_CARD_BALANCE_INQUIRY_FEE,
            ReceiptTypes.PREPAID_CARD_CASH_ADVANCE,
            ReceiptTypes.PREPAID_CARD_DISPUTED_CHARGE_REFUND,
            ReceiptTypes.PREPAID_CARD_DISPUTE_DEPOSIT,
            ReceiptTypes.PREPAID_CARD_DOMESTIC_CASH_WITHDRAWAL_FEE,
            ReceiptTypes.PREPAID_CARD_EXCHANGE_RATE_DIFFERENCE,
            ReceiptTypes.PREPAID_CARD_MANUAL_UNLOAD,
            ReceiptTypes.PREPAID_CARD_OVERSEAS_CASH_WITHDRAWAL_FEE,
            ReceiptTypes.PREPAID_CARD_PIN_CHANGE_FEE,
            ReceiptTypes.PREPAID_CARD_REFUND,
            ReceiptTypes.PREPAID_CARD_REPLACEMENT_FEE,
            ReceiptTypes.PREPAID_CARD_SALE,
            ReceiptTypes.PREPAID_CARD_SALE_REVERSAL,
            ReceiptTypes.PREPAID_CARD_UNLOAD,
            ReceiptTypes.TRANSFER_TO_PREPAID_CARD
    })
    public @interface ReceiptType {
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
            ReceiptFields.TYPE,
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
    private ReceiptDetails mReceiptDetails;

    public Receipt(@NonNull JSONObject jsonObject) throws JSONException {
        toMap(jsonObject);
        if (jsonObject.has(ReceiptFields.DETAILS)) {
            mReceiptDetails = new ReceiptDetails(jsonObject.getJSONObject(ReceiptFields.DETAILS));
        }
    }

    private Receipt(@NonNull Map<String, Object> fields) {
        super();
        setFields(fields);
    }

    @Nullable
    public String getJournalId() {
        return (String) mFields.get(ReceiptFields.JOURNAL_ID);
    }

    @Nullable
    @ReceiptType
    public String getType() {
        return (String) mFields.get(ReceiptFields.TYPE);
    }

    @Nullable
    public String getCreatedOn() {
        return (String) mFields.get(ReceiptFields.CREATED_ON);
    }

    @Nullable
    @Entry
    public String getEntry() {
        return (String) mFields.get(ReceiptFields.ENTRY);
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
        return (String) mFields.get(ReceiptFields.FOREIGN_EXCHANGE_RATE);
    }

    @Nullable
    public String getForeignExchangeCurrency() {
        return (String) mFields.get(ReceiptFields.FOREIGN_EXCHANGE_CURRENCY);
    }

    @Nullable
    public String getCurrency() {
        return (String) mFields.get(ReceiptFields.CURRENCY);
    }

    @Nullable
    public ReceiptDetails getDetails() {
        return mReceiptDetails;
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
     * Please use {@code getField(@NonNull String key, @NonNull Class<T> clazz)} if the value is not a {@link
     * String}
     * </p>
     *
     * @param key can only be a {@link String} that represents a {@link ReceiptField} name
     * @return a {@link String} value that represents the value of a {@link ReceiptField}
     */
    @Nullable
    public String getField(@NonNull @Receipt.ReceiptField String key) {
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

    public static final Creator<Receipt> CREATOR =
            new Creator<Receipt>() {
                @Override
                public Receipt createFromParcel(Parcel source) {
                    final Map<String, Object> fields = new HashMap<>();
                    source.readMap(fields, this.getClass().getClassLoader());
                    return new Receipt(fields);
                }

                @Override
                public Receipt[] newArray(int size) {
                    return new Receipt[0];
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

        public Receipt.Builder journalId(@NonNull final String journalId) {
            mFields.put(ReceiptFields.JOURNAL_ID, journalId);
            return this;
        }

        public Receipt.Builder type(@NonNull @ReceiptType final String type) {
            mFields.put(ReceiptFields.TYPE, type);
            return this;
        }

        public Receipt.Builder createdOn(@NonNull final String createdOn) {
            mFields.put(ReceiptFields.CREATED_ON, createdOn);
            return this;
        }

        public Receipt.Builder entry(@NonNull @Entry final String entry) {
            mFields.put(ReceiptFields.ENTRY, entry);
            return this;
        }

        public Receipt.Builder sourceToken(@NonNull String sourceToken) {
            mFields.put(ReceiptFields.SOURCE_TOKEN, sourceToken);
            return this;
        }

        public Receipt.Builder destinationToken(@NonNull final String destinationToken) {
            mFields.put(ReceiptFields.DESTINATION_TOKEN, destinationToken);
            return this;
        }

        public Receipt.Builder amount(@NonNull final String amount) {
            mFields.put(ReceiptFields.AMOUNT, amount);
            return this;
        }

        public Receipt.Builder fee(@NonNull final String fee) {
            mFields.put(ReceiptFields.FEE, fee);
            return this;
        }

        public Receipt.Builder foreignExchangeRate(@NonNull final String foreignExchangeRate) {
            mFields.put(ReceiptFields.FOREIGN_EXCHANGE_RATE, foreignExchangeRate);
            return this;
        }

        public Receipt.Builder foreignExchangeCurrency(@NonNull final String foreignExchangeCurrency) {
            mFields.put(ReceiptFields.FOREIGN_EXCHANGE_CURRENCY, foreignExchangeCurrency);
            return this;
        }

        public Receipt.Builder currency(@NonNull final String currency) {
            mFields.put(ReceiptFields.CURRENCY, currency);
            return this;
        }

        public Receipt.Builder details(@NonNull final ReceiptDetails details) {
            try {
                mFields.put(ReceiptFields.DETAILS, details.toJsonString());
            } catch (JSONException e) {
            }
            return this;
        }

        public Receipt build() {
            return new Receipt(mFields);
        }
    }
}
