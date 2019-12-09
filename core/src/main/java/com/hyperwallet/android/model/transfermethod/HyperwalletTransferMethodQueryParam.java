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

import static com.hyperwallet.android.model.StatusTransition.StatusDefinition.ACTIVATED;
import static com.hyperwallet.android.model.StatusTransition.StatusDefinition.DE_ACTIVATED;
import static com.hyperwallet.android.model.StatusTransition.StatusDefinition.INVALID;
import static com.hyperwallet.android.model.StatusTransition.StatusDefinition.VERIFIED;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodTypes.BANK_ACCOUNT;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodTypes.BANK_CARD;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodTypes.PAYPAL_ACCOUNT;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodTypes.PREPAID_CARD;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodTypes.WIRE_ACCOUNT;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringDef;

import com.hyperwallet.android.model.QueryParam;
import com.hyperwallet.android.model.StatusTransition;
import com.hyperwallet.android.util.DateUtil;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Date;
import java.util.Map;

/**
 * Represents the common query param fields to the transfer methods.
 */
public class HyperwalletTransferMethodQueryParam extends QueryParam {

    private static final String TRANSFER_METHOD_TYPE = "type";
    private static final String TRANSFER_METHOD_STATUS = "status";
    private static final String TRANSFER_METHOD_CREATED_ON = "createdOn";

    private Date mCreatedOn;
    private String mStatus;
    private String mType;

    /**
     * Construct a {@code HyperwalletTransferMethodQueryParam} object
     * from {@link HyperwalletTransferMethodQueryParam.Builder}
     */
    public HyperwalletTransferMethodQueryParam(@NonNull final HyperwalletTransferMethodBuilder builder) {
        super(builder);
        mStatus = builder.mStatus;
        mType = builder.mType;
        mCreatedOn = builder.mCreatedOn;
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

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            BANK_ACCOUNT,
            WIRE_ACCOUNT,
            BANK_CARD,
            PAYPAL_ACCOUNT,
            PREPAID_CARD
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

    /**
     * Builder Class for the {@link HyperwalletBankAccountQueryParam}
     */
    public static class Builder extends HyperwalletTransferMethodBuilder<Builder> {

        @Override
        public HyperwalletTransferMethodQueryParam build() {
            return new HyperwalletTransferMethodQueryParam(this);
        }
    }

    static class HyperwalletTransferMethodBuilder<B extends QueryParam.Builder> extends QueryParam.Builder<B> {

        Date mCreatedOn;
        String mStatus;
        String mType;

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
         * {@link StatusTransition.StatusDefinition}.
         *
         * @param status The status of this method
         * @return Builder
         */
        public B status(
                @NonNull @HyperwalletTransferMethodQueryParam.TransferMethodStatusQuery final String status) {
            mStatus = status;
            return self();
        }

        public B type(@NonNull @HyperwalletTransferMethodQueryParam.TransferMethodTypeQuery final String type) {
            mType = type;
            return self();
        }


        public B sortByCreatedOnAsc() {
            sortBy(HyperwalletTransferMethodQueryParam.TransferMethodSortable.ASCENDANT_CREATE_ON);
            return self();
        }

        /**
         * Specify the sort order with the Created date descendant param
         * {@link HyperwalletTransferMethodQueryParam.TransferMethodSortable#DESCENDANT_CREATE_ON}.
         *
         * @return Builder
         */
        public B sortByCreatedOnDesc() {
            sortBy(HyperwalletTransferMethodQueryParam.TransferMethodSortable.DESCENDANT_CREATE_ON);
            return self();
        }

        /**
         * Specify the sort order with the Status ascendant param
         * {@link HyperwalletTransferMethodQueryParam.TransferMethodSortable#ASCENDANT_STATUS}.
         *
         * @return Builder
         */
        public B sortByStatusAsc() {
            sortBy(HyperwalletTransferMethodQueryParam.TransferMethodSortable.ASCENDANT_STATUS);
            return self();
        }

        /**
         * Specify the sort order with the Status descendant param
         * {@link HyperwalletTransferMethodQueryParam.TransferMethodSortable#DESCENDANT_STATUS}.
         *
         * @return Builder
         */
        public B sortByStatusDesc() {
            sortBy(HyperwalletTransferMethodQueryParam.TransferMethodSortable.DESCENDANT_STATUS);
            return self();
        }

        /**
         * Specify the sort order with the Type ascendant param
         * {@link HyperwalletTransferMethodQueryParam.TransferMethodSortable#ASCENDANT_TYPE}.
         *
         * @return Builder
         */
        public B sortByTypeAsc() {
            sortBy(HyperwalletTransferMethodQueryParam.TransferMethodSortable.ASCENDANT_TYPE);
            return self();
        }

        /**
         * Specify the sort order with the Type descendant param
         * {@link HyperwalletTransferMethodQueryParam.TransferMethodSortable#DESCENDANT_TYPE}.
         *
         * @return Builder
         */
        public B sortByTypeDesc() {
            sortBy(HyperwalletTransferMethodQueryParam.TransferMethodSortable.DESCENDANT_TYPE);
            return self();
        }

    }

    public final class TransferMethodSortable {
        public static final String ASCENDANT_CREATE_ON = "+createdOn";
        public static final String ASCENDANT_STATUS = "+status";
        public static final String ASCENDANT_TYPE = "+type";
        public static final String DESCENDANT_CREATE_ON = "-createdOn";
        public static final String DESCENDANT_STATUS = "-status";
        public static final String DESCENDANT_TYPE = "-type";
    }
}
