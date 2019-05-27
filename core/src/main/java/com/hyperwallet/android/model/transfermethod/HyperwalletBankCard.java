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

package com.hyperwallet.android.model.transfermethod;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the bank card fields
 */

public class HyperwalletBankCard extends HyperwalletTransferMethod {

    /**
     * Constructor to build bank card, based on json object
     */
    public HyperwalletBankCard(@NonNull JSONObject jsonObject) throws JSONException {
        super(jsonObject);
    }

    private HyperwalletBankCard(@NonNull Map<String, Object> fields) {
        super();
        setFields(fields);
    }

    public static class Builder {
        private Map<String, Object> mFields;

        public Builder() {
            mFields = new HashMap<>();
            mFields.put(TransferMethodFields.TYPE, TransferMethodTypes.BANK_CARD);
        }

        public Builder(@NonNull String transferMethodCountry,
                @NonNull String transferMethodCurrency,
                @NonNull String cardNumber,
                @NonNull String dateOfExpiry, @NonNull String cvv) {
            mFields = new HashMap<>();
            mFields.put(TransferMethodFields.TYPE, TransferMethodTypes.BANK_CARD);
            mFields.put(TransferMethodFields.TRANSFER_METHOD_CURRENCY, transferMethodCurrency);
            mFields.put(TransferMethodFields.TRANSFER_METHOD_COUNTRY, transferMethodCountry);
            mFields.put(TransferMethodFields.CARD_NUMBER, cardNumber);
            mFields.put(TransferMethodFields.DATE_OF_EXPIRY, dateOfExpiry);
            mFields.put(TransferMethodFields.CVV, cvv);
        }

        public Builder cardBrand(String cardBrand) {
            mFields.put(TransferMethodFields.CARD_BRAND, cardBrand);
            return this;
        }

        public Builder cardNumber(@NonNull String cardNumber) {
            mFields.put(TransferMethodFields.CARD_NUMBER, cardNumber);
            return this;
        }

        public Builder cvv(@NonNull String cvv) {
            mFields.put(TransferMethodFields.CVV, cvv);
            return this;
        }

        public Builder cardType(String cardType) {
            mFields.put(TransferMethodFields.CARD_TYPE, cardType);
            return this;
        }

        public Builder dateOfExpiry(@NonNull String dateOfExpiry) {
            mFields.put(TransferMethodFields.DATE_OF_EXPIRY, dateOfExpiry);
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

        public HyperwalletBankCard build() {
            return new HyperwalletBankCard(mFields);
        }
    }
}
