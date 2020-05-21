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

package com.hyperwallet.android.model.transfermethod;

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
public class BankAccount extends TransferMethod {

    /**
     * Construct a {@code BankAccount} object from {@link JSONObject} representation
     *
     * @param jsonObject raw data information
     */
    public BankAccount(@NonNull JSONObject jsonObject) throws JSONException {
        super(jsonObject);
    }

    /**
     * Construct a {@code BankAccount} object from Map key-value pair representation
     *
     * @param fields Map key-value pair raw data information
     */
    private BankAccount(@NonNull Map<String, Object> fields) {
        super();
        setFields(fields);
    }

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            Purpose.CHECKING,
            Purpose.SAVINGS
    })
    public @interface PurposeType {
    }

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            BankAccountRelationships.SELF,
            BankAccountRelationships.OWN_COMPANY
    })
    public @interface BankAccountRelationshipType {
    }

    /**
     * Builder for {@link BankAccount} representation
     */
    public static class Builder {
        private Map<String, Object> mFields;

        /**
         * Construct a {@code Builder} class
         */
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
        public Builder(@NonNull final String transferMethodCountry,
                @NonNull final String transferMethodCurrency,
                @NonNull final String bankAccountId) {
            mFields = new HashMap<>();
            mFields.put(TransferMethodFields.TYPE, TransferMethodTypes.BANK_ACCOUNT);
            mFields.put(TransferMethodFields.TRANSFER_METHOD_CURRENCY, transferMethodCurrency);
            mFields.put(TransferMethodFields.TRANSFER_METHOD_COUNTRY, transferMethodCountry);
            mFields.put(TransferMethodFields.BANK_ACCOUNT_ID, bankAccountId);
        }

        public Builder bankAccountId(@NonNull final String bankAccountId) {
            mFields.put(TransferMethodFields.BANK_ACCOUNT_ID, bankAccountId);
            return this;
        }

        public Builder bankAccountPurpose(@NonNull @PurposeType final String bankAccountPurpose) {
            mFields.put(TransferMethodFields.BANK_ACCOUNT_PURPOSE, bankAccountPurpose);
            return this;
        }

        public Builder bankAccountRelationship(
                @NonNull @BankAccountRelationshipType final String bankAccountRelationship) {
            mFields.put(TransferMethodFields.BANK_ACCOUNT_RELATIONSHIP, bankAccountRelationship);
            return this;
        }

        public Builder bankId(@NonNull final String bankId) {
            mFields.put(TransferMethodFields.BANK_ID, bankId);
            return this;
        }

        public Builder bankName(@NonNull final String bankName) {
            mFields.put(TransferMethodFields.BANK_NAME, bankName);
            return this;
        }

        public Builder branchId(@NonNull final String branchId) {
            mFields.put(TransferMethodFields.BRANCH_ID, branchId);
            return this;
        }

        public Builder branchName(@NonNull final String branchName) {
            mFields.put(TransferMethodFields.BRANCH_NAME, branchName);
            return this;
        }

        public Builder token(@NonNull final String token) {
            mFields.put(TransferMethodFields.TOKEN, token);
            return this;
        }

        public Builder transferMethodCountry(@NonNull final String transferMethodCountry) {
            mFields.put(TransferMethodFields.TRANSFER_METHOD_COUNTRY, transferMethodCountry);
            return this;
        }

        public Builder transferMethodCurrency(@NonNull final String transferMethodCurrency) {
            mFields.put(TransferMethodFields.TRANSFER_METHOD_CURRENCY, transferMethodCurrency);
            return this;
        }

        public Builder transferMethodType(@NonNull @TransferMethodType final String transferMethodType) {
            mFields.put(TransferMethodFields.TYPE, transferMethodType);
            return this;
        }

        public Builder addressLine1(@NonNull final String addressLine1) {
            mFields.put(TransferMethodFields.ADDRESS_LINE_1, addressLine1);
            return this;
        }

        public Builder addressLine2(@NonNull final String addressLine2) {
            mFields.put(TransferMethodFields.ADDRESS_LINE_2, addressLine2);
            return this;
        }

        public Builder city(@NonNull final String city) {
            mFields.put(TransferMethodFields.CITY, city);
            return this;
        }

        public Builder country(@NonNull final String country) {
            mFields.put(TransferMethodFields.COUNTRY, country);
            return this;
        }

        public Builder countryOfBirth(@NonNull final String countryOfBirth) {
            mFields.put(TransferMethodFields.COUNTRY_OF_BIRTH, countryOfBirth);
            return this;
        }

        public Builder countryOfNationality(@NonNull final String countryOfNationality) {
            mFields.put(TransferMethodFields.COUNTRY_OF_NATIONALITY, countryOfNationality);
            return this;
        }

        public Builder dateOfBirth(@NonNull final String dateOfBirth) {
            mFields.put(TransferMethodFields.DATE_OF_BIRTH, dateOfBirth);
            return this;
        }

        public Builder driverLicenseId(@NonNull final String driverLicenseId) {
            mFields.put(TransferMethodFields.DRIVER_LICENSE_ID, driverLicenseId);
            return this;
        }

        public Builder employerId(@NonNull final String employerId) {
            mFields.put(TransferMethodFields.EMPLOYER_ID, employerId);
            return this;
        }

        public Builder firstName(@NonNull final String firstName) {
            mFields.put(TransferMethodFields.FIRST_NAME, firstName);
            return this;
        }

        public Builder gender(@NonNull final String gender) {
            mFields.put(TransferMethodFields.GENDER, gender);
            return this;
        }

        public Builder governmentId(@NonNull final String governmentId) {
            mFields.put(TransferMethodFields.GOVERNMENT_ID, governmentId);
            return this;
        }

        public Builder governmentIdType(@NonNull final String governmentIdType) {
            mFields.put(TransferMethodFields.GOVERNMENT_ID_TYPE, governmentIdType);
            return this;
        }

        public Builder lastName(@NonNull final String governmentIdType) {
            mFields.put(TransferMethodFields.LAST_NAME, governmentIdType);
            return this;
        }

        public Builder middleName(@NonNull final String middleName) {
            mFields.put(TransferMethodFields.MIDDLE_NAME, middleName);
            return this;
        }

        public Builder mobileNumber(@NonNull final String mobileNumber) {
            mFields.put(TransferMethodFields.MOBILE_NUMBER, mobileNumber);
            return this;
        }

        public Builder passportId(@NonNull final String passportId) {
            mFields.put(TransferMethodFields.PASSPORT_ID, passportId);
            return this;
        }

        public Builder phoneNumber(@NonNull final String phoneNumber) {
            mFields.put(TransferMethodFields.PHONE_NUMBER, phoneNumber);
            return this;
        }

        public Builder postalCode(@NonNull final String postalCode) {
            mFields.put(TransferMethodFields.POSTAL_CODE, postalCode);
            return this;
        }

        public Builder stateProvince(@NonNull final String stateProvince) {
            mFields.put(TransferMethodFields.STATE_PROVINCE, stateProvince);
            return this;
        }

        public Builder intermediaryBankAccountId(@NonNull final String intermediaryBankAccountId) {
            mFields.put(TransferMethodFields.INTERMEDIARY_BANK_ACCOUNT_ID, intermediaryBankAccountId);
            return this;
        }

        public Builder intermediaryBankAddressLine1(@NonNull final String intermediaryBankAddressLine1) {
            mFields.put(TransferMethodFields.INTERMEDIARY_BANK_ADDRESS_LINE_1, intermediaryBankAddressLine1);
            return this;
        }

        public Builder intermediaryBankAddressLine2(@NonNull final String intermediaryBankAddressLine2) {
            mFields.put(TransferMethodFields.INTERMEDIARY_BANK_ADDRESS_LINE_2, intermediaryBankAddressLine2);
            return this;
        }

        public Builder intermediaryBankCity(@NonNull final String intermediaryBankCity) {
            mFields.put(TransferMethodFields.INTERMEDIARY_BANK_CITY, intermediaryBankCity);
            return this;
        }

        public Builder intermediaryBankCountry(@NonNull final String intermediaryBankCountry) {
            mFields.put(TransferMethodFields.INTERMEDIARY_BANK_COUNTRY, intermediaryBankCountry);
            return this;
        }

        public Builder intermediaryBankId(@NonNull final String intermediaryBankId) {
            mFields.put(TransferMethodFields.INTERMEDIARY_BANK_ID, intermediaryBankId);
            return this;
        }

        public Builder intermediaryBankName(@NonNull final String intermediaryBankName) {
            mFields.put(TransferMethodFields.INTERMEDIARY_BANK_NAME, intermediaryBankName);
            return this;
        }

        public Builder intermediaryBankPostalCode(@NonNull final String intermediaryBankPostalCode) {
            mFields.put(TransferMethodFields.INTERMEDIARY_BANK_POSTAL_CODE, intermediaryBankPostalCode);
            return this;
        }

        public Builder intermediaryBankStateProvince(@NonNull final String intermediaryBankStateProvince) {
            mFields.put(TransferMethodFields.INTERMEDIARY_BANK_STATE_PROVINCE, intermediaryBankStateProvince);
            return this;
        }

        public Builder wireInstructions(@NonNull final String wireInstructions) {
            mFields.put(TransferMethodFields.WIRE_INSTRUCTIONS, wireInstructions);
            return this;
        }

        public BankAccount build() {
            return new BankAccount(mFields);
        }
    }

    public final class Purpose {
        public static final String CHECKING = "CHECKING";
        public static final String SAVINGS = "SAVINGS";
    }

    public final class BankAccountRelationships {
        public static final String OWN_COMPANY = "OWN_COMPANY";
        public static final String SELF = "SELF";
    }
}
