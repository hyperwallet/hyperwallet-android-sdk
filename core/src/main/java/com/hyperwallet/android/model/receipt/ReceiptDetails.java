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
 * Represents the receipt detail mFields
 */
public final class ReceiptDetails implements HyperwalletJsonModel, Parcelable {

    /**
     * Common Receipt detail field keys
     */
    public interface ReceiptDetailsFields {

        String BANK_ACCOUNT_ID = "bankAccountId";
        String BANK_ACCOUNT_PURPOSE = "bankAccountPurpose";
        String BANK_ID = "bankId";
        String BANK_NAME = "bankName";
        String BRANCH_ADDRESS_LINE_1 = "branchAddressLine1";
        String BRANCH_ADDRESS_LINE_2 = "branchAddressLine2";
        String BRANCH_CITY = "branchCity";
        String BRANCH_COUNTRY = "branchCountry";
        String BRANCH_ID = "branchId";
        String BRANCH_NAME = "branchName";
        String BRANCH_POSTAL_CODE = "branchPostalCode";
        String BRANCH_STATE_PROVINCE = "branchStateProvince";
        String CARD_NUMBER = "cardNumber";
        String CARD_EXPIRY_DATE = "cardExpiryDate";
        String CARD_HOLDER_NAME = "cardHolderName";
        String CHARITY_NAME = "charityName";
        String CHECK_NUMBER = "checkNumber";
        String CLIENT_PAYMENT_ID = "clientPaymentId";
        String MEMO = "memo";
        String NOTES = "notes";
        String PAYEE_ADDRESS_LINE_1 = "payeeAddressLine1";
        String PAYEE_ADDRESS_LINE_2 = "payeeAddressLine2";
        String PAYEE_CITY = "payeeCity";
        String PAYEE_COUNTRY = "payeeCountry";
        String PAYEE_EMAIL = "payeeEmail";
        String PAYEE_NAME = "payeeName";
        String PAYEE_POSTAL_CODE = "payeePostalCode";
        String PAYEE_STATE_PROVINCE = "payeeStateProvince";
        String PAYER_NAME = "payerName";
        String PAYMENT_EXPIRY_DATE = "paymentExpiryDate";
        String RETURN_OR_RECALL_REASON = "returnOrRecallReason";
        String SECURITY_ANSWER = "securityAnswer";
        String SECURITY_QUESTION = "securityQuestion";
        String WEBSITE = "website";
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
        super();
        setFields(fields);
    }

    @Nullable
    public String getBankAccountId() {
        return (String) mFields.get(BANK_ACCOUNT_ID);
    }

    @Nullable
    public String getBankAccountPurpose() {
        return (String) mFields.get(BANK_ACCOUNT_PURPOSE);
    }

    @Nullable
    public String getBankId() {
        return (String) mFields.get(BANK_ID);
    }

    @Nullable
    public String getBankName() {
        return (String) mFields.get(BANK_NAME);
    }

    @Nullable
    public String getBranchAddressLine1() {
        return (String) mFields.get(BRANCH_ADDRESS_LINE_1);
    }

    @Nullable
    public String getBranchAddressLine2() {
        return (String) mFields.get(BRANCH_ADDRESS_LINE_2);
    }

    @Nullable
    public String getBranchCity() {
        return (String) mFields.get(BRANCH_CITY);
    }

    @Nullable
    public String getBranchCountry() {
        return (String) mFields.get(BRANCH_COUNTRY);
    }

    @Nullable
    public String getBranchId() {
        return (String) mFields.get(BRANCH_ID);
    }

    @Nullable
    public String getBranchName() {
        return (String) mFields.get(BRANCH_NAME);
    }

    @Nullable
    public String getBranchPostalCode() {
        return (String) mFields.get(BRANCH_POSTAL_CODE);
    }

    @Nullable
    public String getBranchStateProvince() {
        return (String) mFields.get(BRANCH_STATE_PROVINCE);
    }

    @Nullable
    public String getCardNumber() {
        return (String) mFields.get(CARD_NUMBER);
    }

    @Nullable
    public String getCardExpiryDate() {
        return (String) mFields.get(CARD_EXPIRY_DATE);
    }

    @Nullable
    public String getCardHolderName() {
        return (String) mFields.get(CARD_HOLDER_NAME);
    }

