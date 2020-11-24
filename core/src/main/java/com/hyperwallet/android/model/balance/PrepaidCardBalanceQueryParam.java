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
 * Represents the Prepaid card balance query params fields.
 */
public class PrepaidCardBalanceQueryParam extends QueryParam {

    /**
     * Construct a {@code PrepaidCardBalanceQueryParam} object
     * from {@link PrepaidCardBalanceQueryParam.Builder}
     */
    private PrepaidCardBalanceQueryParam(@NonNull final Builder builder) {
        super(builder);
    }

    @NonNull
    @Override
    public Map<String, String> buildQuery() {
        return super.buildQuery();
    }

    /**
     * Builder Class for the {@link PrepaidCardBalanceQueryParam}
     */
    public static class Builder extends QueryParam.Builder<Builder> {

        @Override
        public PrepaidCardBalanceQueryParam build() {
            return new PrepaidCardBalanceQueryParam(this);
        }

        /**
         * Specify the sort order with the Currency ascendant param
         * {@link PrepaidCardBalanceQueryParam.BalanceSortable#ASCENDANT_CURRENCY}.
         *
         * @return Builder
         */
        public Builder sortByCurrencyAsc() {
            sortBy(PrepaidCardBalanceQueryParam.BalanceSortable.ASCENDANT_CURRENCY);
            return self();
        }

        /**
         * Specify the sort order with the Currency descendant param
         * {@link PrepaidCardBalanceQueryParam.BalanceSortable#DESCENDANT_CURRENCY}.
         *
         * @return Builder
         */
        public Builder sortByCurrencyDesc() {
            sortBy(PrepaidCardBalanceQueryParam.BalanceSortable.DESCENDANT_CURRENCY);
            return self();
        }

        /**
         * Specify the sort order with the Amount ascendant param
         * {@link PrepaidCardBalanceQueryParam.BalanceSortable#ASCENDANT_AMOUNT}.
         *
         * @return Builder
         */
        public Builder sortByAmountAsc() {
            sortBy(PrepaidCardBalanceQueryParam.BalanceSortable.ASCENDANT_AMOUNT);
            return self();
        }

        /**
         * Specify the sort order with the Amount descendant param
         * {@link PrepaidCardBalanceQueryParam.BalanceSortable#DESCENDANT_AMOUNT}.
         *
         * @return Builder
         */
        public Builder sortByAmountDesc() {
            sortBy(PrepaidCardBalanceQueryParam.BalanceSortable.DESCENDANT_AMOUNT);
            return self();
        }

    }

    public final class BalanceSortable {
        public static final String ASCENDANT_AMOUNT = "+amount";
        public static final String ASCENDANT_CURRENCY = "+currency";
        public static final String DESCENDANT_AMOUNT = "-amount";
        public static final String DESCENDANT_CURRENCY = "-currency";

        private BalanceSortable() {

        }
    }
}