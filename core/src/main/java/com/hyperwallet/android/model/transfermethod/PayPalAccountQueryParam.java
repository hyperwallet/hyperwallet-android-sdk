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

import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodTypes.PAYPAL_ACCOUNT;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethodQueryParam.TRANSFER_METHOD_TYPE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hyperwallet.android.model.QueryParam;
import com.hyperwallet.android.util.DateUtil;

import java.util.Date;
import java.util.Map;

/**
 * Represents the PayPal Account query param fields.
 */
public class PayPalAccountQueryParam extends QueryParam {

    protected static final String TRANSFER_METHOD_CREATE_ON = "createdOn";
    protected static final String TRANSFER_METHODT_STATUS = "status";
    private String mStatus;
    private String mType;

    private final Date mCreatedOn;

    /**
     * Constructs the default implementation of the PayPal Account query param.
     */
    private PayPalAccountQueryParam(@NonNull Builder builder) {
        super(builder);
        mCreatedOn = builder.mCreatedOn;
        mStatus = builder.mStatus;
        mType = PAYPAL_ACCOUNT;
    }

    @Nullable
    public Date getCreatedOn() {
        return mCreatedOn;
    }

    @Nullable
    public @HyperwalletTransferMethodQueryParam.TransferMethodStatusQuery
    String getStatus() {
        return mStatus;
    }

    @Nullable
    public @HyperwalletTransferMethodQueryParam.TransferMethodTypeQuery
    String getType() {
        return mType;
    }

    @NonNull
    @Override
    public Map<String, String> buildQuery() {
        Map<String, String> query = super.buildQuery();
        if (mCreatedOn != null) {
            query.put(TRANSFER_METHOD_CREATE_ON, DateUtil.toDateTimeFormat(mCreatedOn));
        }
        if (mStatus != null) {
            query.put(TRANSFER_METHODT_STATUS, mStatus);
        }

        query.put(TRANSFER_METHOD_TYPE, PAYPAL_ACCOUNT);

        return query;
    }

    /**
     * Builder Class for the {@link HyperwalletBankAccountQueryParam}
     */
    public static class Builder extends QueryParam.Builder<Builder> {
        private Date mCreatedOn;
        private String mStatus;

        /**
         * Define a Date created on.
         *
         * @param createdOn Date
         * @return Builder
         */
        public Builder createdOn(@NonNull Date createdOn) {
            mCreatedOn = new Date(createdOn.getTime());
            return this;
        }

        /**
         * Specify status of this method. Which is one of the
         * {@link com.hyperwallet.android.model.HyperwalletStatusTransition.StatusDefinition}.
         *
         * @param status The status of this method
         * @return Builder
         */
        public Builder status(@NonNull @HyperwalletTransferMethodQueryParam.TransferMethodStatusQuery String status) {
            mStatus = status;
            return this;
        }

        @Override
        public PayPalAccountQueryParam build() {
            return new PayPalAccountQueryParam(this);
        }

    }
}