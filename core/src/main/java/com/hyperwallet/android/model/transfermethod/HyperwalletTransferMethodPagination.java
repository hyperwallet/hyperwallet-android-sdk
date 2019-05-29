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

import static com.hyperwallet.android.model.HyperwalletStatusTransition.StatusDefinition.ACTIVATED;
import static com.hyperwallet.android.model.HyperwalletStatusTransition.StatusDefinition.DE_ACTIVATED;
import static com.hyperwallet.android.model.HyperwalletStatusTransition.StatusDefinition.INVALID;
import static com.hyperwallet.android.model.HyperwalletStatusTransition.StatusDefinition.VERIFIED;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodTypes.BANK_ACCOUNT;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodTypes.BANK_CARD;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodTypes.PAYPAL_ACCOUNT;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodTypes.WIRE_ACCOUNT;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringDef;

import com.hyperwallet.android.model.QueryParam;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Map;

/**
 * Represents the common pagination fields to the transfer methods
 */
public class HyperwalletTransferMethodPagination extends QueryParam {

    protected static final String TRANSFER_METHOD_TYPE = "type";
    protected static final String TRANSFER_METHODT_STATUS = "status";
    private String mStatus;
    private String mType;

    protected HyperwalletTransferMethodPagination(Builder builder) {
        super(builder);
        mStatus = builder.status;
        mType = builder.type;
    }

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            BANK_ACCOUNT,
            WIRE_ACCOUNT,
            BANK_CARD,
            PAYPAL_ACCOUNT
    })
    public @interface TransferMethodTypeQuery {
    }

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            ACTIVATED,
            DE_ACTIVATED,
            INVALID,
            VERIFIED,
    })
    public @interface TransferMethodStatusQuery {
    }

    /**
     * Constructors a Hyperwallet Transfer Method Pagination based on Map object
     *
     * @param urlQueryMap the URL query map with the specific parameters
     */
    public HyperwalletTransferMethodPagination(@NonNull Map<String, String> urlQueryMap) {
        super(urlQueryMap);

        if (containsKeyAndHasValue(urlQueryMap, TRANSFER_METHOD_TYPE)) {
            mType = urlQueryMap.get(TRANSFER_METHOD_TYPE);
        }

        if (containsKeyAndHasValue(urlQueryMap, TRANSFER_METHODT_STATUS)) {
            mStatus = urlQueryMap.get(TRANSFER_METHODT_STATUS);
        }
    }

    @Nullable
    public @TransferMethodTypeQuery
    String getType() {
        return mType;
    }

    @Nullable
    public @TransferMethodStatusQuery
    String getStatus() {
        return mStatus;
    }

    @NonNull
    @Override
    public Map<String, String> buildQuery() {
        Map<String, String> query = super.buildQuery();

        if (mType != null) {
            query.put(TRANSFER_METHOD_TYPE, mType);
        }

        if (mStatus != null) {
            query.put(TRANSFER_METHODT_STATUS, mStatus);
        }

        return query;
    }

    public static Builder<?, ?> builder() {
        return new Builder() {
            @Override
            public HyperwalletTransferMethodPagination build() {
                return new HyperwalletTransferMethodPagination(this);
            }
        };
    }

    /**
     * Builder Class for the {@link HyperwalletTransferMethodPagination}
     */
    public static abstract class Builder<S extends HyperwalletTransferMethodPagination, B extends Builder<S, B>> extends
            QueryParam.Builder<S, B> {
        private String status;
        private String type;

        /**
         * Specify status of this method. Which is one of the
         * {@link com.hyperwallet.android.model.HyperwalletStatusTransition.StatusDefinition}.
         *
         * @param status The status of this method
         * @return Builder
         */
        public B status(@NonNull @TransferMethodStatusQuery String status) {
            this.status = status;
            return (B) this;
        }

        /**
         * Specify type of this method. Which is one of the
         * {@link com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodTypes}.
         *
         * @param type The type of this method
         * @return Builder
         */
        public B type(@NonNull @TransferMethodTypeQuery String type) {
            this.type = type;
            return (B) this;
        }
    }
}
