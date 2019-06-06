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
import com.hyperwallet.android.util.DateUtil;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Date;
import java.util.Map;

/**
 * Represents the common query param fields to the transfer methods.
 */
public class HyperwalletTransferMethodQueryParam extends QueryParam {

    public static final String TRANSFER_METHOD_TYPE = "type";
    public static final String TRANSFER_METHOD_STATUS = "status";
    public static final String TRANSFER_METHOD_CREATED_ON = "createdOn";

    private Date mCreatedOn;
    private String mStatus;
    private String mType;

    /**
     * Constructors the QueryParam
     */
    public HyperwalletTransferMethodQueryParam(@NonNull final Builder builder) {
        super(builder);
        mStatus = builder.mStatus;
        mType = builder.mType;
        mCreatedOn = builder.mCreatedOn;
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

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            TransferMethodSortable.ASCENDANT_CREATE_ON,
            TransferMethodSortable.ASCENDANT_STATUS,
            TransferMethodSortable.DESCENDANT_CREATE_ON,
            TransferMethodSortable.DESCENDANT_STATUS
    })
    public @interface TransferMethodSortableQuery {
    }

    public final class TransferMethodSortable {
        public static final String ASCENDANT_CREATE_ON = "+createdOn";
        public static final String ASCENDANT_STATUS = "+status";
        public static final String ASCENDANT_TYPE = "+type";
        public static final String DESCENDANT_CREATE_ON = "-createdOn";
        public static final String DESCENDANT_STATUS = "-status";
        public static final String DESCENDANT_TYPE = "-type";
    }


    @NonNull
    @Override
    public Map<String, String> buildQuery() {
        Map<String, String> query = super.buildQuery();

        if (mStatus != null) {
            query.put(TRANSFER_METHOD_STATUS, mStatus);
        }
        if (mType != null) {
            query.put(TRANSFER_METHOD_TYPE, mType);
        }
        if (mCreatedOn != null) {
            query.put(TRANSFER_METHOD_CREATED_ON, DateUtil.toDateTimeFormat(mCreatedOn));
        }
        return query;
    }

    @Nullable
    @HyperwalletTransferMethodQueryParam.TransferMethodStatusQuery
    public String getStatus() {
        return mStatus;
    }

    @Nullable
    @HyperwalletTransferMethodQueryParam.TransferMethodTypeQuery
    public String getType() {
        return mType;
    }

    @Nullable
    public Date getCreatedOn() {
        return mCreatedOn;
    }

    public static class Builder<B extends QueryParam.Builder> extends QueryParam.Builder<B> {

        private Date mCreatedOn;
        private String mStatus;
        private String mType;


        /**
         * Define a Date created on.
         *
         * @param createdOn Date
         * @return Builder
         */

        public B createdOn(@NonNull final Date createdOn) {
            mCreatedOn = new Date(createdOn.getTime());
            return self();
        }

        /**
         * Specify status of this method. Which is one of the
         * {@link com.hyperwallet.android.model.HyperwalletStatusTransition.StatusDefinition}.
         *
         * @param status The status of this method
         * @return Builder
         */
        public B status(
                @NonNull @HyperwalletTransferMethodQueryParam.TransferMethodStatusQuery final String status) {
            mStatus = status;
            return self();
        }

        protected B type(@NonNull @HyperwalletTransferMethodQueryParam.TransferMethodTypeQuery final String type) {
            mType = type;
            return self();
        }


        public B sortByCreatedOnAsc() {
            mSortBy = TransferMethodSortable.ASCENDANT_CREATE_ON;
            return self();
        }

        /**
         * Specify the sort order with the Created date descendant param
         * {@link TransferMethodSortable#DESCENDANT_CREATE_ON}.
         *
         * @return Builder
         */
        public B sortByCreatedOnDesc() {
            mSortBy = TransferMethodSortable.DESCENDANT_CREATE_ON;
            return self();
        }

        /**
         * Specify the sort order with the Status ascendant param {@link TransferMethodSortable#ASCENDANT_STATUS}.
         *
         * @return Builder
         */
        public B sortByStatusAsc() {
            mSortBy = TransferMethodSortable.ASCENDANT_STATUS;
            return self();
        }

        /**
         * Specify the sort order with the Status descendant param {@link TransferMethodSortable#DESCENDANT_STATUS}.
         *
         * @return Builder
         */
        public B sortByStatusDesc() {
            mSortBy = TransferMethodSortable.DESCENDANT_STATUS;
            return self();
        }

        /**
         * Specify the sort order with the Type ascendant param {@link TransferMethodSortable#ASCENDANT_TYPE}.
         *
         * @return Builder
         */
        public B sortByTypeAsc() {
            mSortBy = TransferMethodSortable.ASCENDANT_TYPE;
            return self();
        }

        /**
         * Specify the sort order with the Type descendant param {@link TransferMethodSortable#DESCENDANT_TYPE}.
         *
         * @return Builder
         */
        public B sortByTypeDesc() {
            mSortBy = TransferMethodSortable.DESCENDANT_TYPE;
            return self();
        }


        @Override
        public HyperwalletTransferMethodQueryParam build() {
            return new HyperwalletTransferMethodQueryParam(this);
        }


    }
}
