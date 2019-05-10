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

    public interface UserStatuses {
        String PRE_ACTIVATED = "PRE_ACTIVATED";
        String ACTIVATED = "ACTIVATED";
        String LOCKED = "LOCKED";
        String FROZEN = "FROZEN";
        String DE_ACTIVATED = "DE_ACTIVATED";
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


    public interface VerificationStatuses {
        String NOT_REQUIRED = "NOT_REQUIRED";
        String REQUIRED = "REQUIRED";
        String FAILED = "FAILED";
        String UNDER_REVIEW = "UNDER_REVIEW";
        String VERIFIED = "VERIFIED";
    }

    public interface BusinessContactRoles {
        String DIRECTOR = "DIRECTOR";
        String OWNER = "OWNER";
        String OTHER = "OTHER";
    }

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            BusinessContactRoles.DIRECTOR,
            BusinessContactRoles.OWNER,
            BusinessContactRoles.OTHER
    })
    public @interface BusinessContactRole {

    }

    public interface BusinessTypes {
        String CORPORATION = "CORPORATION";
        String PARTNERSHIP = "PARTNERSHIP";
    }

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            BusinessTypes.CORPORATION,
            BusinessTypes.PARTNERSHIP
    })
    public @interface BusinessType {

    }

    public interface GovernmentIdTypes {
        String PASSPORT = "PASSPORT";
        String NATIONAL_ID_CARD = "NATIONAL_ID_CARD";
    }

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            GovernmentIdTypes.PASSPORT,
            GovernmentIdTypes.NATIONAL_ID_CARD
    })
    public @interface GovernmentIdType {

    }

    public interface ProfileTypes {
        String INDIVIDUAL = "INDIVIDUAL";
        String BUSINESS = "BUSINESS";
    }

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            ProfileTypes.INDIVIDUAL,
            ProfileTypes.BUSINESS
    })
    public @interface ProfileType {

    }

    public interface Genders {
        String MALE = "MALE";
        String FEMALE = "FEMALE";
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

    private Map<String, Object> mFields;

    public HyperwalletUser(@NonNull JSONObject jsonObject) throws JSONException {
        toMap(jsonObject);
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
     * Returns a {@link String} value of a {@code Map<String, String>}
     * <p>
     * Please use {@code getField(@NonNull String key, @NonNull Class<T> clazz)} if the value is not a {@link String}
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
        return (String) mFields.get(UserFields.TOKEN);
    }

    @Nullable
    public @UserStatus
    String getStatus() {
        return (String) mFields.get(UserFields.STATUS);
    }

    @Nullable
    public @VerificationStatus
    String getVerificationStatus() {
        return (String) mFields.get(UserFields.VERIFICATION_STATUS);
    }

    @Nullable
    public String getCreatedOn() {
        return (String) mFields.get(UserFields.CREATED_ON);
    }

    @Nullable
    public String getClientUserId() {
        return (String) mFields.get(UserFields.CLIENT_USER_ID);
    }

    @Nullable
    public String getAddressLine1() {
        return (String) mFields.get(UserFields.ADDRESS_LINE_1);
    }

    @Nullable
    public String getAddressLine2() {
        return (String) mFields.get(UserFields.ADDRESS_LINE_2);
    }

    @Nullable
    public @BusinessContactRole
    String getBusinessContactRole() {
        return (String) mFields.get(UserFields.BUSINESS_CONTACT_ROLE);
    }

    @Nullable
    public String getBusinessName() {
        return (String) mFields.get(UserFields.BUSINESS_NAME);
    }

    @Nullable
    public String getBusinessRegistrationCountry() {
        return (String) mFields.get(UserFields.BUSINESS_REGISTRATION_COUNTRY);
    }

    @Nullable
    public String getBusinessRegistrationId() {
        return (String) mFields.get(UserFields.BUSINESS_REGISTRATION_ID);
    }

    @Nullable
    public String getBusinessRegistrationStateProvince() {
        return (String) mFields.get(UserFields.BUSINESS_REGISTRATION_STATE_PROVINCE);
    }

    @Nullable
    public String getBusinessContactAddressLine1() {
        return (String) mFields.get(UserFields.BUSINESS_CONTACT_ADDRESS_LINE_1);
    }

    @Nullable
    public String getBusinessContactAddressLine2() {
        return (String) mFields.get(UserFields.BUSINESS_CONTACT_ADDRESS_LINE_2);
    }

    @Nullable
    public String getBusinessContactCity() {
        return (String) mFields.get(UserFields.BUSINESS_CONTACT_CITY);
    }

    @Nullable
    public String getBusinessContactStateProvince() {
        return (String) mFields.get(UserFields.BUSINESS_CONTACT_STATE_PROVINCE);
    }

    @Nullable
    public String getBusinessContactCountry() {
        return (String) mFields.get(UserFields.BUSINESS_CONTACT_COUNTRY);
    }

    @Nullable
    public String getBusinessContactPostalCode() {
        return (String) mFields.get(UserFields.BUSINESS_CONTACT_POSTAL_CODE);
    }

    @Nullable
    public String getBusinessOperatingName() {
        return (String) mFields.get(UserFields.BUSINESS_OPERATING_NAME);
    }

    @Nullable
    public @BusinessType
    String getBusinessType() {
        return (String) mFields.get(UserFields.BUSINESS_TYPE);
    }

    @Nullable
    public String getCity() {
        return (String) mFields.get(UserFields.CITY);
    }

    @Nullable
    public String getCountry() {
        return (String) mFields.get(UserFields.COUNTRY);
    }

    @Nullable
    public String getCountryOfBirth() {
        return (String) mFields.get(UserFields.COUNTRY_OF_BIRTH);
    }

    @Nullable
    public String getCountryOfNationality() {
        return (String) mFields.get(UserFields.COUNTRY_OF_NATIONALITY);
    }

    @Nullable
    public String getDateOfBirth() {
        return (String) mFields.get(UserFields.DATE_OF_BIRTH);
    }

    @Nullable
    public String getDriversLicenseId() {
        return (String) mFields.get(UserFields.DRIVERS_LICENSE_ID);
    }

    @Nullable
    public String getEmail() {
        return (String) mFields.get(UserFields.EMAIL);
    }

    @Nullable
    public String getEmployerId() {
        return (String) mFields.get(UserFields.EMPLOYER_ID);
    }

    @Nullable
    public String getFirstName() {
        return (String) mFields.get(UserFields.FIRST_NAME);
    }

    @Nullable
    public @Gender
    String getGender() {
        return (String) mFields.get(UserFields.GENDER);
    }

    @Nullable
    public String getGovernmentId() {
        return (String) mFields.get(UserFields.GOVERNMENT_ID);
    }

    @Nullable
    public @GovernmentIdType
    String getGovernmentIdType() {
        return (String) mFields.get(UserFields.GOVERNMENT_ID_TYPE);
    }

    @Nullable
    public String getLanguage() {
        return (String) mFields.get(UserFields.LANGUAGE);
    }

    @Nullable
    public String getLastName() {
        return (String) mFields.get(UserFields.LAST_NAME);
    }

    @Nullable
    public String getMiddleName() {
        return (String) mFields.get(UserFields.MIDDLE_NAME);
    }

    @Nullable
    public String getMobileNumber() {
        return (String) mFields.get(UserFields.MOBILE_NUMBER);
    }

    @Nullable
    public String getPassportId() {
        return (String) mFields.get(UserFields.PASSPORT_ID);
    }

    @Nullable
    public String getPhoneNumber() {
        return (String) mFields.get(UserFields.PHONE_NUMBER);
    }

    @Nullable
    public String getPostalCode() {
        return (String) mFields.get(UserFields.POSTAL_CODE);
    }

    @Nullable
    public @ProfileType
    String getProfileType() {
        return (String) mFields.get(UserFields.PROFILE_TYPE);
    }

    @Nullable
    public String getProgramToken() {
        return (String) mFields.get(UserFields.PROGRAM_TOKEN);
    }

    @Nullable
    public String getStateProvince() {
        return (String) mFields.get(UserFields.STATE_PROVINCE);
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

        public HyperwalletUser.Builder token(@NonNull final String token) {
            mFields.put(UserFields.TOKEN, token);
            return this;
        }

        public HyperwalletUser.Builder status(@Nullable @UserStatus final String status) {
            mFields.put(UserFields.STATUS, status);
            return this;
        }

        public HyperwalletUser.Builder verificationStatus(
                @Nullable @VerificationStatus final String verificationStatus) {
            mFields.put(UserFields.VERIFICATION_STATUS, verificationStatus);
            return this;
        }

        public HyperwalletUser.Builder createdOn(@Nullable final String createdOn) {
            mFields.put(UserFields.CREATED_ON, createdOn);
            return this;
        }

        public HyperwalletUser.Builder clientUserId(@Nullable final String clientUserId) {
            mFields.put(UserFields.CLIENT_USER_ID, clientUserId);
            return this;
        }

        public HyperwalletUser.Builder addressLine1(@Nullable final String addressLine1) {
            mFields.put(UserFields.ADDRESS_LINE_1, addressLine1);
            return this;
        }

        public HyperwalletUser.Builder addressLine2(@Nullable final String addressLine2) {
            mFields.put(UserFields.ADDRESS_LINE_2, addressLine2);
            return this;
        }

        public HyperwalletUser.Builder businessContactRole(
                @Nullable final @BusinessContactRole String businessContactRole) {
            mFields.put(UserFields.BUSINESS_CONTACT_ROLE, businessContactRole);
            return this;
        }

        public HyperwalletUser.Builder businessName(@Nullable final String businessName) {
            mFields.put(UserFields.BUSINESS_NAME, businessName);
            return this;
        }

        public HyperwalletUser.Builder businessRegistrationCountry(@Nullable final String businessRegistrationCountry) {
            mFields.put(UserFields.BUSINESS_REGISTRATION_COUNTRY, businessRegistrationCountry);
            return this;
        }

        public HyperwalletUser.Builder businessRegistrationId(@Nullable final String businessRegistrationId) {
            mFields.put(UserFields.BUSINESS_REGISTRATION_ID, businessRegistrationId);
            return this;
        }

        public HyperwalletUser.Builder businessRegistrationStateProvince(
                @Nullable final String businessRegistrationStateProvince) {
            mFields.put(UserFields.BUSINESS_REGISTRATION_STATE_PROVINCE, businessRegistrationStateProvince);
            return this;
        }

        public HyperwalletUser.Builder businessContactAddressLine1(@Nullable final String businessContactAddressLine1) {
            mFields.put(UserFields.BUSINESS_CONTACT_ADDRESS_LINE_1, businessContactAddressLine1);
            return this;
        }

        public HyperwalletUser.Builder businessContactAddressLine2(@Nullable final String businessContactAddressLine2) {
            mFields.put(UserFields.BUSINESS_CONTACT_ADDRESS_LINE_2, businessContactAddressLine2);
            return this;
        }

        public HyperwalletUser.Builder businessContactCity(@Nullable final String businessContactCity) {
            mFields.put(UserFields.BUSINESS_CONTACT_CITY, businessContactCity);
            return this;
        }

        public HyperwalletUser.Builder businessContactStateProvince(
                @Nullable final String businessContactStateProvince) {
            mFields.put(UserFields.BUSINESS_CONTACT_STATE_PROVINCE, businessContactStateProvince);
            return this;
        }

        public HyperwalletUser.Builder businessContactCountry(@Nullable final String businessContactCountry) {
            mFields.put(UserFields.BUSINESS_CONTACT_COUNTRY, businessContactCountry);
            return this;
        }

        public HyperwalletUser.Builder businessContactPostalCode(@Nullable final String businessContactPostalCode) {
            mFields.put(UserFields.BUSINESS_CONTACT_POSTAL_CODE, businessContactPostalCode);
            return this;
        }

        public HyperwalletUser.Builder businessOperatingName(@Nullable final String businessOperatingName) {
            mFields.put(UserFields.BUSINESS_OPERATING_NAME, businessOperatingName);
            return this;
        }

        public HyperwalletUser.Builder businessType(@Nullable @BusinessType final String businessType) {
            mFields.put(UserFields.BUSINESS_TYPE, businessType);
            return this;
        }

        public HyperwalletUser.Builder city(@Nullable final String city) {
            mFields.put(UserFields.CITY, city);
            return this;
        }

        public HyperwalletUser.Builder country(@Nullable final String country) {
            mFields.put(UserFields.COUNTRY, country);
            return this;
        }

        public HyperwalletUser.Builder countryOfBirth(@Nullable final String countryOfBirth) {
            mFields.put(UserFields.COUNTRY_OF_BIRTH, countryOfBirth);
            return this;
        }

        public HyperwalletUser.Builder countryOfNationality(@Nullable final String countryOfNationality) {
            mFields.put(UserFields.COUNTRY_OF_NATIONALITY, countryOfNationality);
            return this;
        }

        public HyperwalletUser.Builder dateOfBirth(@Nullable final String dateOfBirth) {
            mFields.put(UserFields.DATE_OF_BIRTH, dateOfBirth);
            return this;
        }

        public HyperwalletUser.Builder driversLicenseId(@Nullable final String driversLicenseId) {
            mFields.put(UserFields.DRIVERS_LICENSE_ID, driversLicenseId);
            return this;
        }

        public HyperwalletUser.Builder email(@Nullable final String email) {
            mFields.put(UserFields.EMAIL, email);
            return this;
        }

        public HyperwalletUser.Builder employerId(@Nullable final String employerId) {
            mFields.put(UserFields.EMPLOYER_ID, employerId);
            return this;
        }

        public HyperwalletUser.Builder firstName(@Nullable final String firstName) {
            mFields.put(UserFields.FIRST_NAME, firstName);
            return this;
        }

        public HyperwalletUser.Builder gender(@Nullable @Gender final String gender) {
            mFields.put(UserFields.GENDER, gender);
            return this;
        }

        public HyperwalletUser.Builder governmentId(@Nullable final String governmentId) {
            mFields.put(UserFields.GOVERNMENT_ID, governmentId);
            return this;
        }

        public HyperwalletUser.Builder governmentIdType(@Nullable @GovernmentIdType final String governmentIdType) {
            mFields.put(UserFields.GOVERNMENT_ID_TYPE, governmentIdType);
            return this;
        }

        public HyperwalletUser.Builder language(@Nullable final String language) {
            mFields.put(UserFields.LANGUAGE, language);
            return this;
        }

        public HyperwalletUser.Builder lastName(@Nullable final String lastName) {
            mFields.put(UserFields.LAST_NAME, lastName);
            return this;
        }

        public HyperwalletUser.Builder middleName(@Nullable final String middleName) {
            mFields.put(UserFields.MIDDLE_NAME, middleName);
            return this;
        }

        public HyperwalletUser.Builder mobileNumber(@Nullable final String mobileNumber) {
            mFields.put(UserFields.MOBILE_NUMBER, mobileNumber);
            return this;
        }

        public HyperwalletUser.Builder passportId(@Nullable final String passportId) {
            mFields.put(UserFields.PASSPORT_ID, passportId);
            return this;
        }

        public HyperwalletUser.Builder phoneNumber(@Nullable final String phoneNumber) {
            mFields.put(UserFields.PHONE_NUMBER, phoneNumber);
            return this;
        }

        public HyperwalletUser.Builder postalCode(@Nullable final String postalCode) {
            mFields.put(UserFields.POSTAL_CODE, postalCode);
            return this;
        }

        public HyperwalletUser.Builder profileType(@Nullable @ProfileType final String profileType) {
            mFields.put(UserFields.PROFILE_TYPE, profileType);
            return this;
        }

        public HyperwalletUser.Builder programToken(@Nullable final String programToken) {
            mFields.put(UserFields.PROGRAM_TOKEN, programToken);
            return this;
        }

        public HyperwalletUser.Builder stateProvince(@Nullable final String stateProvince) {
            mFields.put(UserFields.STATE_PROVINCE, stateProvince);
            return this;
        }

        public HyperwalletUser build() {
            return new HyperwalletUser(mFields);
        }
    }
}
