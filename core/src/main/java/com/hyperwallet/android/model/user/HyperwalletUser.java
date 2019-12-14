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
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.hyperwallet.android.model.user;

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
 * Represents the User fields.
 */
public final class HyperwalletUser implements HyperwalletJsonModel, Parcelable {

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
    private Map<String, Object> mFields;

    /**
     * Construct a {@code User} object from {@link JSONObject} representation
     *
     * @param jsonObject raw data information
     */
    public HyperwalletUser(@NonNull JSONObject jsonObject) throws JSONException {
        toMap(jsonObject);
    }

    /**
     * Construct a {@code User} object from Map key-value pair representation
     *
     * @param fields map key-value raw data information
     */
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
     * Returns a {@link String} value of a {@code Map<String, String>}
     * <p>
     * Please use {@code getFieldValueToString(@NonNull String key, @NonNull Class<T> clazz)} if the value is not a
     * {@link String}
     * </p>
     *
     * @param key can only be a {@link String} that represents a {@link HyperwalletUser.UserField} name
     * @return a {@link String} value that represents the value of a {@link HyperwalletField}
     */
    @Nullable
    public String getField(@NonNull @UserField String key) {
        return mFields.get(key) != null ? (String) mFields.get(key) : null;
    }

    @Nullable
    public String getToken() {
        return getField(UserFields.TOKEN);
    }

    @Nullable
    @UserStatus
    public String getStatus() {
        return getField(UserFields.STATUS);
    }

    @Nullable
    @VerificationStatus
    public String getVerificationStatus() {
        return getField(UserFields.VERIFICATION_STATUS);
    }

    @Nullable
    public String getCreatedOn() {
        return getField(UserFields.CREATED_ON);
    }

    @Nullable
    public String getClientUserId() {
        return getField(UserFields.CLIENT_USER_ID);
    }

    @Nullable
    public String getAddressLine1() {
        return getField(UserFields.ADDRESS_LINE_1);
    }

    @Nullable
    public String getAddressLine2() {
        return getField(UserFields.ADDRESS_LINE_2);
    }

    @Nullable
    @BusinessContactRole
    public String getBusinessContactRole() {
        return getField(UserFields.BUSINESS_CONTACT_ROLE);
    }

    @Nullable
    public String getBusinessName() {
        return getField(UserFields.BUSINESS_NAME);
    }

    @Nullable
    public String getBusinessRegistrationCountry() {
        return getField(UserFields.BUSINESS_REGISTRATION_COUNTRY);
    }

    @Nullable
    public String getBusinessRegistrationId() {
        return getField(UserFields.BUSINESS_REGISTRATION_ID);
    }

    @Nullable
    public String getBusinessRegistrationStateProvince() {
        return getField(UserFields.BUSINESS_REGISTRATION_STATE_PROVINCE);
    }

    @Nullable
    public String getBusinessContactAddressLine1() {
        return getField(UserFields.BUSINESS_CONTACT_ADDRESS_LINE_1);
    }

    @Nullable
    public String getBusinessContactAddressLine2() {
        return getField(UserFields.BUSINESS_CONTACT_ADDRESS_LINE_2);
    }

    @Nullable
    public String getBusinessContactCity() {
        return getField(UserFields.BUSINESS_CONTACT_CITY);
    }

    @Nullable
    public String getBusinessContactStateProvince() {
        return getField(UserFields.BUSINESS_CONTACT_STATE_PROVINCE);
    }

    @Nullable
    public String getBusinessContactCountry() {
        return getField(UserFields.BUSINESS_CONTACT_COUNTRY);
    }

    @Nullable
    public String getBusinessContactPostalCode() {
        return getField(UserFields.BUSINESS_CONTACT_POSTAL_CODE);
    }

    @Nullable
    public String getBusinessOperatingName() {
        return getField(UserFields.BUSINESS_OPERATING_NAME);
    }

    @Nullable
    @BusinessType
    public String getBusinessType() {
        return getField(UserFields.BUSINESS_TYPE);
    }

    @Nullable
    public String getCity() {
        return getField(UserFields.CITY);
    }

    @Nullable
    public String getCountry() {
        return getField(UserFields.COUNTRY);
    }

    @Nullable
    public String getCountryOfBirth() {
        return getField(UserFields.COUNTRY_OF_BIRTH);
    }

    @Nullable
    public String getCountryOfNationality() {
        return getField(UserFields.COUNTRY_OF_NATIONALITY);
    }