    @Nullable
    public String getCharityName() {
        return (String) mFields.get(CHARITY_NAME);
    }

    @Nullable
    public String getCheckNumber() {
        return (String) mFields.get(CHECK_NUMBER);
    }

    @Nullable
    public String getClientPaymentId() {
        return (String) mFields.get(CLIENT_PAYMENT_ID);
    }

    @Nullable
    public String getMemo() {
        return (String) mFields.get(MEMO);
    }

    @Nullable
    public String getNotes() {
        return (String) mFields.get(NOTES);
    }

    @Nullable
    public String getPayeeAddressLine1() {
        return (String) mFields.get(PAYEE_ADDRESS_LINE_1);
    }

    @Nullable
    public String getPayeeAddressLine2() {
        return (String) mFields.get(PAYEE_ADDRESS_LINE_2);
    }

    @Nullable
    public String getPayeeCity() {
        return (String) mFields.get(PAYEE_CITY);
    }

    @Nullable
    public String getPayeeCountry() {
        return (String) mFields.get(PAYEE_COUNTRY);
    }

    @Nullable
    public String getPayeeEmail() {
        return (String) mFields.get(PAYEE_EMAIL);
    }

    @Nullable
    public String getPayeeName() {
        return (String) mFields.get(PAYEE_NAME);
    }

    @Nullable
    public String getPayeePostalCode() {
        return (String) mFields.get(PAYEE_POSTAL_CODE);
    }

    @Nullable
    public String getPayeeStateProvince() {
        return (String) mFields.get(PAYEE_STATE_PROVINCE);
    }

    @Nullable
    public String getPayerName() {
        return (String) mFields.get(PAYER_NAME);
    }

    @Nullable
    public String getPaymentExpiryDate() {
        return (String) mFields.get(PAYMENT_EXPIRY_DATE);
    }

    @Nullable
    public String getReturnOrRecallReason() {
        return (String) mFields.get(RETURN_OR_RECALL_REASON);
    }

    @Nullable
    public String getSecurityAnswer() {
        return (String) mFields.get(SECURITY_ANSWER);
    }

    @Nullable
    public String getSecurityQuestion() {
        return (String) mFields.get(SECURITY_QUESTION);
    }

    @Nullable
    public String getWebsite() {
        return (String) mFields.get(WEBSITE);
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
        mFields = fields;
    }


    public static class Builder {
        private Map<String, Object> mFields;

        public Builder() {
            mFields = new HashMap<>();
        }

        public ReceiptDetails.Builder bankAccountId(@NonNull final String bankAccountId) {
            mFields.put(BANK_ACCOUNT_ID, bankAccountId);
            return this;
        }

        public ReceiptDetails.Builder bankAccountPurpose(@NonNull final String bankAccountPurpose) {
            mFields.put(BANK_ACCOUNT_PURPOSE, bankAccountPurpose);
            return this;
        }

        public ReceiptDetails.Builder bankId(@NonNull final String bankId) {
            mFields.put(BANK_ID, bankId);
            return this;
        }

        public ReceiptDetails.Builder bankName(@NonNull final String bankName) {
            mFields.put(BANK_NAME, bankName);
            return this;
        }

        public ReceiptDetails.Builder branchAddressLine1(@NonNull final String branchAddressLine1) {
            mFields.put(BRANCH_ADDRESS_LINE_1, branchAddressLine1);
            return this;
        }

        public ReceiptDetails.Builder branchAddressLine2(@NonNull final String branchAddressLine2) {
            mFields.put(BRANCH_ADDRESS_LINE_2, branchAddressLine2);
            return this;
        }

        public ReceiptDetails.Builder branchCity(@NonNull final String branchCity) {
            mFields.put(BRANCH_CITY, branchCity);
            return this;
        }

        public ReceiptDetails.Builder branchCountry(@NonNull final String branchCountry) {
            mFields.put(BRANCH_COUNTRY, branchCountry);
            return this;
        }

        public ReceiptDetails.Builder branchId(@NonNull final String branchId) {
            mFields.put(BRANCH_ID, branchId);
            return this;
        }

        public ReceiptDetails.Builder branchName(@NonNull final String branchName) {
            mFields.put(BRANCH_NAME, branchName);
            return this;
        }

        public ReceiptDetails.Builder branchPostalCode(@NonNull final String branchPostalCode) {
            mFields.put(BRANCH_POSTAL_CODE, branchPostalCode);
            return this;
        }

