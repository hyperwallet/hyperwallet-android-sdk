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

package com.hyperwallet.android.model.transfermethod;

import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodFields.EMAIL;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodFields.TRANSFER_METHOD_COUNTRY;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodFields.TRANSFER_METHOD_CURRENCY;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the PayPal Account fields.
 */

public class PayPalAccount extends HyperwalletTransferMethod {

    /**
     * Construct a {@code PayPalAccount} object from {@link JSONObject} representation
     *
     * @param jsonObject raw data information
     */
    public PayPalAccount(@NonNull JSONObject jsonObject) throws JSONException {
        super(jsonObject);
    }

    /**
     * Construct a {@code PayPalAccount} object from Map of key-value pair representation
     *
     * @param fields map of key-value raw data information
     */
    private PayPalAccount(@NonNull Map<String, Object> fields) {
        super();
        setFields(fields);
    }

    @Nullable
    public String getCountry() {
        return getField(TRANSFER_METHOD_COUNTRY);
    }

    @Nullable
    public String getCurrency() {
        return getField(TRANSFER_METHOD_CURRENCY);
    }

    @Nullable
    public String getEmail() {
        return getField(EMAIL);
    }

    /**
     * Builder for {@link PayPalAccount}
     */
    public static class Builder {
        private Map<String, Object> mFields;

        /**
         * Constructs a {@code Builder}
         */
        public Builder() {
            mFields = new HashMap<>();
            mFields.put(TransferMethodFields.TYPE, TransferMethodTypes.PAYPAL_ACCOUNT);
        }

        /**
         * Constructs a {@code Builder} with predefined attributes
         *
         * @param transferMethodCountry  Transfer method country of account
         * @param transferMethodCurrency Transfer method currency of account
         * @param email                  email to bind to {@link PayPalAccount}
         */
        public Builder(@NonNull String transferMethodCountry,
                @NonNull String transferMethodCurrency,
                @NonNull String email) {

            mFields = new HashMap<>();
            mFields.put(TransferMethodFields.TYPE, TransferMethodTypes.PAYPAL_ACCOUNT);
            mFields.put(TRANSFER_METHOD_CURRENCY, transferMethodCurrency);
            mFields.put(TRANSFER_METHOD_COUNTRY, transferMethodCountry);
            mFields.put(TransferMethodFields.EMAIL, email);
        }

        public Builder email(String email) {
            mFields.put(TransferMethodFields.EMAIL, email);
            return this;
        }

        public Builder token(@NonNull String token) {
            mFields.put(TransferMethodFields.TOKEN, token);
            return this;
        }

        public Builder transferMethodCountry(@NonNull String transferMethodCountry) {
            mFields.put(TRANSFER_METHOD_COUNTRY, transferMethodCountry);
            return this;
        }

        public Builder transferMethodCurrency(@NonNull String transferMethodCurrency) {
            mFields.put(TRANSFER_METHOD_CURRENCY, transferMethodCurrency);
            return this;
        }

        public PayPalAccount build() {
            return new PayPalAccount(mFields);
        }
    }
}
