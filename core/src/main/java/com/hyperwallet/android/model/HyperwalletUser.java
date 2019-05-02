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
 * AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN
 * THE SOFTWARE.
 */

package com.hyperwallet.android.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringDef;

import com.hyperwallet.android.model.meta.HyperwalletField;
import com.hyperwallet.android.util.JsonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents the User fields.
 */
public final class HyperwalletUser implements HyperwalletJsonModel, Parcelable {

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            UserStatuses.PRE_ACTIVATED,
            UserStatuses.ACTIVATED,
            UserStatuses.LOCKED,
            UserStatuses.FROZEN,
            UserStatuses.DE_ACTIVATED
    })
    public @interface UserStatus {
    }

    public final class UserStatuses {
        public static final String PRE_ACTIVATED = "PRE_ACTIVATED";
        public static final String ACTIVATED = "ACTIVATED";
        public static final String LOCKED = "LOCKED";
        public static final String FROZEN = "FROZEN";
        public static final String DE_ACTIVATED = "DE_ACTIVATED";
    }

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            UserVerificationStatuses.NOT_REQUIRED,
            UserVerificationStatuses.REQUIRED,
            UserVerificationStatuses.FAILED,
            UserVerificationStatuses.UNDER_REVIEW,
            UserVerificationStatuses.VERIFIED
    })
    public @interface UserVerificationStatus {
    }


    public final class UserVerificationStatuses {
        public static final String NOT_REQUIRED = "NOT_REQUIRED";
        public static final String REQUIRED = "REQUIRED";
        public static final String FAILED = "FAILED";
        public static final String UNDER_REVIEW = "UNDER_REVIEW";
        public static final String VERIFIED = "VERIFIED";
    }

    public final class UserBusinessContactRoles {
        public static final String DIRECTOR = "DIRECTOR";
        public static final String OWNER = "OWNER";
        public static final String OTHER = "OTHER";
    }

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            UserBusinessContactRoles.DIRECTOR,
            UserBusinessContactRoles.OWNER,
            UserBusinessContactRoles.OTHER
    })
    public @interface BusinessContactRole {

    }

    public final class UserBusinessTypes {
        public static final String CORPORATION = "CORPORATION";
        public static final String PARTNERSHIP = "PARTNERSHIP";
    }

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            UserBusinessTypes.CORPORATION,
            UserBusinessTypes.PARTNERSHIP
    })
    public @interface UserBusinessType {

    }

    public final class UserFields {
        // common user field keys
        public static final String TOKEN = "token";
        public static final String STATUS = "status";
        public static final String VERIFICATION_STATUS = "verificationStatus";
        public static final String CREATED_ON = "createdOn";
        public static final String CLIENT_USER_ID = "clientUserId";
        public static final String ADDRESS_LINE_1 = "addressLine1";
        public static final String ADDRESS_LINE_2 = "addressLine2";
        public static final String BUSINESS_CONTACT_ROLE = "businessContactRole";
        public static final String BUSINESS_NAME = "businessName";
        public static final String BUSINESS_REGISTRATION_COUNTRY = "businessRegistrationCountry";
        public static final String BUSINESS_REGISTRATION_ID = "businessRegistrationId";
        public static final String BUSINESS_REGISTRATION_STATE_PROVINCE = "businessRegistrationStateProvince";
        public static final String BUSINESS_CONTACT_ADDRESS_LINE_1 = "businessContactAddressLine1";
        public static final String BUSINESS_CONTACT_ADDRESS_LINE_2 = "businessContactAddressLine2";
        public static final String BUSINESS_CONTACT_CITY = "businessContactCity";
        public static final String BUSINESS_CONTACT_STATE_PROVINCE = "businessContactStateProvince";
        public static final String BUSINESS_CONTACT_COUNTRY = "businessContactCountry";
        public static final String BUSINESS_CONTACT_POSTAL_CODE = "businessContactPostalCode";
        public static final String BUSINESS_OPERATING_NAME = "businessOperatingName";
        public static final String BUSINESS_TYPE = "businessType";
        public static final String CITY = "city";
        public static final String COUNTRY = "country";
        public static final String COUNTRY_OF_BIRTH = "countryOfBirth";
        public static final String COUNTRY_OF_NATIONALITY = "countryOfNationality";
        public static final String DATE_OF_BIRTH = "dateOfBirth";
        public static final String DRIVER_LICENSE_ID = "driversLicenseId";
        public static final String EMAIL = "email";
        public static final String EMPLOYER_ID = "employerId";
        public static final String FIRST_NAME = "firstName";
        public static final String GENDER = "gender";
        public static final String GOVERNMENT_ID = "governmentId";
        public static final String GOVERNMENT_ID_TYPE = "governmentIdType";
        public static final String LAST_NAME = "lastName";
        public static final String MIDDLE_NAME = "middleName";
        public static final String MOBILE_NUMBER = "mobileNumber";
        public static final String PASSPORT_ID = "passportId";
        public static final String PHONE_NUMBER = "phoneNumber";
        public static final String POSTAL_CODE = "postalCode";
        public static final String PROFILE_TYPE = "profileType";
        public static final String PROGRAM_TOKEN = "programToken";
        public static final String STATE_PROVINCE = "stateProvince";
    }

    private Map<String, Object> mFields;

    public HyperwalletUser(@NonNull JSONObject jsonObject) throws JSONException {
        mFields = new HashMap<>();
    }

    public HyperwalletUser(@NonNull Map<String, Object> fields) {
        super();
        setFields(fields);
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
    public void setField(@NonNull @HyperwalletTransferMethod.TransferMethodFieldKey String key, Object value) {
        mFields.put(key, value);
    }

    /**
     * Returns a {@link String} value of a {@code Map<String, String>}
     * <p>
     * Please use {@code getField(@NonNull String key, @NonNull Class<T> clazz)} if the value is not a {@link String}
     * </p>
     *
     * @param key can only be a {@link String} that represents a {@link HyperwalletField} name
     * @return a {@link String} value that represents the value of a {@link HyperwalletField}
     */
    @Nullable
    public String getField(@NonNull @HyperwalletTransferMethod.TransferMethodFieldKey String key) {
        return mFields.get(key) != null ? (String) mFields.get(key) : null;
    }

    /**
     * Returns a {@link Class<T>} value of a {@code Map<String, T>}
     *
     * @param key can only be {@link String}
     * @return a {@code T} value
     */
    @Nullable
    public <T> T getField(@NonNull @HyperwalletTransferMethod.TransferMethodFieldKey String key,
            @NonNull Class<T> clazz) {
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

    public static final Creator<HyperwalletUser> CREATOR =
            new Creator<HyperwalletUser>() {
                @Override
                public HyperwalletUser createFromParcel(Parcel source) {
                    final Map<String, Object> fields = new HashMap<>();
                    source.readMap(fields, this.getClass().getClassLoader());
                    return new HyperwalletUser(fields);
                }

                @Override
                public HyperwalletUser[] newArray(int size) {
                    return new HyperwalletUser[0];
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

        public HyperwalletUser.Builder token(@NonNull String token) {
            mFields.put(UserFields.TOKEN, token);
            return this;
        }

        public HyperwalletUser.Builder status(String status) {
            mFields.put(UserFields.STATUS, status);
            return this;
        }

        public HyperwalletUser.Builder verificationStatus(String verificationStatus) {
            mFields.put(UserFields.VERIFICATION_STATUS, verificationStatus);
            return this;
        }

        public HyperwalletUser.Builder createdOn(String createdOn) {
            mFields.put(UserFields.CREATED_ON, createdOn);
            return this;
        }

        public HyperwalletUser.Builder clientUserId(String clientUserId) {
            mFields.put(UserFields.CLIENT_USER_ID, clientUserId);
            return this;
        }

        public HyperwalletUser.Builder addressLine1(String addressLine1) {
            mFields.put(UserFields.ADDRESS_LINE_1, addressLine1);
            return this;
        }

        public HyperwalletUser.Builder addressLine2(String addressLine2) {
            mFields.put(UserFields.ADDRESS_LINE_2, addressLine2);
            return this;
        }

        public HyperwalletUser.Builder businessContactRole(String businessContactRole) {
            mFields.put(UserFields.BUSINESS_CONTACT_ROLE, businessContactRole);
            return this;
        }

        public HyperwalletUser.Builder businessName(String businessName) {
            mFields.put(UserFields.BUSINESS_CONTACT_ROLE, businessName);
            return this;
        }

        public HyperwalletUser.Builder email(String email) {
            mFields.put(UserFields.EMAIL, email);
            return this;
        }

        public HyperwalletUser build() {
            return new HyperwalletUser(mFields);
        }
    }
}
