/*
 *  The MIT License (MIT)
 *  Copyright (c) 2018 Hyperwallet Systems Inc.
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 *  associated documentation files (the "Software"), to deal in the Software without restriction,
 *  including without limitation the rights to use, copy, modify, merge, publish, distribute,
 *  sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 *  NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 *  NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 *  DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.hyperwallet.android.model.graphql;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hyperwallet.android.model.transfermethod.TransferMethod;

import org.json.JSONObject;

import java.util.Objects;

/**
 * Holds basic Processing time information.
 */
public final class ProcessingTime {

    private static final String COUNTRY = "country";
    private static final String CURRENCY = "currency";
    private static final String TRANSFER_METHOD_TYPE = "transferMethodType";
    private static final String VALUE = "value";
    private final String mCountry;
    private final String mCurrency;
    private final String mTransferMethodType;
    private final String mValue;

    /**
     * Construct a {@code ProcessingTime} object from {@link JSONObject} representation
     *
     * @param jsonObject raw data representation
     */
    public ProcessingTime(@NonNull final JSONObject jsonObject) {
        mCountry = jsonObject.optString(COUNTRY);
        mCurrency = jsonObject.optString(CURRENCY);
        mTransferMethodType = jsonObject.optString(TRANSFER_METHOD_TYPE);
        mValue = jsonObject.optString(VALUE);
    }

    /**
     * @return Country information
     */
    @Nullable
    public String getCountry() {
        return mCountry;
    }

    /**
     * @return Currency information
     */
    @Nullable
    public String getCurrency() {
        return mCurrency;
    }

    /**
     * @return Transfer method type information
     */
    @Nullable
    @TransferMethod.TransferMethodType
    public String getTransferMethodType() {
        return mTransferMethodType;
    }

    /**
     * @return Processing time value information
     */
    @Nullable
    public String getValue() {
        return mValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProcessingTime that = (ProcessingTime) o;

        return Objects.equals(mCountry, that.mCountry)
                && Objects.equals(mCurrency, that.mCurrency)
                && Objects.equals(mTransferMethodType, that.mTransferMethodType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mCountry, mCurrency, mTransferMethodType);
    }

}
