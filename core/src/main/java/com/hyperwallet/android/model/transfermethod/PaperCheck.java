package com.hyperwallet.android.model.transfermethod;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.TRANSFER_METHOD_COUNTRY;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.TRANSFER_METHOD_CURRENCY;

/**
 * Represents the PaperCheck Account fields.
 */
public class PaperCheck extends TransferMethod {

    /**
     * Construct a {@code PaperCheck} object from {@link JSONObject} representation
     *
     * @param jsonObject raw data information
     */
    public PaperCheck(@NonNull final JSONObject jsonObject) throws JSONException {
        super(jsonObject);
    }

    /**
     * Construct a {@code PaperCheck} object from Map of key-value pair representation
     *
     * @param fields map of key-value raw data information
     */
    private PaperCheck(@NonNull final Map<String, Object> fields) {
        super();
        setFields(fields);
    }

    public String getCountry() {
        return getField(TRANSFER_METHOD_COUNTRY);
    }

    @Nullable
    public String getCurrency() {
        return getField(TRANSFER_METHOD_CURRENCY);
    }

    /**
     * Builder for {@link PaperCheck}
     */
    public static class Builder {
        private Map<String, Object> mFields;

        /**
         * Constructs a {@code Builder}
         */
        public Builder() {
            mFields = new HashMap<>();
            mFields.put(TransferMethodFields.TYPE, TransferMethodTypes.PAPER_CHECK);
        }

        /**
         * Constructs a {@code Builder} with predefined attributes
         *
         * @param transferMethodCountry  Transfer method country of account
         * @param transferMethodCurrency Transfer method currency of account
         */
        public Builder(@NonNull final String transferMethodCountry,
                       @NonNull final String transferMethodCurrency) {

            mFields = new HashMap<>();
            mFields.put(TransferMethodFields.TYPE, TransferMethodTypes.PAPER_CHECK);
            mFields.put(TRANSFER_METHOD_CURRENCY, transferMethodCurrency);
            mFields.put(TRANSFER_METHOD_COUNTRY, transferMethodCountry);
        }

        public PaperCheck.Builder token(@NonNull String token) {
            mFields.put(TransferMethodFields.TOKEN, token);
            return this;
        }

        public PaperCheck.Builder transferMethodCountry(@NonNull String transferMethodCountry) {
            mFields.put(TRANSFER_METHOD_COUNTRY, transferMethodCountry);
            return this;
        }

        public PaperCheck.Builder transferMethodCurrency(@NonNull String transferMethodCurrency) {
            mFields.put(TRANSFER_METHOD_CURRENCY, transferMethodCurrency);
            return this;
        }

        public PaperCheck.Builder shippingMethod(@NonNull final String addressLine1) {
            mFields.put(TransferMethodFields.SHIPPING_METHOD, addressLine1);
            return this;
        }

        public PaperCheck.Builder addressLine1(@NonNull final String addressLine2) {
            mFields.put(TransferMethodFields.ADDRESS_LINE_1, addressLine2);
            return this;
        }

        public PaperCheck.Builder addressLine2(@NonNull final String addressLine2) {
            mFields.put(TransferMethodFields.ADDRESS_LINE_2, addressLine2);
            return this;
        }

        public PaperCheck.Builder city(@NonNull final String city) {
            mFields.put(TransferMethodFields.CITY, city);
            return this;
        }
        public PaperCheck.Builder postalCode(@NonNull final String postalCode) {
            mFields.put(TransferMethodFields.POSTAL_CODE, postalCode);
            return this;
        }

        public PaperCheck.Builder stateProvince(@NonNull final String stateProvince) {
            mFields.put(TransferMethodFields.STATE_PROVINCE, stateProvince);
            return this;
        }
        public PaperCheck.Builder country(@NonNull final String country) {
            mFields.put(TransferMethodFields.COUNTRY, country);
            return this;
        }

        public PaperCheck build() {
            return new PaperCheck(mFields);
        }
    }
}
