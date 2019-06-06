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


import static com.hyperwallet.android.model.receipt.ReceiptQueryParam.ReceiptQueryParamFields.CURRENCY;

import androidx.annotation.NonNull;

import com.hyperwallet.android.model.QueryParam;

import java.util.Map;

/**
 * Represents the query parameters for getting list of receipts {@link Receipt}
 */
public class ReceiptQueryParam extends QueryParam {

    private final String mCurrency;

    /**
     * Constructors the receipt query params.
     */
    public ReceiptQueryParam(@NonNull final Builder builder) {
        super(builder);
        this.mCurrency = builder.mCurrency;
    }

    public String getCurrency() {
        return mCurrency;
    }

    @NonNull
    @Override
    public Map<String, String> buildQuery() {
        Map<String, String> query = super.buildQuery();
        if (mCurrency != null) {
            query.put(CURRENCY, mCurrency);
        }
        return query;
    }

    public final class ReceiptSortable {
        public static final String ASCENDANT_CREATE_ON = "+createdOn";
        public static final String ASCENDANT_AMOUNT = "+amount";
        public static final String ASCENDANT_TYPE = "+type";
        public static final String ASCENDANT_CURRENCY = "+currency";
        public static final String DESCENDANT_CREATE_ON = "-createdOn";
        public static final String DESCENDANT_AMOUNT = "-amount";
        public static final String DESCENDANT_TYPE = "-type";
        public static final String DESCENDANT_CURRENCY = "-currency";

        private ReceiptSortable() {
        }
    }

    public @interface ReceiptSortableQuery {
    }


    /**
     * Common Receipt query parameters
     */
    public static final class ReceiptQueryParamFields {
        static final String CURRENCY = "currency";

    }

    /**
     * Builder Class for the {@link ReceiptQueryParam}
     */
    public static class Builder extends QueryParam.Builder<Builder> {

        private String mCurrency;

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
