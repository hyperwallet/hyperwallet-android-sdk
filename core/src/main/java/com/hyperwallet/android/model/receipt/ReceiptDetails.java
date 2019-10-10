/*
 * Copyright 2018 Hyperwallet
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
 * to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of
 * the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
 * THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 */

package com.hyperwallet.android.model.receipt;

import static com.hyperwallet.android.model.receipt.ReceiptDetails.ReceiptDetailsFields.BANK_ACCOUNT_ID;
import static com.hyperwallet.android.model.receipt.ReceiptDetails.ReceiptDetailsFields.BANK_ACCOUNT_PURPOSE;
import static com.hyperwallet.android.model.receipt.ReceiptDetails.ReceiptDetailsFields.BANK_ID;
import static com.hyperwallet.android.model.receipt.ReceiptDetails.ReceiptDetailsFields.BANK_NAME;
import static com.hyperwallet.android.model.receipt.ReceiptDetails.ReceiptDetailsFields.BRANCH_ADDRESS_LINE_1;
import static com.hyperwallet.android.model.receipt.ReceiptDetails.ReceiptDetailsFields.BRANCH_ADDRESS_LINE_2;
import static com.hyperwallet.android.model.receipt.ReceiptDetails.ReceiptDetailsFields.BRANCH_CITY;
import static com.hyperwallet.android.model.receipt.ReceiptDetails.ReceiptDetailsFields.BRANCH_COUNTRY;
import static com.hyperwallet.android.model.receipt.ReceiptDetails.ReceiptDetailsFields.BRANCH_ID;
import static com.hyperwallet.android.model.receipt.ReceiptDetails.ReceiptDetailsFields.BRANCH_NAME;
import static com.hyperwallet.android.model.receipt.ReceiptDetails.ReceiptDetailsFields.BRANCH_POSTAL_CODE;
import static com.hyperwallet.android.model.receipt.ReceiptDetails.ReceiptDetailsFields.BRANCH_STATE_PROVINCE;
import static com.hyperwallet.android.model.receipt.ReceiptDetails.ReceiptDetailsFields.CARD_EXPIRY_DATE;
import static com.hyperwallet.android.model.receipt.ReceiptDetails.ReceiptDetailsFields.CARD_HOLDER_NAME;
import static com.hyperwallet.android.model.receipt.ReceiptDetails.ReceiptDetailsFields.CARD_NUMBER;
import static com.hyperwallet.android.model.receipt.ReceiptDetails.ReceiptDetailsFields.CHARITY_NAME;
import static com.hyperwallet.android.model.receipt.ReceiptDetails.ReceiptDetailsFields.CHECK_NUMBER;
import static com.hyperwallet.android.model.receipt.ReceiptDetails.ReceiptDetailsFields.CLIENT_PAYMENT_ID;
import static com.hyperwallet.android.model.receipt.ReceiptDetails.ReceiptDetailsFields.MEMO;
import static com.hyperwallet.android.model.receipt.ReceiptDetails.ReceiptDetailsFields.NOTES;
import static com.hyperwallet.android.model.receipt.ReceiptDetails.ReceiptDetailsFields.PAYEE_ADDRESS_LINE_1;
import static com.hyperwallet.android.model.receipt.ReceiptDetails.ReceiptDetailsFields.PAYEE_ADDRESS_LINE_2;
import static com.hyperwallet.android.model.receipt.ReceiptDetails.ReceiptDetailsFields.PAYEE_CITY;
import static com.hyperwallet.android.model.receipt.ReceiptDetails.ReceiptDetailsFields.PAYEE_COUNTRY;
import static com.hyperwallet.android.model.receipt.ReceiptDetails.ReceiptDetailsFields.PAYEE_EMAIL;
import static com.hyperwallet.android.model.receipt.ReceiptDetails.ReceiptDetailsFields.PAYEE_NAME;
import static com.hyperwallet.android.model.receipt.ReceiptDetails.ReceiptDetailsFields.PAYEE_POSTAL_CODE;
import static com.hyperwallet.android.model.receipt.ReceiptDetails.ReceiptDetailsFields.PAYEE_STATE_PROVINCE;
import static com.hyperwallet.android.model.receipt.ReceiptDetails.ReceiptDetailsFields.PAYER_NAME;
import static com.hyperwallet.android.model.receipt.ReceiptDetails.ReceiptDetailsFields.PAYMENT_EXPIRY_DATE;
import static com.hyperwallet.android.model.receipt.ReceiptDetails.ReceiptDetailsFields.RETURN_OR_RECALL_REASON;
import static com.hyperwallet.android.model.receipt.ReceiptDetails.ReceiptDetailsFields.SECURITY_ANSWER;
import static com.hyperwallet.android.model.receipt.ReceiptDetails.ReceiptDetailsFields.SECURITY_QUESTION;
import static com.hyperwallet.android.model.receipt.ReceiptDetails.ReceiptDetailsFields.WEBSITE;

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
 * Represents the receipt detail fields
 */
