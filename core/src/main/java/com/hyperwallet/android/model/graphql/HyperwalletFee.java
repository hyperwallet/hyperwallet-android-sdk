/*
 *  The MIT License (MIT)
 *  Copyright (c) 2019 Hyperwallet Systems Inc.
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 *  associated documentation files (the "Software"), to deal in the Software without restriction,
 *  including without limitation the rights to use, copy, modify, merge, publish, distribute,
 *  sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 *  NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 *  NON INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 *  DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.hyperwallet.android.model.graphql;

import androidx.annotation.NonNull;
import androidx.annotation.StringDef;

import org.json.JSONObject;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Objects;

/**
 * Represents Hyperwallet Fees
 */
public class HyperwalletFee {

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

    private static final String COUNTRY = "country";
    private static final String CURRENCY = "currency";
    private static final String FEE_RATE_TYPE = "feeRateType";
    private static final String ID_TOKEN = "idToken";
    private static final String MAX = "maximum";
    private static final String MIN = "minimum";
    private static final String TRANSFER_METHOD_TYPE = "transferMethodType";
    private static final String FEE_VALUE = "value";

    private String mCountry;
    private String mCurrency;
    private @FeeRateType
    String mFeeRateType;
    private String mIdToken;
    private String mMax;
    private String mMin;
    private String mTransferMethodType;
    private String mValue;

    public HyperwalletFee(@NonNull final JSONObject fee) {
        mCountry = fee.optString(COUNTRY);
        mCurrency = fee.optString(CURRENCY);
        mFeeRateType = fee.optString(FEE_RATE_TYPE);
        mIdToken = fee.optString(ID_TOKEN);
        mMax = fee.optString(MAX);
        mMin = fee.optString(MIN);
        mTransferMethodType = fee.optString(TRANSFER_METHOD_TYPE);
        mValue = fee.optString(FEE_VALUE);
    }

    public String getCountry() {
        return mCountry;
    }

    public String getCurrency() {
        return mCurrency;
    }

    public @FeeRateType
    String getFeeRateType() {
        return mFeeRateType;
    }

    public String getIdToken() {
        return mIdToken;
    }

    public String getMax() {
        return mMax;
    }

    public String getMin() {
        return mMin;
    }

    public String getTransferMethodType() {
        return mTransferMethodType;
    }

    public String getValue() {
        return mValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HyperwalletFee)) return false;
        HyperwalletFee that = (HyperwalletFee) o;
        return Objects.equals(getCountry(), that.getCountry()) &&
                Objects.equals(getCurrency(), that.getCurrency()) &&
                Objects.equals(getFeeRateType(), that.getFeeRateType()) &&
                Objects.equals(getIdToken(), that.getIdToken()) &&
                Objects.equals(getMax(), that.getMax()) &&
                Objects.equals(getMin(), that.getMin()) &&
                Objects.equals(getTransferMethodType(), that.getTransferMethodType()) &&
                Objects.equals(getValue(), that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCountry(), getCurrency(), getFeeRateType(), getIdToken(), getMax(), getMin(),
                getTransferMethodType(), getValue());
    }
}
