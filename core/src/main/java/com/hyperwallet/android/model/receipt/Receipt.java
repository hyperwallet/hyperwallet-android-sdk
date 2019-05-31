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

import static com.hyperwallet.android.model.receipt.Receipt.ReceiptFields.AMOUNT;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptFields.CREATED_ON;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptFields.CURRENCY;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptFields.DESTINATION_TOKEN;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptFields.DETAILS;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptFields.ENTRY;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptFields.FEE;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptFields.FOREIGN_EXCHANGE_CURRENCY;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptFields.FOREIGN_EXCHANGE_RATE;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptFields.JOURNAL_ID;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptFields.SOURCE_TOKEN;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptFields.TYPE;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptTypes.CARD_ACTIVATION_FEE;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptTypes.CARD_ACTIVATION_FEE_WAIVER;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptTypes.CARD_FEE;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptTypes.MANUAL_TRANSFER_TO_PREPAID_CARD;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptTypes.PAYMENT;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptTypes.PREPAID_CARD_BALANCE_INQUIRY_FEE;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptTypes.PREPAID_CARD_CASH_ADVANCE;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptTypes.PREPAID_CARD_DISPUTED_CHARGE_REFUND;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptTypes.PREPAID_CARD_DISPUTE_DEPOSIT;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptTypes.PREPAID_CARD_DOMESTIC_CASH_WITHDRAWAL_FEE;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptTypes.PREPAID_CARD_EXCHANGE_RATE_DIFFERENCE;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptTypes.PREPAID_CARD_MANUAL_UNLOAD;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptTypes.PREPAID_CARD_OVERSEAS_CASH_WITHDRAWAL_FEE;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptTypes.PREPAID_CARD_PIN_CHANGE_FEE;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptTypes.PREPAID_CARD_REFUND;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptTypes.PREPAID_CARD_REPLACEMENT_FEE;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptTypes.PREPAID_CARD_SALE;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptTypes.PREPAID_CARD_SALE_REVERSAL;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptTypes.PREPAID_CARD_UNLOAD;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptTypes.TRANSFER_TO_PREPAID_CARD;

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

    public final class Entries {
        public static final String CREDIT = "CREDIT";
        public static final String DEBIT = "DEBIT";
    }

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            Entries.CREDIT,
            Entries.DEBIT
    })
    public @interface Entry {
    }

    public final class ReceiptTypes {
        public static final String PAYMENT = "PAYMENT"; // todo: check
        public static final String CARD_ACTIVATION_FEE = "CARD_ACTIVATION_FEE";
        public static final String CARD_ACTIVATION_FEE_WAIVER = "CARD_ACTIVATION_FEE_WAIVER";
        public static final String CARD_FEE = "CARD_FEE";
        public static final String MANUAL_TRANSFER_TO_PREPAID_CARD = "MANUAL_TRANSFER_TO_PREPAID_CARD";
        public static final String PREPAID_CARD_BALANCE_INQUIRY_FEE = "PREPAID_CARD_BALANCE_INQUIRY_FEE";
        public static final String PREPAID_CARD_CASH_ADVANCE = "PREPAID_CARD_CASH_ADVANCE";
        public static final String PREPAID_CARD_DISPUTED_CHARGE_REFUND = "PREPAID_CARD_DISPUTED_CHARGE_REFUND";
        public static final String PREPAID_CARD_DISPUTE_DEPOSIT = "PREPAID_CARD_DISPUTE_DEPOSIT";
        public static final String PREPAID_CARD_DOMESTIC_CASH_WITHDRAWAL_FEE =
                "PREPAID_CARD_DOMESTIC_CASH_WITHDRAWAL_FEE";
        public static final String PREPAID_CARD_EXCHANGE_RATE_DIFFERENCE = "PREPAID_CARD_EXCHANGE_RATE_DIFFERENCE";
        public static final String PREPAID_CARD_MANUAL_UNLOAD = "PREPAID_CARD_MANUAL_UNLOAD";
        public static final String PREPAID_CARD_OVERSEAS_CASH_WITHDRAWAL_FEE =
                "PREPAID_CARD_OVERSEAS_CASH_WITHDRAWAL_FEE";
        public static final String PREPAID_CARD_PIN_CHANGE_FEE = "PREPAID_CARD_PIN_CHANGE_FEE";
        public static final String PREPAID_CARD_REFUND = "PREPAID_CARD_REFUND";
        public static final String PREPAID_CARD_REPLACEMENT_FEE = "PREPAID_CARD_REPLACEMENT_FEE";
        public static final String PREPAID_CARD_SALE = "PREPAID_CARD_SALE";
        public static final String PREPAID_CARD_SALE_REVERSAL = "PREPAID_CARD_SALE_REVERSAL";
        public static final String PREPAID_CARD_UNLOAD = "PREPAID_CARD_UNLOAD";
        public static final String TRANSFER_TO_PREPAID_CARD = "TRANSFER_TO_PREPAID_CARD";
    }

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            PAYMENT,
            CARD_ACTIVATION_FEE,
            CARD_ACTIVATION_FEE_WAIVER,
            CARD_FEE,
            MANUAL_TRANSFER_TO_PREPAID_CARD,
            PREPAID_CARD_BALANCE_INQUIRY_FEE,
            PREPAID_CARD_CASH_ADVANCE,
            PREPAID_CARD_DISPUTED_CHARGE_REFUND,
            PREPAID_CARD_DISPUTE_DEPOSIT,
            PREPAID_CARD_DOMESTIC_CASH_WITHDRAWAL_FEE,
            PREPAID_CARD_EXCHANGE_RATE_DIFFERENCE,
            PREPAID_CARD_MANUAL_UNLOAD,
            PREPAID_CARD_OVERSEAS_CASH_WITHDRAWAL_FEE,
            PREPAID_CARD_PIN_CHANGE_FEE,
            PREPAID_CARD_REFUND,
            PREPAID_CARD_REPLACEMENT_FEE,
            PREPAID_CARD_SALE,
            PREPAID_CARD_SALE_REVERSAL,
            PREPAID_CARD_UNLOAD,
            TRANSFER_TO_PREPAID_CARD
    })
    public @interface ReceiptType {
    }

    /**
     * Common Receipt field keys
     */
    public final class ReceiptFields {
        public static final String JOURNAL_ID = "journalId";
        public static final String TYPE = "type";
        public static final String CREATED_ON = "createdOn";
        public static final String ENTRY = "entry";
        public static final String SOURCE_TOKEN = "sourceToken";
        public static final String DESTINATION_TOKEN = "destinationToken";
        public static final String AMOUNT = "amount";
        public static final String FEE = "fee";
        public static final String FOREIGN_EXCHANGE_RATE = "foreignExchangeRate";
        public static final String FOREIGN_EXCHANGE_CURRENCY = "foreignExchangeCurrency";
        public static final String CURRENCY = "currency";
        public static final String DETAILS = "details";
    }

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            JOURNAL_ID,
            TYPE,
            CREATED_ON,
            ENTRY,
            SOURCE_TOKEN,
            DESTINATION_TOKEN,
            AMOUNT,
            FEE,
            FOREIGN_EXCHANGE_RATE,
            FOREIGN_EXCHANGE_CURRENCY,
            CURRENCY,
            DETAILS
    })
    public @interface ReceiptField {
    }

    private Map<String, Object> mFields;

    public Receipt(@NonNull JSONObject jsonObject) throws JSONException {
        toMap(jsonObject);
        if (jsonObject.has(DETAILS)) {
            mFields.put(DETAILS, new ReceiptDetails(jsonObject.getJSONObject(DETAILS)));
        }
    }

    private Receipt(@NonNull Map<String, Object> fields) {
        super();
        setFields(fields);
    }

    @Nullable
    public String getJournalId() {
        return getField(JOURNAL_ID);
    }

    @Nullable
    @ReceiptType
    public String getType() {
        return getField(TYPE);
    }

    @Nullable
    public String getCreatedOn() {
        return getField(CREATED_ON);
    }

    @Nullable
    @Entry
    public String getEntry() {
        return getField(ENTRY);
    }

    @Nullable
    public String getSourceToken() {
        return (String) mFields.get(SOURCE_TOKEN);
    }

    @Nullable
    public String getDestinationToken() {
        return getField(DESTINATION_TOKEN);
    }

    @Nullable
    public String getAmount() {
        return getField(AMOUNT);
    }

    @Nullable
    public String getFee() {
        return getField(FEE);
    }

    @Nullable
    public String getForeignExchangeRate() {
        return getField(FOREIGN_EXCHANGE_RATE);
    }

    @Nullable
    public String getForeignExchangeCurrency() {
        return getField(FOREIGN_EXCHANGE_CURRENCY);
    }

    @Nullable
    public String getCurrency() {
        return getField(CURRENCY);
    }

    @Nullable
    public ReceiptDetails getDetails() {
        return mFields.get(DETAILS) != null ? (ReceiptDetails) mFields.get(DETAILS) : null;
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
}