    @Nullable
    public String getDateOfBirth() {
        return getField(UserFields.DATE_OF_BIRTH);
    }

    @Nullable
    public String getDriversLicenseId() {
        return getField(UserFields.DRIVERS_LICENSE_ID);
    }

    @Nullable
    public String getEmail() {
        return getField(UserFields.EMAIL);
    }

    @Nullable
    public String getEmployerId() {
        return getField(UserFields.EMPLOYER_ID);
    }

    @Nullable
    public String getFirstName() {
        return getField(UserFields.FIRST_NAME);
    }

    @Nullable
    @Gender
    public String getGender() {
        return getField(UserFields.GENDER);
    }

    @Nullable
    public String getGovernmentId() {
        return getField(UserFields.GOVERNMENT_ID);
    }

    @Nullable
    @GovernmentIdType
    public String getGovernmentIdType() {
        return getField(UserFields.GOVERNMENT_ID_TYPE);
    }

    @Nullable
    public String getLanguage() {
        return getField(UserFields.LANGUAGE);
    }

    @Nullable
    public String getLastName() {
        return getField(UserFields.LAST_NAME);
    }

    @Nullable
    public String getMiddleName() {
        return getField(UserFields.MIDDLE_NAME);
    }

    @Nullable
    public String getMobileNumber() {
        return getField(UserFields.MOBILE_NUMBER);
    }

    @Nullable
    public String getPassportId() {
        return getField(UserFields.PASSPORT_ID);
    }

    @Nullable
    public String getPhoneNumber() {
        return getField(UserFields.PHONE_NUMBER);
    }

    @Nullable
    public String getPostalCode() {
        return getField(UserFields.POSTAL_CODE);
    }

    @Nullable
    @ProfileType
    public String getProfileType() {
        return getField(UserFields.PROFILE_TYPE);
    }

    @Nullable
    public String getProgramToken() {
        return getField(UserFields.PROGRAM_TOKEN);
    }

    @Nullable
    public String getStateProvince() {
        return getField(UserFields.STATE_PROVINCE);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeMap(mFields);
    }

