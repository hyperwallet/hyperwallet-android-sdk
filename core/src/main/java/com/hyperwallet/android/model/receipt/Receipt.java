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
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptTypes.ACCOUNT_CLOSURE;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptTypes.ACCOUNT_CLOSURE_FEE;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptTypes.ACCOUNT_UNLOAD;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptTypes.ADJUSTMENT;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptTypes.ANNUAL_FEE;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptTypes.ANNUAL_FEE_REFUND;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptTypes.BANK_ACCOUNT_TRANSFER_FEE;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptTypes.BANK_ACCOUNT_TRANSFER_RETURN;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptTypes.BANK_ACCOUNT_TRANSFER_RETURN_FEE;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptTypes.CARD_ACTIVATION_FEE;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptTypes.CARD_ACTIVATION_FEE_WAIVER;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptTypes.CARD_FEE;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptTypes.CUSTOMER_SERVICE_FEE;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptTypes.CUSTOMER_SERVICE_FEE_REFUND;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptTypes.DEPOSIT;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptTypes.DONATION;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptTypes.DONATION_FEE;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptTypes.DONATION_RETURN;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptTypes.DORMANT_USER_FEE;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptTypes.DORMANT_USER_FEE_REFUND;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptTypes.EXPEDITED_SHIPPING_FEE;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptTypes.FOREIGN_EXCHANGE;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptTypes.GENERIC_FEE_REFUND;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptTypes.MANUAL_ADJUSTMENT;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptTypes.MANUAL_TRANSFER_TO_PREPAID_CARD;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptTypes.MERCHANT_PAYMENT;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptTypes.MERCHANT_PAYMENT_FEE;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptTypes.MERCHANT_PAYMENT_REFUND;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptTypes.MERCHANT_PAYMENT_RETURN;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptTypes.MONEYGRAM_TRANSFER_RETURN;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptTypes.MONTHLY_FEE;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptTypes.MONTHLY_FEE_REFUND;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptTypes.PAPER_CHECK_FEE;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptTypes.PAPER_CHECK_REFUND;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptTypes.PAYMENT;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptTypes.PAYMENT_CANCELLATION;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptTypes.PAYMENT_EXPIRATION;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptTypes.PAYMENT_EXPIRY_FEE;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptTypes.PAYMENT_FEE;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptTypes.PAYMENT_RETURN;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptTypes.PAYMENT_REVERSAL;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptTypes.PAYMENT_REVERSAL_FEE;
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
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptTypes.PROCESSING_FEE;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptTypes.STANDARD_SHIPPING_FEE;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptTypes.TRANSFER_FEE;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptTypes.TRANSFER_TO_BANK_ACCOUNT;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptTypes.TRANSFER_TO_MONEYGRAM;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptTypes.TRANSFER_TO_PAPER_CHECK;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptTypes.TRANSFER_TO_PREPAID_CARD;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptTypes.TRANSFER_TO_PROGRAM_ACCOUNT;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptTypes.TRANSFER_TO_USER;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptTypes.TRANSFER_TO_WESTERN_UNION;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptTypes.TRANSFER_TO_WIRE;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptTypes.TRANSFER_TO_WUBS_WIRE;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptTypes.VIRTUAL_INCENTIVE_CANCELLATION;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptTypes.VIRTUAL_INCENTIVE_ISSUANCE;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptTypes.VIRTUAL_INCENTIVE_PURCHASE;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptTypes.VIRTUAL_INCENTIVE_REFUND;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptTypes.WESTERN_UNION_TRANSFER_RETURN;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptTypes.WIRE_TRANSFER_FEE;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptTypes.WIRE_TRANSFER_RETURN;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptTypes.WUBS_WIRE_TRANSFER_RETURN;

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
import java.util.Objects;

/**
 * Represents the receipt fields
 */
public final class Receipt implements HyperwalletJsonModel, Parcelable {

    public static final class Entries {
        private Entries() {
        }

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

    public static final class ReceiptTypes {

        private ReceiptTypes() {
        }

