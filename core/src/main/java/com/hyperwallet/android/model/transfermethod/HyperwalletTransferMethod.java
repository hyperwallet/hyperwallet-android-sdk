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

package com.hyperwallet.android.model.transfermethod;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringDef;

import com.hyperwallet.android.model.HyperwalletJsonModel;
import com.hyperwallet.android.model.graphql.field.HyperwalletField;
import com.hyperwallet.android.util.JsonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents the object to hold transfer method data fields
 */
public class HyperwalletTransferMethod implements HyperwalletJsonModel, Parcelable {

    public static final Creator<HyperwalletTransferMethod> CREATOR =
            new Creator<HyperwalletTransferMethod>() {
                @Override
                public HyperwalletTransferMethod createFromParcel(Parcel source) {
                    final Map<String, Object> fields = new HashMap<>();
                    source.readMap(fields, this.getClass().getClassLoader());
                    return new HyperwalletTransferMethod(fields);
                }

                @Override
                public HyperwalletTransferMethod[] newArray(int size) {
                    return new HyperwalletTransferMethod[0];
                }
            };
    private Map<String, Object> mFields;

    /**
     * Construct a {@code TransferMethod}
     */
    public HyperwalletTransferMethod() {
        mFields = new HashMap<>();
    }

    /**
     * Construct a {@code TransferMethod} object from {@link JSONObject} representation
     *
     * @param jsonObject raw data information
     */
    public HyperwalletTransferMethod(@NonNull final JSONObject jsonObject) throws JSONException {
        toMap(jsonObject);
    }

