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

import static com.hyperwallet.android.model.QueryParam.Sortable.ASCENDANT_AMOUNT;
import static com.hyperwallet.android.model.QueryParam.Sortable.ASCENDANT_CREATE_ON;
import static com.hyperwallet.android.model.QueryParam.Sortable.ASCENDANT_CURRENCY;
import static com.hyperwallet.android.model.QueryParam.Sortable.ASCENDANT_TYPE;
import static com.hyperwallet.android.model.QueryParam.Sortable.DESCENDANT_AMOUNT;
import static com.hyperwallet.android.model.QueryParam.Sortable.DESCENDANT_CREATE_ON;
import static com.hyperwallet.android.model.QueryParam.Sortable.DESCENDANT_CURRENCY;
import static com.hyperwallet.android.model.QueryParam.Sortable.DESCENDANT_TYPE;
import static com.hyperwallet.android.model.receipt.ReceiptQueryParam.ReceiptQueryParamFields.AMOUNT;
import static com.hyperwallet.android.model.receipt.ReceiptQueryParam.ReceiptQueryParamFields.CREATED_ON;
import static com.hyperwallet.android.model.receipt.ReceiptQueryParam.ReceiptQueryParamFields.CURRENCY;
import static com.hyperwallet.android.model.receipt.ReceiptQueryParam.ReceiptQueryParamFields.TYPE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hyperwallet.android.model.QueryParam;
import com.hyperwallet.android.model.transfermethod.HyperwalletBankAccountPagination;
import com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethodPagination;
import com.hyperwallet.android.util.DateUtil;

import java.util.Date;
import java.util.Map;

/**
 * Represents the query parameters for getting list of receipts {@link Receipt}
 */
public class ReceiptQueryParam extends QueryParam {

    /**
     * Common Receipt detail field keys
     */
    public final class ReceiptQueryParamFields {
        static final String TYPE = "type";
        static final String AMOUNT = "amount";
        static final String CURRENCY = "currency";
        static final String CREATED_ON = "createdOn";
    }

    private final Date mCreatedOn;
    private final String mType;
    private final String mAmount;
    private final String mCurrency;

    public ReceiptQueryParam(@NonNull final Map<String, String> fields) {
        super(fields);
        mCreatedOn = getDateValueBy(fields, CREATED_ON);
        mType = containsKeyAndHasValue(fields, TYPE) ? fields.get(TYPE) : null;
        mAmount = containsKeyAndHasValue(fields, AMOUNT) ? fields.get(AMOUNT) : null;
        mCurrency = containsKeyAndHasValue(fields, CURRENCY) ? fields.get(CURRENCY) : null;
    }

    public ReceiptQueryParam(@NonNull final Builder builder) {
        super(builder);
        this.mCreatedOn = builder.mCreatedOn;
        this.mType = builder.mType;
        this.mAmount = builder.mAmount;
        this.mCurrency = builder.mCurrency;
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


    public static Builder<?> builder() {
        return new Builder();
    }

    public static class Builder<B extends Builder<B>> extends QueryParam.Builder<B> {

        private Date mCreatedOn;
        private String mType;
        private String mAmount;
        private String mCurrency;

        public Builder createdOn(@NonNull final Date createdOn) {
            mCreatedOn = createdOn;
            return this;
        }

        public Builder type(@NonNull @Receipt.ReceiptType final String type) {
            mType = type;
            return this;
        }

        public Builder amount(@NonNull final String amount) {
            mAmount = amount;
            return this;
        }

        public Builder currency(@NonNull final String currency) {
            mCurrency = currency;
            return this;
        }

        public Builder sortByCreatedOnAsc() {
            mSortBy = ASCENDANT_CREATE_ON;
            return this;
        }

        public Builder sortByCreatedOnDesc() {
            mSortBy = DESCENDANT_CREATE_ON;
            return this;
        }

        public Builder sortByTypeAsc() {
            mSortBy = ASCENDANT_TYPE;
            return this;
        }

        public Builder sortByTypeDesc() {
            mSortBy = DESCENDANT_TYPE;
            return this;
        }

        public Builder sortByAmountAsc() {
            mSortBy = ASCENDANT_AMOUNT;
            return this;
        }

        public Builder sortByAmountDesc() {
            mSortBy = DESCENDANT_AMOUNT;
            return this;
        }

        public Builder sortByCurrencyAsc() {
            mSortBy = ASCENDANT_CURRENCY;
            return this;
        }

        public Builder sortByCurrencyDesc() {
            mSortBy = DESCENDANT_CURRENCY;
            return this;
        }

        @Override
        public ReceiptQueryParam build() {
            return new ReceiptQueryParam(this);
        }
    }

}
