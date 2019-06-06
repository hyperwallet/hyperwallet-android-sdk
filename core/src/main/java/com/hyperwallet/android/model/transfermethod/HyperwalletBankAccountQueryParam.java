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

import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodTypes.BANK_ACCOUNT;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethodQueryParam.TRANSFER_METHODT_STATUS;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethodQueryParam.TRANSFER_METHOD_TYPE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hyperwallet.android.model.QueryParam;

import java.util.Map;

/**
 * Represents the bank account query params fields.
 */
public class HyperwalletBankAccountQueryParam extends QueryParam {

    private final String mStatus;
    private final String mType;

    private HyperwalletBankAccountQueryParam(@NonNull final Builder builder) {
        super(builder);
        mStatus = builder.mStatus;
        mType = builder.mType != null ? builder.mType : BANK_ACCOUNT;
    }

    @NonNull
    @Override
    public Map<String, String> buildQuery() {
        Map<String, String> query = super.buildQuery();

        if (mStatus != null) {
            query.put(TRANSFER_METHODT_STATUS, mStatus);
        }

        query.put(TRANSFER_METHOD_TYPE, mType);

        return query;
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

    /**
     * Builder Class for the {@link HyperwalletBankAccountQueryParam}
     */
    public static class Builder extends QueryParam.Builder<Builder> {
        private String mStatus;
        private String mType;

        /**
         * Specify status of this method. Which is one of the
         * {@link com.hyperwallet.android.model.HyperwalletStatusTransition.StatusDefinition}.
         *
         * @param status The status of this method
         * @return Builder
         */
        public Builder status(
                @NonNull @HyperwalletTransferMethodQueryParam.TransferMethodStatusQuery final String status) {
            mStatus = status;
            return this;
        }

        /**
         * Specify type of this method. Which is one of the
         * {@link com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodTypes}.
         *
         * @param type The type of this method
         * @return Builder
         */
        public Builder type(@NonNull @HyperwalletTransferMethodQueryParam.TransferMethodTypeQuery final String type) {
            mType = type;
            return this;
        }

        @Override
        public HyperwalletBankAccountQueryParam build() {
            return new HyperwalletBankAccountQueryParam(this);
        }
    }
}