    protected void setFields(@NonNull Map<String, Object> fields) {
        mFields = new HashMap<>(fields);
    }

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

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            VerificationStatuses.NOT_REQUIRED,
            VerificationStatuses.REQUIRED,
            VerificationStatuses.FAILED,
            VerificationStatuses.UNDER_REVIEW,
            VerificationStatuses.VERIFIED
    })
    public @interface VerificationStatus {
    }

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            BusinessContactRoles.DIRECTOR,
            BusinessContactRoles.OWNER,
            BusinessContactRoles.OTHER
    })
    public @interface BusinessContactRole {

    }

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            BusinessTypes.CORPORATION,
            BusinessTypes.PARTNERSHIP
    })
    public @interface BusinessType {

    }

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            GovernmentIdTypes.PASSPORT,
            GovernmentIdTypes.NATIONAL_ID_CARD
    })
    public @interface GovernmentIdType {

    }

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            ProfileTypes.INDIVIDUAL,
            ProfileTypes.BUSINESS
    })
    public @interface ProfileType {

    }

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            Genders.MALE,
            Genders.FEMALE
    })
    public @interface Gender {

    }

    /**
     * Common User field keys
     */
    public interface UserFields {
        String TOKEN = "token";
        String STATUS = "status";
        String VERIFICATION_STATUS = "verificationStatus";
        String CREATED_ON = "createdOn";
        String CLIENT_USER_ID = "clientUserId";
        String ADDRESS_LINE_1 = "addressLine1";
        String ADDRESS_LINE_2 = "addressLine2";
        String BUSINESS_CONTACT_ROLE = "businessContactRole";
        String BUSINESS_NAME = "businessName";
        String BUSINESS_REGISTRATION_COUNTRY = "businessRegistrationCountry";
        String BUSINESS_REGISTRATION_ID = "businessRegistrationId";
        String BUSINESS_REGISTRATION_STATE_PROVINCE = "businessRegistrationStateProvince";
        String BUSINESS_CONTACT_ADDRESS_LINE_1 = "businessContactAddressLine1";
        String BUSINESS_CONTACT_ADDRESS_LINE_2 = "businessContactAddressLine2";
        String BUSINESS_CONTACT_CITY = "businessContactCity";
        String BUSINESS_CONTACT_STATE_PROVINCE = "businessContactStateProvince";
        String BUSINESS_CONTACT_COUNTRY = "businessContactCountry";
        String BUSINESS_CONTACT_POSTAL_CODE = "businessContactPostalCode";
        String BUSINESS_OPERATING_NAME = "businessOperatingName";
        String BUSINESS_TYPE = "businessType";
        String CITY = "city";
        String COUNTRY = "country";
        String COUNTRY_OF_BIRTH = "countryOfBirth";
        String COUNTRY_OF_NATIONALITY = "countryOfNationality";
        String DATE_OF_BIRTH = "dateOfBirth";
        String DRIVERS_LICENSE_ID = "driversLicenseId";
        String EMAIL = "email";
        String EMPLOYER_ID = "employerId";
        String FIRST_NAME = "firstName";
        String GENDER = "gender";
        String GOVERNMENT_ID = "governmentId";
        String GOVERNMENT_ID_TYPE = "governmentIdType";
        String LANGUAGE = "language";
        String LAST_NAME = "lastName";
        String MIDDLE_NAME = "middleName";
        String MOBILE_NUMBER = "mobileNumber";
        String PASSPORT_ID = "passportId";
        String PHONE_NUMBER = "phoneNumber";
        String POSTAL_CODE = "postalCode";
        String PROFILE_TYPE = "profileType";
        String PROGRAM_TOKEN = "programToken";
        String STATE_PROVINCE = "stateProvince";
    }

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            UserFields.TOKEN,
            UserFields.STATUS,
            UserFields.VERIFICATION_STATUS,
            UserFields.CREATED_ON,
            UserFields.CLIENT_USER_ID,
            UserFields.ADDRESS_LINE_1,
            UserFields.ADDRESS_LINE_2,
            UserFields.BUSINESS_CONTACT_ROLE,
            UserFields.BUSINESS_NAME,
            UserFields.BUSINESS_REGISTRATION_COUNTRY,
            UserFields.BUSINESS_REGISTRATION_ID,
            UserFields.BUSINESS_REGISTRATION_STATE_PROVINCE,
            UserFields.BUSINESS_CONTACT_ADDRESS_LINE_1,
            UserFields.BUSINESS_CONTACT_ADDRESS_LINE_2,
            UserFields.BUSINESS_CONTACT_CITY,
            UserFields.BUSINESS_CONTACT_STATE_PROVINCE,
            UserFields.BUSINESS_CONTACT_COUNTRY,
            UserFields.BUSINESS_CONTACT_POSTAL_CODE,
            UserFields.BUSINESS_OPERATING_NAME,
            UserFields.BUSINESS_TYPE,
            UserFields.CITY,
            UserFields.COUNTRY,
            UserFields.COUNTRY_OF_BIRTH,
            UserFields.COUNTRY_OF_NATIONALITY,
            UserFields.DATE_OF_BIRTH,
            UserFields.DRIVERS_LICENSE_ID,
            UserFields.EMAIL,
            UserFields.EMPLOYER_ID,
            UserFields.FIRST_NAME,
            UserFields.GENDER,
            UserFields.GOVERNMENT_ID,
            UserFields.GOVERNMENT_ID_TYPE,
            UserFields.LANGUAGE,
            UserFields.LAST_NAME,
            UserFields.MIDDLE_NAME,
            UserFields.MOBILE_NUMBER,
            UserFields.PASSPORT_ID,
            UserFields.PHONE_NUMBER,
            UserFields.POSTAL_CODE,
            UserFields.PROFILE_TYPE,
            UserFields.PROGRAM_TOKEN,
            UserFields.STATE_PROVINCE
    })
    public @interface UserField {
    }

    /**
     * Builder for {@link HyperwalletUser} representation
     */
    public static class Builder {
        private Map<String, Object> mFields;

        public Builder() {
            mFields = new HashMap<>();
        }

        public HyperwalletUser.Builder token(@NonNull final String token) {
            mFields.put(UserFields.TOKEN, token);
            return this;
        }

        public HyperwalletUser.Builder status(@NonNull @UserStatus final String status) {
            mFields.put(UserFields.STATUS, status);
            return this;
        }

        public HyperwalletUser.Builder verificationStatus(
                @NonNull @VerificationStatus final String verificationStatus) {
            mFields.put(UserFields.VERIFICATION_STATUS, verificationStatus);
            return this;
        }

        public HyperwalletUser.Builder createdOn(@NonNull final String createdOn) {
            mFields.put(UserFields.CREATED_ON, createdOn);
            return this;
        }

        public HyperwalletUser.Builder clientUserId(@NonNull final String clientUserId) {
            mFields.put(UserFields.CLIENT_USER_ID, clientUserId);
            return this;
        }

        public HyperwalletUser.Builder addressLine1(@NonNull final String addressLine1) {
            mFields.put(UserFields.ADDRESS_LINE_1, addressLine1);
            return this;
        }

        public HyperwalletUser.Builder addressLine2(@NonNull final String addressLine2) {
            mFields.put(UserFields.ADDRESS_LINE_2, addressLine2);
            return this;
        }

        public HyperwalletUser.Builder businessContactRole(
                @NonNull @BusinessContactRole final String businessContactRole) {
            mFields.put(UserFields.BUSINESS_CONTACT_ROLE, businessContactRole);
            return this;
        }

        public HyperwalletUser.Builder businessName(@NonNull final String businessName) {
            mFields.put(UserFields.BUSINESS_NAME, businessName);
            return this;
        }

        public HyperwalletUser.Builder businessRegistrationCountry(@NonNull final String businessRegistrationCountry) {
            mFields.put(UserFields.BUSINESS_REGISTRATION_COUNTRY, businessRegistrationCountry);
            return this;
        }

        public HyperwalletUser.Builder businessRegistrationId(@NonNull final String businessRegistrationId) {
            mFields.put(UserFields.BUSINESS_REGISTRATION_ID, businessRegistrationId);
            return this;
        }

        public HyperwalletUser.Builder businessRegistrationStateProvince(
                @NonNull final String businessRegistrationStateProvince) {
            mFields.put(UserFields.BUSINESS_REGISTRATION_STATE_PROVINCE, businessRegistrationStateProvince);
            return this;
        }

        public HyperwalletUser.Builder businessContactAddressLine1(@NonNull final String businessContactAddressLine1) {
            mFields.put(UserFields.BUSINESS_CONTACT_ADDRESS_LINE_1, businessContactAddressLine1);
            return this;
        }

        public HyperwalletUser.Builder businessContactAddressLine2(@NonNull final String businessContactAddressLine2) {
            mFields.put(UserFields.BUSINESS_CONTACT_ADDRESS_LINE_2, businessContactAddressLine2);
            return this;
        }

        public HyperwalletUser.Builder businessContactCity(@NonNull final String businessContactCity) {
            mFields.put(UserFields.BUSINESS_CONTACT_CITY, businessContactCity);
            return this;
        }

        public HyperwalletUser.Builder businessContactStateProvince(
                @NonNull final String businessContactStateProvince) {
            mFields.put(UserFields.BUSINESS_CONTACT_STATE_PROVINCE, businessContactStateProvince);
            return this;
        }

        public HyperwalletUser.Builder businessContactCountry(@NonNull final String businessContactCountry) {
            mFields.put(UserFields.BUSINESS_CONTACT_COUNTRY, businessContactCountry);
            return this;
        }

        public HyperwalletUser.Builder businessContactPostalCode(@NonNull final String businessContactPostalCode) {
            mFields.put(UserFields.BUSINESS_CONTACT_POSTAL_CODE, businessContactPostalCode);
            return this;
        }

        public HyperwalletUser.Builder businessOperatingName(@NonNull final String businessOperatingName) {
            mFields.put(UserFields.BUSINESS_OPERATING_NAME, businessOperatingName);
            return this;
        }

        public HyperwalletUser.Builder businessType(@NonNull @BusinessType final String businessType) {
            mFields.put(UserFields.BUSINESS_TYPE, businessType);
            return this;
        }

        public HyperwalletUser.Builder city(@NonNull final String city) {
            mFields.put(UserFields.CITY, city);
            return this;
        }

        public HyperwalletUser.Builder country(@NonNull final String country) {
            mFields.put(UserFields.COUNTRY, country);
            return this;
        }

        public HyperwalletUser.Builder countryOfBirth(@NonNull final String countryOfBirth) {
            mFields.put(UserFields.COUNTRY_OF_BIRTH, countryOfBirth);
            return this;
        }

        public HyperwalletUser.Builder countryOfNationality(@NonNull final String countryOfNationality) {
            mFields.put(UserFields.COUNTRY_OF_NATIONALITY, countryOfNationality);
            return this;
        }

        public HyperwalletUser.Builder dateOfBirth(@NonNull final String dateOfBirth) {
            mFields.put(UserFields.DATE_OF_BIRTH, dateOfBirth);
            return this;
        }

        public HyperwalletUser.Builder driversLicenseId(@NonNull final String driversLicenseId) {
            mFields.put(UserFields.DRIVERS_LICENSE_ID, driversLicenseId);
            return this;
        }

        public HyperwalletUser.Builder email(@NonNull final String email) {
            mFields.put(UserFields.EMAIL, email);
            return this;
        }

        public HyperwalletUser.Builder employerId(@NonNull final String employerId) {
            mFields.put(UserFields.EMPLOYER_ID, employerId);
            return this;
        }

        public HyperwalletUser.Builder firstName(@NonNull final String firstName) {
            mFields.put(UserFields.FIRST_NAME, firstName);
            return this;
        }

        public HyperwalletUser.Builder gender(@NonNull @Gender final String gender) {
            mFields.put(UserFields.GENDER, gender);
            return this;
        }

        public HyperwalletUser.Builder governmentId(@NonNull final String governmentId) {
            mFields.put(UserFields.GOVERNMENT_ID, governmentId);
            return this;
        }

        public HyperwalletUser.Builder governmentIdType(@NonNull @GovernmentIdType final String governmentIdType) {
            mFields.put(UserFields.GOVERNMENT_ID_TYPE, governmentIdType);
            return this;
        }

        public HyperwalletUser.Builder language(@NonNull final String language) {
            mFields.put(UserFields.LANGUAGE, language);
            return this;
        }

        public HyperwalletUser.Builder lastName(@NonNull final String lastName) {
            mFields.put(UserFields.LAST_NAME, lastName);
            return this;
        }

        public HyperwalletUser.Builder middleName(@NonNull final String middleName) {
            mFields.put(UserFields.MIDDLE_NAME, middleName);
            return this;
        }

        public HyperwalletUser.Builder mobileNumber(@NonNull final String mobileNumber) {
            mFields.put(UserFields.MOBILE_NUMBER, mobileNumber);
            return this;
        }

        public HyperwalletUser.Builder passportId(@NonNull final String passportId) {
            mFields.put(UserFields.PASSPORT_ID, passportId);
            return this;
        }

        public HyperwalletUser.Builder phoneNumber(@NonNull final String phoneNumber) {
            mFields.put(UserFields.PHONE_NUMBER, phoneNumber);
            return this;
        }

        public HyperwalletUser.Builder postalCode(@NonNull final String postalCode) {
            mFields.put(UserFields.POSTAL_CODE, postalCode);
            return this;
        }

        public HyperwalletUser.Builder profileType(@NonNull @ProfileType final String profileType) {
            mFields.put(UserFields.PROFILE_TYPE, profileType);
            return this;
        }

        public HyperwalletUser.Builder programToken(@NonNull final String programToken) {
            mFields.put(UserFields.PROGRAM_TOKEN, programToken);
            return this;
        }

        public HyperwalletUser.Builder stateProvince(@NonNull final String stateProvince) {
            mFields.put(UserFields.STATE_PROVINCE, stateProvince);
            return this;
        }

        public HyperwalletUser build() {
            return new HyperwalletUser(mFields);
        }
    }

    public final class UserStatuses {
        public static final String PRE_ACTIVATED = "PRE_ACTIVATED";
        public static final String ACTIVATED = "ACTIVATED";
        public static final String LOCKED = "LOCKED";
        public static final String FROZEN = "FROZEN";
        public static final String DE_ACTIVATED = "DE_ACTIVATED";
    }

    public final class VerificationStatuses {
        public static final String NOT_REQUIRED = "NOT_REQUIRED";
        public static final String REQUIRED = "REQUIRED";
        public static final String FAILED = "FAILED";
        public static final String UNDER_REVIEW = "UNDER_REVIEW";
        public static final String VERIFIED = "VERIFIED";
    }

    public final class BusinessContactRoles {
        public static final String DIRECTOR = "DIRECTOR";
        public static final String OWNER = "OWNER";
        public static final String OTHER = "OTHER";
    }

    public final class BusinessTypes {
        public static final String CORPORATION = "CORPORATION";
        public static final String PARTNERSHIP = "PARTNERSHIP";
    }

    public final class GovernmentIdTypes {
        public static final String PASSPORT = "PASSPORT";
        public static final String NATIONAL_ID_CARD = "NATIONAL_ID_CARD";
    }

    public final class ProfileTypes {
        public static final String INDIVIDUAL = "INDIVIDUAL";
        public static final String BUSINESS = "BUSINESS";
    }

    public final class Genders {
        public static final String MALE = "MALE";
        public static final String FEMALE = "FEMALE";
    }
}
