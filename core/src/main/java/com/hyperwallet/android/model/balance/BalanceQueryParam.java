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
package com.hyperwallet.android.model.balance;

import androidx.annotation.NonNull;

import com.hyperwallet.android.model.QueryParam;

import java.util.Map;

/**
 * Represents the Balance query params fields.
 */
public class BalanceQueryParam extends QueryParam {

    private static final String CURRENCY = "currency";

    private final String mCurrency;

    /**
     * Construct a {@code BalanceQueryParam} object
     * from {@link BalanceQueryParam.Builder}
     */
    private BalanceQueryParam(@NonNull final Builder builder) {
        super(builder);
        mCurrency = builder.mCurrency;
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

    public String getCurrency() {
        return mCurrency;
    }

    /**
     * Builder Class for the {@link BalanceQueryParam}
     */
    public static class Builder extends QueryParam.Builder<Builder> {

        private String mCurrency;

        @Override
        public BalanceQueryParam build() {
            return new BalanceQueryParam(this);
        }

        /**
         * Specify the sort order with the Currency ascendant param
         * {@link BalanceQueryParam.BalanceSortable#ASCENDANT_CURRENCY}.
         *
         * @return Builder
         */
        public Builder sortByCurrencyAsc() {
            sortBy(BalanceQueryParam.BalanceSortable.ASCENDANT_CURRENCY);
            return self();
        }

        /**
         * Specify the sort order with the Currency descendant param
         * {@link BalanceQueryParam.BalanceSortable#DESCENDANT_CURRENCY}.
         *
         * @return Builder
         */
        public Builder sortByCurrencyDesc() {
            sortBy(BalanceQueryParam.BalanceSortable.DESCENDANT_CURRENCY);
            return self();
        }

        /**
         * Specify the sort order with the Amount ascendant param
         * {@link BalanceQueryParam.BalanceSortable#ASCENDANT_AMOUNT}.
         *
         * @return Builder
         */
        public Builder sortByAmountAsc() {
            sortBy(BalanceQueryParam.BalanceSortable.ASCENDANT_AMOUNT);
            return self();
        }

        /**
         * Specify the sort order with the Amount descendant param
         * {@link BalanceQueryParam.BalanceSortable#DESCENDANT_AMOUNT}.
         *
         * @return Builder
         */
        public Builder sortByAmountDesc() {
            sortBy(BalanceQueryParam.BalanceSortable.DESCENDANT_AMOUNT);
            return self();
        }

        /**
         * Define a currency.
         *
         * @return Builder
         */
        public Builder currency(@NonNull final String currency) {
            mCurrency = currency;
            return self();
        }
    }

    protected final class BalanceSortable {
        static final String ASCENDANT_AMOUNT = "+amount";
        static final String ASCENDANT_CURRENCY = "+currency";
        static final String DESCENDANT_AMOUNT = "-amount";
        static final String DESCENDANT_CURRENCY = "-currency";
    }
}