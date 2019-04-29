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

package com.hyperwallet.android.model;

import androidx.annotation.NonNull;
import androidx.annotation.StringDef;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents the bank account fields
 */
public class HyperwalletBankAccount extends HyperwalletTransferMethod {

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            Purpose.CHECKING,
            Purpose.SAVINGS
    })
    public @interface PurposeType {
    }

    public final class Purpose {
        public static final String CHECKING = "CHECKING";
        public static final String SAVINGS = "SAVINGS";
    }

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            BankAccountRelationships.SELF,
            BankAccountRelationships.OWN_COMPANY
    })
    public @interface BankAccountRelationshipType {
    }

    public final class BankAccountRelationships {
        public static final String OWN_COMPANY = "OWN_COMPANY";
        public static final String SELF = "SELF";
    }

    public HyperwalletBankAccount(@NonNull JSONObject jsonObject) throws JSONException {
        super(jsonObject);
    }

    private HyperwalletBankAccount(@NonNull Map<String, Object> fields) {
        super();
        setFields(fields);
    }

    public static class Builder {
        private Map<String, Object> mFields;

        public Builder() {
            mFields = new HashMap<>();
            mFields.put(TransferMethodFields.TYPE, TransferMethodTypes.BANK_ACCOUNT);
        }

        /**
         * Minimum required information to create Bank account
         *
         * @param transferMethodCountry  The bank account country.
         * @param transferMethodCurrency The bank account currency.
         * @param bankAccountId          The bank account number, IBAN or equivalent.
         *                               If you are providing an IBAN, the first two letters of the
         *                               IBAN must match the transferMethodCountry
         */
        public Builder(@NonNull String transferMethodCountry,
                @NonNull String transferMethodCurrency,
                @NonNull String bankAccountId) {
            mFields = new HashMap<>();
            mFields.put(TransferMethodFields.TYPE, TransferMethodTypes.BANK_ACCOUNT);
            mFields.put(TransferMethodFields.TRANSFER_METHOD_CURRENCY, transferMethodCurrency);
            mFields.put(TransferMethodFields.TRANSFER_METHOD_COUNTRY, transferMethodCountry);
            mFields.put(TransferMethodFields.BANK_ACCOUNT_ID, bankAccountId);
        }

        public Builder bankAccountId(@NonNull String bankAccountId) {
            mFields.put(TransferMethodFields.BANK_ACCOUNT_ID, bankAccountId);
            return this;
        }

        public Builder bankAccountPurpose(@PurposeType String bankAccountPurpose) {
            mFields.put(TransferMethodFields.BANK_ACCOUNT_PURPOSE, bankAccountPurpose);
            return this;
        }

        public Builder bankAccountRelationship(@BankAccountRelationshipType String bankAccountRelationship) {
            mFields.put(TransferMethodFields.BANK_ACCOUNT_RELATIONSHIP, bankAccountRelationship);
            return this;
        }

        public Builder bankId(String bankId) {
            mFields.put(TransferMethodFields.BANK_ID, bankId);
            return this;
        }

        public Builder bankName(String bankName) {
            mFields.put(TransferMethodFields.BANK_NAME, bankName);
            return this;
        }

        public Builder branchId(String branchId) {
            mFields.put(TransferMethodFields.BRANCH_ID, branchId);
            return this;
        }

        public Builder branchName(String branchName) {
            mFields.put(TransferMethodFields.BRANCH_NAME, branchName);
            return this;
        }

        public Builder token(@NonNull String token) {
            mFields.put(TransferMethodFields.TOKEN, token);
            return this;
        }

        public Builder transferMethodCountry(@NonNull String transferMethodCountry) {
            mFields.put(TransferMethodFields.TRANSFER_METHOD_COUNTRY, transferMethodCountry);
            return this;
        }

        public Builder transferMethodCurrency(@NonNull String transferMethodCurrency) {
            mFields.put(TransferMethodFields.TRANSFER_METHOD_CURRENCY, transferMethodCurrency);
            return this;
        }

        public Builder transferMethodType(@NonNull @TransferMethodType String transferMethodType) {
            mFields.put(TransferMethodFields.TYPE, transferMethodType);
            return this;
        }

        public Builder addressLine1(String addressLine1) {
            mFields.put(TransferMethodFields.ADDRESS_LINE_1, addressLine1);
            return this;
        }

        public Builder addressLine2(String addressLine2) {
            mFields.put(TransferMethodFields.ADDRESS_LINE_2, addressLine2);
            return this;
        }

        public Builder city(String city) {
            mFields.put(TransferMethodFields.CITY, city);
            return this;
        }

        public Builder country(String country) {
            mFields.put(TransferMethodFields.COUNTRY, country);
            return this;
        }

        public Builder countryOfBirth(String countryOfBirth) {
            mFields.put(TransferMethodFields.COUNTRY_OF_BIRTH, countryOfBirth);
            return this;
        }

        public Builder countryOfNationality(String countryOfNationality) {
            mFields.put(TransferMethodFields.COUNTRY_OF_NATIONALITY, countryOfNationality);
            return this;
        }

        public Builder dateOfBirth(String dateOfBirth) {
            mFields.put(TransferMethodFields.DATE_OF_BIRTH, dateOfBirth);
            return this;
        }

        public Builder driverLicenseId(String driverLicenseId) {
            mFields.put(TransferMethodFields.DRIVER_LICENSE_ID, driverLicenseId);
            return this;
        }

        public Builder employerId(String employerId) {
            mFields.put(TransferMethodFields.EMPLOYER_ID, employerId);
            return this;
        }

        public Builder firstName(String firstName) {
            mFields.put(TransferMethodFields.FIRST_NAME, firstName);
            return this;
        }

        public Builder gender(String gender) {
            mFields.put(TransferMethodFields.GENDER, gender);
            return this;
        }

        public Builder governmentId(String governmentId) {
            mFields.put(TransferMethodFields.GOVERNMENT_ID, governmentId);
            return this;
        }

        public Builder governmentIdType(String governmentIdType) {
            mFields.put(TransferMethodFields.GOVERNMENT_ID_TYPE, governmentIdType);
            return this;
        }

        public Builder lastName(String governmentIdType) {
            mFields.put(TransferMethodFields.LAST_NAME, governmentIdType);
            return this;
        }

        public Builder middleName(String middleName) {
            mFields.put(TransferMethodFields.MIDDLE_NAME, middleName);
            return this;
        }

        public Builder mobileNumber(String mobileNumber) {
            mFields.put(TransferMethodFields.MOBILE_NUMBER, mobileNumber);
            return this;
        }

        public Builder passportId(String passportId) {
            mFields.put(TransferMethodFields.PASSPORT_ID, passportId);
            return this;
        }

        public Builder phoneNumber(String phoneNumber) {
            mFields.put(TransferMethodFields.PHONE_NUMBER, phoneNumber);
            return this;
        }

        public Builder postalCode(String postalCode) {
            mFields.put(TransferMethodFields.POSTAL_CODE, postalCode);
            return this;
        }

        public Builder stateProvince(String stateProvince) {
            mFields.put(TransferMethodFields.STATE_PROVINCE, stateProvince);
            return this;
        }

        public HyperwalletBankAccount build() {
            return new HyperwalletBankAccount(mFields);
        }
    }
}
