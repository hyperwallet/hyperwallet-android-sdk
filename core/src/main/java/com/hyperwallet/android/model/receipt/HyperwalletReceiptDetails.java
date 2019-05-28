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

public final class HyperwalletReceiptDetails implements HyperwalletJsonModel, Parcelable {

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
            ReceiptDetailsFields.BANK_ACCOUNT_ID,
            ReceiptDetailsFields.BANK_ACCOUNT_PURPOSE,
            ReceiptDetailsFields.BANK_ID,
            ReceiptDetailsFields.BANK_NAME,
            ReceiptDetailsFields.BRANCH_ADDRESS_LINE_1,
            ReceiptDetailsFields.BRANCH_ADDRESS_LINE_2,
            ReceiptDetailsFields.BRANCH_CITY,
            ReceiptDetailsFields.BRANCH_COUNTRY,
            ReceiptDetailsFields.BRANCH_ID,
            ReceiptDetailsFields.BRANCH_NAME,
            ReceiptDetailsFields.BRANCH_POSTAL_CODE,
            ReceiptDetailsFields.BRANCH_STATE_PROVINCE,
            ReceiptDetailsFields.CARD_NUMBER,
            ReceiptDetailsFields.CARD_EXPIRY_DATE,
            ReceiptDetailsFields.CARD_HOLDER_NAME,
            ReceiptDetailsFields.CHARITY_NAME,
            ReceiptDetailsFields.CHECK_NUMBER,
            ReceiptDetailsFields.CLIENT_PAYMENT_ID,
            ReceiptDetailsFields.MEMO,
            ReceiptDetailsFields.NOTES,
            ReceiptDetailsFields.PAYEE_ADDRESS_LINE_1,
            ReceiptDetailsFields.PAYEE_ADDRESS_LINE_2,
            ReceiptDetailsFields.PAYEE_CITY,
            ReceiptDetailsFields.PAYEE_COUNTRY,
            ReceiptDetailsFields.PAYEE_EMAIL,
            ReceiptDetailsFields.PAYEE_NAME,
            ReceiptDetailsFields.PAYEE_POSTAL_CODE,
            ReceiptDetailsFields.PAYEE_STATE_PROVINCE,
            ReceiptDetailsFields.PAYER_NAME,
            ReceiptDetailsFields.PAYMENT_EXPIRY_DATE,
            ReceiptDetailsFields.RETURN_OR_RECALL_REASON,
            ReceiptDetailsFields.SECURITY_ANSWER,
            ReceiptDetailsFields.SECURITY_QUESTION,
            ReceiptDetailsFields.WEBSITE
    })
    public @interface ReceiptDetailsField {
    }

    private Map<String, Object> mFields;

    public HyperwalletReceiptDetails(@NonNull JSONObject jsonObject) throws JSONException {
        toMap(jsonObject);
    }

    private HyperwalletReceiptDetails(@NonNull Map<String, Object> fields) {
        super();
        setFields(fields);
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
    public String getField(@NonNull @HyperwalletReceiptDetails.ReceiptDetailsField String key) {
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

    public static final Creator<HyperwalletReceiptDetails> CREATOR =
            new Creator<HyperwalletReceiptDetails>() {
                @Override
                public HyperwalletReceiptDetails createFromParcel(Parcel source) {
                    final Map<String, Object> fields = new HashMap<>();
                    source.readMap(fields, this.getClass().getClassLoader());
                    return new HyperwalletReceiptDetails(fields);
                }

                @Override
                public HyperwalletReceiptDetails[] newArray(int size) {
                    return new HyperwalletReceiptDetails[0];
                }
            };

    protected void setFields(@NonNull Map<String, Object> fields) {
        mFields = fields;
    }

}