        public static final String ANNUAL_FEE = "ANNUAL_FEE";
        public static final String ANNUAL_FEE_REFUND = "ANNUAL_FEE_REFUND";
        public static final String CUSTOMER_SERVICE_FEE = "CUSTOMER_SERVICE_FEE";
        public static final String CUSTOMER_SERVICE_FEE_REFUND = "CUSTOMER_SERVICE_FEE_REFUND";
        public static final String EXPEDITED_SHIPPING_FEE = "EXPEDITED_SHIPPING_FEE";
        public static final String GENERIC_FEE_REFUND = "GENERIC_FEE_REFUND";
        public static final String MONTHLY_FEE = "MONTHLY_FEE";
        public static final String MONTHLY_FEE_REFUND = "MONTHLY_FEE_REFUND";
        public static final String PAYMENT_EXPIRY_FEE = "PAYMENT_EXPIRY_FEE";
        public static final String PAYMENT_FEE = "PAYMENT_FEE";
        public static final String PROCESSING_FEE = "PROCESSING_FEE";
        public static final String STANDARD_SHIPPING_FEE = "STANDARD_SHIPPING_FEE";
        public static final String TRANSFER_FEE = "TRANSFER_FEE";
        public static final String ADJUSTMENT = "ADJUSTMENT";
        public static final String FOREIGN_EXCHANGE = "FOREIGN_EXCHANGE";
        public static final String DEPOSIT = "DEPOSIT";
        public static final String MANUAL_ADJUSTMENT = "MANUAL_ADJUSTMENT";
        public static final String PAYMENT_EXPIRATION = "PAYMENT_EXPIRATION";
        public static final String BANK_ACCOUNT_TRANSFER_FEE = "BANK_ACCOUNT_TRANSFER_FEE";
        public static final String BANK_ACCOUNT_TRANSFER_RETURN = "BANK_ACCOUNT_TRANSFER_RETURN";
        public static final String BANK_ACCOUNT_TRANSFER_RETURN_FEE = "BANK_ACCOUNT_TRANSFER_RETURN_FEE";
        public static final String TRANSFER_TO_BANK_ACCOUNT = "TRANSFER_TO_BANK_ACCOUNT";
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
        public static final String DONATION = "DONATION";
        public static final String DONATION_FEE = "DONATION_FEE";
        public static final String DONATION_RETURN = "DONATION_RETURN";
        public static final String MERCHANT_PAYMENT = "MERCHANT_PAYMENT";
        public static final String MERCHANT_PAYMENT_FEE = "MERCHANT_PAYMENT_FEE";
        public static final String MERCHANT_PAYMENT_REFUND = "MERCHANT_PAYMENT_REFUND";
        public static final String MERCHANT_PAYMENT_RETURN = "MERCHANT_PAYMENT_RETURN";
        public static final String MONEYGRAM_TRANSFER_RETURN = "MONEYGRAM_TRANSFER_RETURN";
        public static final String TRANSFER_TO_MONEYGRAM = "TRANSFER_TO_MONEYGRAM";
        public static final String PAPER_CHECK_FEE = "PAPER_CHECK_FEE";
        public static final String PAPER_CHECK_REFUND = "PAPER_CHECK_REFUND";
        public static final String TRANSFER_TO_PAPER_CHECK = "TRANSFER_TO_PAPER_CHECK";
        public static final String ACCOUNT_CLOSURE = "ACCOUNT_CLOSURE";
        public static final String ACCOUNT_CLOSURE_FEE = "ACCOUNT_CLOSURE_FEE";
        public static final String ACCOUNT_UNLOAD = "ACCOUNT_UNLOAD";
        public static final String DORMANT_USER_FEE = "DORMANT_USER_FEE";
        public static final String DORMANT_USER_FEE_REFUND = "DORMANT_USER_FEE_REFUND";
        public static final String PAYMENT = "PAYMENT";
        public static final String PAYMENT_CANCELLATION = "PAYMENT_CANCELLATION";
        public static final String PAYMENT_REVERSAL = "PAYMENT_REVERSAL";
        public static final String PAYMENT_REVERSAL_FEE = "PAYMENT_REVERSAL_FEE";
        public static final String PAYMENT_RETURN = "PAYMENT_RETURN";
        public static final String TRANSFER_TO_PROGRAM_ACCOUNT = "TRANSFER_TO_PROGRAM_ACCOUNT";
        public static final String TRANSFER_TO_USER = "TRANSFER_TO_USER";
        public static final String VIRTUAL_INCENTIVE_CANCELLATION = "VIRTUAL_INCENTIVE_CANCELLATION";
        public static final String VIRTUAL_INCENTIVE_ISSUANCE = "VIRTUAL_INCENTIVE_ISSUANCE";
        public static final String VIRTUAL_INCENTIVE_PURCHASE = "VIRTUAL_INCENTIVE_PURCHASE";
        public static final String VIRTUAL_INCENTIVE_REFUND = "VIRTUAL_INCENTIVE_REFUND";
        public static final String TRANSFER_TO_WESTERN_UNION = "TRANSFER_TO_WESTERN_UNION";
        public static final String TRANSFER_TO_WUBS_WIRE = "TRANSFER_TO_WUBS_WIRE";
        public static final String WESTERN_UNION_TRANSFER_RETURN = "WESTERN_UNION_TRANSFER_RETURN";
        public static final String WUBS_WIRE_TRANSFER_RETURN = "WUBS_WIRE_TRANSFER_RETURN";
        public static final String TRANSFER_TO_WIRE = "TRANSFER_TO_WIRE";
        public static final String WIRE_TRANSFER_FEE = "WIRE_TRANSFER_FEE";
        public static final String WIRE_TRANSFER_RETURN = "WIRE_TRANSFER_RETURN";
    }

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            ANNUAL_FEE,
            ANNUAL_FEE_REFUND,
            CUSTOMER_SERVICE_FEE,
            CUSTOMER_SERVICE_FEE_REFUND,
            EXPEDITED_SHIPPING_FEE,
            GENERIC_FEE_REFUND,
            MONTHLY_FEE,
            MONTHLY_FEE_REFUND,
            PAYMENT_EXPIRY_FEE,
            PAYMENT_FEE,
            PROCESSING_FEE,
            STANDARD_SHIPPING_FEE,
            TRANSFER_FEE,
            ADJUSTMENT,
            FOREIGN_EXCHANGE,
            DEPOSIT,
            MANUAL_ADJUSTMENT,
            PAYMENT_EXPIRATION,
            BANK_ACCOUNT_TRANSFER_FEE,
            BANK_ACCOUNT_TRANSFER_RETURN,
            BANK_ACCOUNT_TRANSFER_RETURN_FEE,
            TRANSFER_TO_BANK_ACCOUNT,
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
            TRANSFER_TO_PREPAID_CARD,
            DONATION,
            DONATION_FEE,
            DONATION_RETURN,
            MERCHANT_PAYMENT,
            MERCHANT_PAYMENT_FEE,
            MERCHANT_PAYMENT_REFUND,
            MERCHANT_PAYMENT_RETURN,
            MONEYGRAM_TRANSFER_RETURN,
            TRANSFER_TO_MONEYGRAM,
            PAPER_CHECK_FEE,
            PAPER_CHECK_REFUND,
            TRANSFER_TO_PAPER_CHECK,
            ACCOUNT_CLOSURE,
            ACCOUNT_CLOSURE_FEE,
            ACCOUNT_UNLOAD,
            DORMANT_USER_FEE,
            DORMANT_USER_FEE_REFUND,
            PAYMENT,
            PAYMENT_CANCELLATION,
            PAYMENT_REVERSAL,
            PAYMENT_REVERSAL_FEE,
            PAYMENT_RETURN,
            TRANSFER_TO_PROGRAM_ACCOUNT,
            TRANSFER_TO_USER,
            VIRTUAL_INCENTIVE_CANCELLATION,
            VIRTUAL_INCENTIVE_ISSUANCE,
            VIRTUAL_INCENTIVE_PURCHASE,
            VIRTUAL_INCENTIVE_REFUND,
            TRANSFER_TO_WESTERN_UNION,
            TRANSFER_TO_WUBS_WIRE,
            WESTERN_UNION_TRANSFER_RETURN,
            WUBS_WIRE_TRANSFER_RETURN,
            TRANSFER_TO_WIRE,
            WIRE_TRANSFER_FEE,
            WIRE_TRANSFER_RETURN,
    })
    public @interface ReceiptType {
    }

    /**
     * Common Receipt field keys
     */
    public static final class ReceiptFields {
        private ReceiptFields() {
        }

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
     * Please use {@code getFieldValueToString(@NonNull String key, @NonNull Class<T> clazz)} if the value is not a {@link
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Receipt)) return false;
        Receipt that = (Receipt) o;
        return Objects.equals(getJournalId(), that.getJournalId())
                && Objects.equals(getType(), that.getType())
                && Objects.equals(getEntry(), that.getEntry());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getJournalId(), getType(), getEntry());
    }
}
