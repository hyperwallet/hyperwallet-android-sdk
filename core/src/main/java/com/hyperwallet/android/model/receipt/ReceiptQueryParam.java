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
 * AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE
 * USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 */

package com.hyperwallet.android.model.receipt;

import static com.hyperwallet.android.model.receipt.ReceiptQueryParam.ReceiptQueryParamFields.AMOUNT;
import static com.hyperwallet.android.model.receipt.ReceiptQueryParam.ReceiptQueryParamFields.CREATED_ON;
import static com.hyperwallet.android.model.receipt.ReceiptQueryParam.ReceiptQueryParamFields.CURRENCY;
import static com.hyperwallet.android.model.receipt.ReceiptQueryParam.ReceiptQueryParamFields.TYPE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hyperwallet.android.model.QueryParam;
import com.hyperwallet.android.util.DateUtil;

import java.util.Date;
import java.util.Map;

/**
 * Represents the query parameters for getting list of receipts {@link Receipt}
 */
public class ReceiptQueryParam extends QueryParam {

    /**
     * Common Receipt query parameters
     */
    public static final class ReceiptQueryParamFields {
        static final String TYPE = "type";
        static final String AMOUNT = "amount";
        static final String CURRENCY = "currency";
        static final String CREATED_ON = "createdOn";
    }

    private final Date mCreatedOn;
    private final String mType;
    private final String mAmount;
    private final String mCurrency;

    /**
     * Constructors the receipt query params.
     */
    public ReceiptQueryParam(@NonNull final Builder builder) {
        super(builder);
        this.mCreatedOn = builder.mCreatedOn;
        this.mType = builder.mType;
        this.mAmount = builder.mAmount;
        this.mCurrency = builder.mCurrency;
    }

    public @interface ReceiptSortableQuery {
    }

    public final class ReceiptSortable {
        private ReceiptSortable() {
        }

        public static final String ASCENDANT_CREATE_ON = "+createdOn";
        public static final String ASCENDANT_AMOUNT = "+amount";
        public static final String ASCENDANT_TYPE = "+type";
        public static final String ASCENDANT_CURRENCY = "+currency";
        public static final String DESCENDANT_CREATE_ON = "-createdOn";
        public static final String DESCENDANT_AMOUNT = "-amount";
        public static final String DESCENDANT_TYPE = "-type";
        public static final String DESCENDANT_CURRENCY = "-currency";
    }

    @Nullable
    public Date getCreatedOn() {
        return mCreatedOn;
    }

    @Nullable
    @Receipt.ReceiptType
    public String getType() {
        return mType;
    }

    public String getAmount() {
        return mAmount;
    }

    public String getCurrency() {
        return mCurrency;
    }

    @NonNull
    @Override
    public Map<String, String> buildQuery() {
        Map<String, String> query = super.buildQuery();
        if (mCreatedOn != null) {
            query.put(CREATED_ON, DateUtil.toDateTimeFormat(mCreatedOn));
        }
        if (mAmount != null) {
            query.put(AMOUNT, mAmount);
        }
        if (mCurrency != null) {
            query.put(CURRENCY, mCurrency);
        }
        if (mType != null) {
            query.put(TYPE, mType);
        }
        return query;
    }

    /**
     * Builder Class for the {@link ReceiptQueryParam}
     */
    public static class Builder extends QueryParam.Builder<Builder> {
        private Date mCreatedOn;
        private String mType;
        private String mAmount;
        private String mCurrency;

        /**
         * Define a Date created on.
         *
         * @param createdOn Date
         * @return Builder
         */
        public Builder createdOn(@NonNull final Date createdOn) {
            mCreatedOn = new Date(createdOn.getTime());
            return this;
        }

        /**
         * Specify TYPE of this method. Which is one of the
         * {@link Receipt.ReceiptType}.
         *
         * @param type The type of receipts
         * @return Builder
         */
        public Builder type(@NonNull @Receipt.ReceiptType final String type) {
            mType = type;
            return this;
        }

        /**
         * Define an Amount.
         *
         * @param amount the amount
         * @return Builder
         */
        public Builder amount(@NonNull final String amount) {
            mAmount = amount;
            return this;
        }

        /**
         * Define an Currency filter
         *
         * @param currency The currency of receipts
         * @return Builder
         */
        public Builder currency(@NonNull final String currency) {
            mCurrency = currency;
            return this;
        }

        @Override
        public ReceiptQueryParam build() {
            return new ReceiptQueryParam(this);
        }

        /**
         * Specify the sort order with the Amount ascendant param {@link ReceiptSortable#ASCENDANT_AMOUNT}.
         *
         * @return Builder
         */
        public Builder sortByAmountAsc() {
            mSortBy = ReceiptSortable.ASCENDANT_AMOUNT;
            return this;
        }

        /**
         * Specify the sort order with the Amount descendant param {@link ReceiptSortable#DESCENDANT_AMOUNT}.
         *
         * @return Builder
         */
        public Builder sortByAmountDesc() {
            mSortBy = ReceiptSortable.DESCENDANT_AMOUNT;
            return this;
        }


        /**
         * Specify the sort order with the transaction type ascendant param {@link ReceiptSortable#ASCENDANT_TYPE}.
         *
         * @return Builder
         */
        public Builder sortByTypeAsc() {
            mSortBy = ReceiptSortable.ASCENDANT_TYPE;
            return this;
        }

        /**
         * Specify the sort order with the transaction type descendant param {@link ReceiptSortable#DESCENDANT_TYPE}.
         *
         * @return Builder
         */
        public Builder sortByTypeDesc() {
            mSortBy = ReceiptSortable.DESCENDANT_TYPE;
            return this;
        }


        /**
         * Specify the sort order with the transaction type ascendant param {@link ReceiptSortable#ASCENDANT_CURRENCY}.
         *
         * @return Builder
         */
        public Builder sortByCurrencyAsc() {
            mSortBy = ReceiptSortable.ASCENDANT_CURRENCY;
            return this;
        }

        /**
         * Specify the sort order with the currency descendant param {@link ReceiptSortable#DESCENDANT_CURRENCY}.
         *
         * @return Builder
         */
        public Builder sortByCurrencyDesc() {
            mSortBy = ReceiptSortable.DESCENDANT_CURRENCY;
            return this;
        }


        /**
         * Specify the sort order with the created on ascendant param {@link ReceiptSortable#ASCENDANT_CREATE_ON}.
         *
         * @return Builder
         */
        public Builder sortByCreatedOnAsc() {
            mSortBy = ReceiptSortable.ASCENDANT_CREATE_ON;
            return this;
        }

        /**
         * Specify the sort order with the created on descendant param {@link ReceiptSortable#DESCENDANT_CREATE_ON}.
         *
         * @return Builder
         */
        public Builder sortByCreatedOnDesc() {
            mSortBy = ReceiptSortable.DESCENDANT_CREATE_ON;
            return this;
        }
    }
}
