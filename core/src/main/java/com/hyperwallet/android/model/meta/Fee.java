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

package com.hyperwallet.android.model.meta;

import androidx.annotation.NonNull;
import androidx.annotation.StringDef;
import androidx.annotation.VisibleForTesting;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Class for presenting Fee
 */
@Deprecated
public class Fee {

    private static final String COUNTRY = "country";
    private static final String CURRENCY = "currency";
    private static final String FEE_RATE_TYPE = "feeRateType";
    private static final String MINIMUM = "minimum";
    private static final String MAXIMUM = "maximum";
    private static final String TRANSFER_METHOD_TYPE = "transferMethodType";
    private static final String VALUE = "value";

    private final String mCountry;
    private final String mCurrency;
    private final String mTransferMethodType;

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            FeeRate.FLAT,
            FeeRate.PERCENT
    })
    public @interface FeeRateType {
    }

    public final class FeeRate {
        public static final String FLAT = "FLAT";
        public static final String PERCENT = "PERCENT";
    }

    private final @FeeRateType
    String mFeeRateType;
    private final String mValue;
    private final String mMinimum;
    private final String mMaximum;

    public Fee(@NonNull final JSONObject fee) throws JSONException {
        mTransferMethodType = fee.getString(TRANSFER_METHOD_TYPE);
        mCountry = fee.getString(COUNTRY);
        mCurrency = fee.getString(CURRENCY);
        mFeeRateType = fee.getString(FEE_RATE_TYPE);
        mValue = fee.getString(VALUE);
        mMinimum = fee.optString(MINIMUM);
        mMaximum = fee.optString(MAXIMUM);
    }

    @VisibleForTesting
    public Fee(final String transferMethodType,
            final String country,
            final String currency,
            final String feeRateType,
            final String value,
            final String minimum,
            final String maximum) {
        this.mTransferMethodType = transferMethodType;
        this.mCountry = country;
        this.mCurrency = currency;
        this.mFeeRateType = feeRateType;
        this.mValue = value;
        this.mMinimum = minimum;
        this.mMaximum = maximum;
    }

    public @FeeRateType
    String getFeeRateType() {
        return mFeeRateType;
    }

    public String getValue() {
        return mValue;
    }

    public String getMinimum() {
        return mMinimum;
    }

    public String getMaximum() {
        return mMaximum;
    }

    public String getTransferMethodType() {
        return mTransferMethodType;
    }

    public String getCountry() {
        return mCountry;
    }

    public String getCurrency() {
        return mCurrency;
    }
}