        public ReceiptDetails.Builder branchStateProvince(@NonNull final String branchStateProvince) {
            mFields.put(BRANCH_STATE_PROVINCE, branchStateProvince);
            return this;
        }

        public ReceiptDetails.Builder cardNumber(@NonNull final String cardNumber) {
            mFields.put(CARD_NUMBER, cardNumber);
            return this;
        }

        public ReceiptDetails.Builder cardExpiryDate(@NonNull final String cardExpiryDate) {
            mFields.put(CARD_EXPIRY_DATE, cardExpiryDate);
            return this;
        }

        public ReceiptDetails.Builder cardHolderName(@NonNull final String cardHolderName) {
            mFields.put(CARD_HOLDER_NAME, cardHolderName);
            return this;
        }

        public ReceiptDetails.Builder charityName(@NonNull final String charityName) {
            mFields.put(CHARITY_NAME, charityName);
            return this;
        }

        public ReceiptDetails.Builder checkNumber(@NonNull final String checkNumber) {
            mFields.put(CHECK_NUMBER, checkNumber);
            return this;
        }

        public ReceiptDetails.Builder clientPaymentId(@NonNull final String clientPaymentId) {
            mFields.put(CLIENT_PAYMENT_ID, clientPaymentId);
            return this;
        }

        public ReceiptDetails.Builder memo(@NonNull final String memo) {
            mFields.put(MEMO, memo);
            return this;
        }

        public ReceiptDetails.Builder notes(@NonNull final String notes) {
            mFields.put(NOTES, notes);
            return this;
        }

        public ReceiptDetails.Builder payeeAddressLine1(@NonNull final String payeeAddressLine1) {
            mFields.put(PAYEE_ADDRESS_LINE_1, payeeAddressLine1);
            return this;
        }

        public ReceiptDetails.Builder payeeAddressLine2(@NonNull final String payeeAddressLine2) {
            mFields.put(PAYEE_ADDRESS_LINE_2, payeeAddressLine2);
            return this;
        }

        public ReceiptDetails.Builder payeeCity(@NonNull final String payeeCity) {
            mFields.put(PAYEE_CITY, payeeCity);
            return this;
        }

        public ReceiptDetails.Builder payeeCountry(@NonNull final String payeeCountry) {
            mFields.put(PAYEE_COUNTRY, payeeCountry);
            return this;
        }

        public ReceiptDetails.Builder payeeEmail(@NonNull final String payeeEmail) {
            mFields.put(PAYEE_EMAIL, payeeEmail);
            return this;
        }

        public ReceiptDetails.Builder payeeName(@NonNull final String payeeName) {
            mFields.put(PAYEE_NAME, payeeName);
            return this;
        }

        public ReceiptDetails.Builder payeePostalCode(@NonNull final String payeePostalCode) {
            mFields.put(PAYEE_POSTAL_CODE, payeePostalCode);
            return this;
        }

        public ReceiptDetails.Builder payeeStateProvince(@NonNull final String payeeStateProvince) {
            mFields.put(PAYEE_STATE_PROVINCE, payeeStateProvince);
            return this;
        }

        public ReceiptDetails.Builder payerName(@NonNull final String payerName) {
            mFields.put(PAYER_NAME, payerName);
            return this;
        }

        public ReceiptDetails.Builder paymentExpiryDate(@NonNull final String paymentExpiryDate) {
            mFields.put(PAYMENT_EXPIRY_DATE, paymentExpiryDate);
            return this;
        }

        public ReceiptDetails.Builder returnOrRecallReason(@NonNull final String returnOrRecallReason) {
            mFields.put(RETURN_OR_RECALL_REASON, returnOrRecallReason);
            return this;
        }

        public ReceiptDetails.Builder securityAnswer(@NonNull final String securityAnswer) {
            mFields.put(SECURITY_ANSWER, securityAnswer);
            return this;
        }

        public ReceiptDetails.Builder securityQuestion(@NonNull final String securityQuestion) {
            mFields.put(SECURITY_QUESTION, securityQuestion);
            return this;
        }

        public ReceiptDetails.Builder website(@NonNull final String website) {
            mFields.put(WEBSITE, website);
            return this;
        }

        public ReceiptDetails build() {
            return new ReceiptDetails(mFields);
        }
    }
}