public final class ReceiptDetails implements HyperwalletJsonModel, Parcelable {

    /**
     * Common Receipt detail field keys
     */
    public static final class ReceiptDetailsFields {

        private ReceiptDetailsFields() {
        }

        public static final String BANK_ACCOUNT_ID = "bankAccountId";
        public static final String BANK_ACCOUNT_PURPOSE = "bankAccountPurpose";
        public static final String BANK_ID = "bankId";
        public static final String BANK_NAME = "bankName";
        public static final String BRANCH_ADDRESS_LINE_1 = "branchAddressLine1";
        public static final String BRANCH_ADDRESS_LINE_2 = "branchAddressLine2";
        public static final String BRANCH_CITY = "branchCity";
        public static final String BRANCH_COUNTRY = "branchCountry";
        public static final String BRANCH_ID = "branchId";
        public static final String BRANCH_NAME = "branchName";
        public static final String BRANCH_POSTAL_CODE = "branchPostalCode";
        public static final String BRANCH_STATE_PROVINCE = "branchStateProvince";
        public static final String CARD_NUMBER = "cardNumber";
        public static final String CARD_EXPIRY_DATE = "cardExpiryDate";
        public static final String CARD_HOLDER_NAME = "cardHolderName";
        public static final String CHARITY_NAME = "charityName";
        public static final String CHECK_NUMBER = "checkNumber";
        public static final String CLIENT_PAYMENT_ID = "clientPaymentId";
        public static final String MEMO = "memo";
        public static final String NOTES = "notes";
        public static final String PAYEE_ADDRESS_LINE_1 = "payeeAddressLine1";
        public static final String PAYEE_ADDRESS_LINE_2 = "payeeAddressLine2";
        public static final String PAYEE_CITY = "payeeCity";
        public static final String PAYEE_COUNTRY = "payeeCountry";
        public static final String PAYEE_EMAIL = "payeeEmail";
        public static final String PAYEE_NAME = "payeeName";
        public static final String PAYEE_POSTAL_CODE = "payeePostalCode";
        public static final String PAYEE_STATE_PROVINCE = "payeeStateProvince";
        public static final String PAYER_NAME = "payerName";
        public static final String PAYMENT_EXPIRY_DATE = "paymentExpiryDate";
        public static final String RETURN_OR_RECALL_REASON = "returnOrRecallReason";
        public static final String SECURITY_ANSWER = "securityAnswer";
        public static final String SECURITY_QUESTION = "securityQuestion";
        public static final String WEBSITE = "website";
    }

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            BANK_ACCOUNT_ID,
            BANK_ACCOUNT_PURPOSE,
            BANK_ID,
            BANK_NAME,
            BRANCH_ADDRESS_LINE_1,
            BRANCH_ADDRESS_LINE_2,
            BRANCH_CITY,
            BRANCH_COUNTRY,
            BRANCH_ID,
            BRANCH_NAME,
            BRANCH_POSTAL_CODE,
            BRANCH_STATE_PROVINCE,
            CARD_NUMBER,
            CARD_EXPIRY_DATE,
            CARD_HOLDER_NAME,
            CHARITY_NAME,
            CHECK_NUMBER,
            CLIENT_PAYMENT_ID,
            MEMO,
            NOTES,
            PAYEE_ADDRESS_LINE_1,
            PAYEE_ADDRESS_LINE_2,
            PAYEE_CITY,
            PAYEE_COUNTRY,
            PAYEE_EMAIL,
            PAYEE_NAME,
            PAYEE_POSTAL_CODE,
            PAYEE_STATE_PROVINCE,
            PAYER_NAME,
            PAYMENT_EXPIRY_DATE,
            RETURN_OR_RECALL_REASON,
            SECURITY_ANSWER,
            SECURITY_QUESTION,
            WEBSITE
    })
    public @interface ReceiptDetailsField {
    }

    private Map<String, Object> mFields;

    public ReceiptDetails(@NonNull JSONObject jsonObject) throws JSONException {
        toMap(jsonObject);
    }

    ReceiptDetails(@NonNull Map<String, Object> fields) {
        setFields(fields);
    }

    @Nullable
    public String getBankAccountId() {
        return getField(BANK_ACCOUNT_ID);
    }

    @Nullable
    public String getBankAccountPurpose() {
        return getField(BANK_ACCOUNT_PURPOSE);
    }

    @Nullable
    public String getBankId() {
        return getField(BANK_ID);
    }

    @Nullable
    public String getBankName() {
        return getField(BANK_NAME);
    }

    @Nullable
    public String getBranchAddressLine1() {
        return getField(BRANCH_ADDRESS_LINE_1);
    }

    @Nullable
    public String getBranchAddressLine2() {
        return getField(BRANCH_ADDRESS_LINE_2);
    }

    @Nullable
    public String getBranchCity() {
        return getField(BRANCH_CITY);
    }

    @Nullable
    public String getBranchCountry() {
        return getField(BRANCH_COUNTRY);
    }

    @Nullable
    public String getBranchId() {
        return getField(BRANCH_ID);
    }

    @Nullable
    public String getBranchName() {
        return getField(BRANCH_NAME);
    }

    @Nullable
    public String getBranchPostalCode() {
        return getField(BRANCH_POSTAL_CODE);
    }

    @Nullable
    public String getBranchStateProvince() {
        return getField(BRANCH_STATE_PROVINCE);
    }

    @Nullable
    public String getCardNumber() {
        return getField(CARD_NUMBER);
    }

    @Nullable
    public String getCardExpiryDate() {
        return getField(CARD_EXPIRY_DATE);
    }

    @Nullable
    public String getCardHolderName() {
        return getField(CARD_HOLDER_NAME);
    }

    @Nullable
    public String getCharityName() {
        return getField(CHARITY_NAME);
    }

    @Nullable
    public String getCheckNumber() {
        return getField(CHECK_NUMBER);
    }

    @Nullable
    public String getClientPaymentId() {
        return getField(CLIENT_PAYMENT_ID);
    }

    @Nullable
    public String getMemo() {
        return (String) getField(MEMO);
    }

    @Nullable
    public String getNotes() {
        return (String) getField(NOTES);
    }

    @Nullable
    public String getPayeeAddressLine1() {
        return getField(PAYEE_ADDRESS_LINE_1);
    }

    @Nullable
    public String getPayeeAddressLine2() {
        return getField(PAYEE_ADDRESS_LINE_2);
    }

    @Nullable
    public String getPayeeCity() {
        return getField(PAYEE_CITY);
    }

    @Nullable
    public String getPayeeCountry() {
        return getField(PAYEE_COUNTRY);
    }

    @Nullable
    public String getPayeeEmail() {
        return getField(PAYEE_EMAIL);
    }

    @Nullable
    public String getPayeeName() {
        return getField(PAYEE_NAME);
    }

    @Nullable
    public String getPayeePostalCode() {
        return getField(PAYEE_POSTAL_CODE);
    }

    @Nullable
    public String getPayeeStateProvince() {
        return getField(PAYEE_STATE_PROVINCE);
    }

    @Nullable
    public String getPayerName() {
        return getField(PAYER_NAME);
    }

    @Nullable
    public String getPaymentExpiryDate() {
        return getField(PAYMENT_EXPIRY_DATE);
    }

    @Nullable
    public String getReturnOrRecallReason() {
        return getField(RETURN_OR_RECALL_REASON);
    }

    @Nullable
    public String getSecurityAnswer() {
        return getField(SECURITY_ANSWER);
    }

    @Nullable
    public String getSecurityQuestion() {
        return getField(SECURITY_QUESTION);
    }

    @Nullable
    public String getWebsite() {
        return getField(WEBSITE);
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
     * Please use {@code getFieldValueToString(@NonNull String key, @NonNull Class<T> clazz)} if the value is not a
     * {@link String}
     * </p>
     *
     * @param key can only be a {@link String} that represents a {@link ReceiptDetailsField}
     *            name
     * @return a {@link String} value that represents the value of a {@link ReceiptDetailsField}
     */
    @Nullable
    public String getField(@NonNull @ReceiptDetails.ReceiptDetailsField String key) {
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

    public static final Creator<ReceiptDetails> CREATOR =
            new Creator<ReceiptDetails>() {
                @Override
                public ReceiptDetails createFromParcel(Parcel source) {
                    final Map<String, Object> fields = new HashMap<>();
                    source.readMap(fields, this.getClass().getClassLoader());
                    return new ReceiptDetails(fields);
                }

                @Override
                public ReceiptDetails[] newArray(int size) {
                    return new ReceiptDetails[0];
                }
            };

    protected void setFields(@NonNull Map<String, Object> fields) {
        mFields = new HashMap<>(fields);
    }

}