    /**
     * Construct a {@code TransferMethod} object from Map of key-value pair representation
     *
     * @param fields map of key value-pair raw data information
     */
    private HyperwalletTransferMethod(@NonNull final Map<String, Object> fields) {
        mFields = new HashMap<>(fields);
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
     * Convert a json String to a {@code Map<String, Object>}
     *
     * @param jsonObject is a response from Rest API in {@link String} format
     */
    private void toMap(@NonNull JSONObject jsonObject) throws JSONException {
        mFields = JsonUtils.jsonObjectToMap(jsonObject);
    }

    /**
     * Put a key-value pair into {@code mField}
     *
     * @param key   is a {@link String} that represents a {@link HyperwalletField} name
     * @param value is an {@link Object} that represent the value of a {@link HyperwalletField}
     */
    public void setField(@NonNull @TransferMethodFieldKey final String key, final Object value) {
        mFields.put(key, value);
    }

    /**
     * Returns a {@link String} value of a {@code Map<String, String>}
     * <p>
     * Please use {@code getFieldValueToString(@NonNull String key, @NonNull Class<T> clazz)} if the value is not a
     * {@link String}
     * </p>
     *
     * @param key can only be a {@link String} that represents a {@link HyperwalletField} name
     * @return a {@link String} value that represents the value of a {@link HyperwalletField}
     */
    @Nullable
    public String getField(@NonNull @TransferMethodFieldKey final String key) {
        return mFields.get(key) != null ? (String) mFields.get(key) : null;
    }

    /**
     * Returns a {@link Class<T>} value of a {@code Map<String, T>}
     *
     * @param key can only be {@link String}
     * @return a {@code T} value
     */
    @Nullable
    public <T> T getField(@NonNull @TransferMethodFieldKey final String key, @NonNull final Class<T> clazz) {
        return clazz.cast(mFields.get(key));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeMap(mFields);
    }

    protected void setFields(@NonNull final Map<String, Object> fields) {
        mFields = new HashMap<>(fields);
    }

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            TransferMethodTypes.BANK_ACCOUNT,
            TransferMethodTypes.BANK_CARD,
            TransferMethodTypes.WIRE_ACCOUNT,
            TransferMethodTypes.PREPAID_CARD,
            TransferMethodTypes.PAPER_CHECK,
            TransferMethodTypes.PAYPAL_ACCOUNT
    })
    public @interface TransferMethodType {
    }

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            TransferMethodFields.CREATED_ON,
            TransferMethodFields.IS_DEFAULT_TRANSFER_METHOD,
            TransferMethodFields.STATUS,
            TransferMethodFields.TOKEN,
            TransferMethodFields.TRANSFER_METHOD_COUNTRY,
            TransferMethodFields.TRANSFER_METHOD_CURRENCY,
            TransferMethodFields.TYPE,
            TransferMethodFields.ADDRESS_LINE_1,
            TransferMethodFields.ADDRESS_LINE_2,
            TransferMethodFields.CITY,
            TransferMethodFields.COUNTRY,
            TransferMethodFields.COUNTRY_OF_BIRTH,
            TransferMethodFields.COUNTRY_OF_NATIONALITY,
            TransferMethodFields.DATE_OF_BIRTH,
            TransferMethodFields.DRIVER_LICENSE_ID,
            TransferMethodFields.EMPLOYER_ID,
            TransferMethodFields.FIRST_NAME,
            TransferMethodFields.GENDER,
            TransferMethodFields.GOVERNMENT_ID,
            TransferMethodFields.GOVERNMENT_ID_TYPE,
            TransferMethodFields.LAST_NAME,
            TransferMethodFields.MIDDLE_NAME,
            TransferMethodFields.MOBILE_NUMBER,
            TransferMethodFields.PASSPORT_ID,
            TransferMethodFields.PHONE_NUMBER,
            TransferMethodFields.POSTAL_CODE,
            TransferMethodFields.PROFILE_TYPE,
            TransferMethodFields.STATE_PROVINCE,
            TransferMethodFields.BUSINESS_TYPE,
            TransferMethodFields.BUSINESS_NAME,
            TransferMethodFields.BUSINESS_REGISTRATION_ID,
            TransferMethodFields.BUSINESS_REGISTRATION_STATE_PROVINCE,
            TransferMethodFields.BUSINESS_REGISTRATION_COUNTRY,
            TransferMethodFields.BUSINESS_OPERATING_NAME,
            TransferMethodFields.BANK_ACCOUNT_ID,
            TransferMethodFields.BANK_ACCOUNT_PURPOSE,
            TransferMethodFields.BANK_ACCOUNT_RELATIONSHIP,
            TransferMethodFields.BANK_ID,
            TransferMethodFields.BANK_NAME,
            TransferMethodFields.BRANCH_ID,
            TransferMethodFields.BRANCH_NAME,
            TransferMethodFields.BRANCH_ADDRESS_LINE_1,
            TransferMethodFields.BRANCH_ADDRESS_LINE_2,
            TransferMethodFields.BRANCH_CITY,
            TransferMethodFields.BRANCH_COUNTRY,
            TransferMethodFields.BRANCH_POSTAL_CODE,
            TransferMethodFields.BRANCH_STATE_PROVINCE,
            TransferMethodFields.CARD_BRAND,
            TransferMethodFields.CARD_NUMBER,
            TransferMethodFields.CARD_TYPE,
            TransferMethodFields.DATE_OF_EXPIRY,
            TransferMethodFields.CVV,
            TransferMethodFields.CARD_PACKAGE,
            TransferMethodFields.EMAIL
    })
    public @interface TransferMethodFieldKey {
    }

    public final class TransferMethodTypes {
        public static final String BANK_ACCOUNT = "BANK_ACCOUNT";
        public static final String BANK_CARD = "BANK_CARD";
        public static final String PAPER_CHECK = "PAPER_CHECK";
        public static final String PREPAID_CARD = "PREPAID_CARD";
        public static final String WIRE_ACCOUNT = "WIRE_ACCOUNT";
        public static final String PAYPAL_ACCOUNT = "PAYPAL_ACCOUNT";
    }

    public final class TransferMethodFields {
        // common transfer method field keys
        public static final String CREATED_ON = "createdOn";
        public static final String IS_DEFAULT_TRANSFER_METHOD = "isDefaultTransferMethod";
        public static final String STATUS = "status";
        public static final String TOKEN = "token";
        public static final String TRANSFER_METHOD_COUNTRY = "transferMethodCountry";
        public static final String TRANSFER_METHOD_CURRENCY = "transferMethodCurrency";
        public static final String TYPE = "type";

        // bank account field keys
        public static final String BANK_ACCOUNT_ID = "bankAccountId";
        public static final String BANK_ACCOUNT_PURPOSE = "bankAccountPurpose";
        public static final String BANK_ACCOUNT_RELATIONSHIP = "bankAccountRelationship";
        public static final String BANK_ID = "bankId";
        public static final String BANK_NAME = "bankName";
        public static final String BRANCH_ID = "branchId";
        public static final String BRANCH_NAME = "branchName";
        public static final String BRANCH_ADDRESS_LINE_1 = "branchAddressLine1";
        public static final String BRANCH_ADDRESS_LINE_2 = "branchAddressLine2";
        public static final String BRANCH_CITY = "branchCity";
        public static final String BRANCH_COUNTRY = "branchCountry";
        public static final String BRANCH_POSTAL_CODE = "branchPostalCode";
        public static final String BRANCH_STATE_PROVINCE = "branchStateProvince";

        // bank card field keys
        public static final String CARD_BRAND = "cardBrand";
        public static final String CARD_NUMBER = "cardNumber";
        public static final String CARD_TYPE = "cardType";
        public static final String DATE_OF_EXPIRY = "dateOfExpiry";
        public static final String CVV = "cvv";
        public static final String CARD_PACKAGE = "cardPackage";

        // profile information
        public static final String PROFILE_TYPE = "profileType";
        public static final String BUSINESS_TYPE = "businessType";
        public static final String BUSINESS_NAME = "businessName";
        public static final String BUSINESS_REGISTRATION_ID = "businessRegistrationId";
        public static final String BUSINESS_REGISTRATION_STATE_PROVINCE = "businessRegistrationStateProvince";
        public static final String BUSINESS_REGISTRATION_COUNTRY = "businessRegistrationCountry";
        public static final String BUSINESS_OPERATING_NAME = "businessOperatingName";
        public static final String FIRST_NAME = "firstName";
        public static final String MIDDLE_NAME = "middleName";
        public static final String LAST_NAME = "lastName";
        public static final String DATE_OF_BIRTH = "dateOfBirth";
        public static final String COUNTRY_OF_BIRTH = "countryOfBirth";
        public static final String COUNTRY_OF_NATIONALITY = "countryOfNationality";
        public static final String GENDER = "gender";
        public static final String PHONE_NUMBER = "phoneNumber";
        public static final String MOBILE_NUMBER = "mobileNumber";
        public static final String EMAIL = "email";
        public static final String GOVERNMENT_ID = "governmentId";
        public static final String GOVERNMENT_ID_TYPE = "governmentIdType";
        public static final String PASSPORT_ID = "passportId";
        public static final String DRIVER_LICENSE_ID = "driversLicenseId";
        public static final String EMPLOYER_ID = "employerId";
        public static final String ADDRESS_LINE_1 = "addressLine1";
        public static final String ADDRESS_LINE_2 = "addressLine2";
        public static final String CITY = "city";
        public static final String STATE_PROVINCE = "stateProvince";
        public static final String COUNTRY = "country";
        public static final String POSTAL_CODE = "postalCode";
        //wire account fields
        public static final String INTERMEDIARY_BANK_ACCOUNT_ID = "intermediaryBankAccountId";
        public static final String INTERMEDIARY_BANK_ADDRESS_LINE_1 = "intermediaryBankAddressLine1";
        public static final String INTERMEDIARY_BANK_ADDRESS_LINE_2 = "intermediaryBankAddressLine2";
        public static final String INTERMEDIARY_BANK_CITY = "intermediaryBankCity";
        public static final String INTERMEDIARY_BANK_COUNTRY = "intermediaryBankCountry";
        public static final String INTERMEDIARY_BANK_ID = "intermediaryBankId";
        public static final String INTERMEDIARY_BANK_NAME = "intermediaryBankName";
        public static final String INTERMEDIARY_BANK_POSTAL_CODE = "intermediaryBankPostalCode";
        public static final String INTERMEDIARY_BANK_STATE_PROVINCE = "intermediaryBankStateProvince";
        public static final String WIRE_INSTRUCTIONS = "wireInstructions";
    }
}
