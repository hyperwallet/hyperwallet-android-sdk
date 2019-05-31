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
import static com.hyperwallet.android.model.receipt.ReceiptQueryParam.ReceiptQueryParamFields.CREATED_AFTER;
import static com.hyperwallet.android.model.receipt.ReceiptQueryParam.ReceiptQueryParamFields.CREATED_BEFORE;
import static com.hyperwallet.android.model.receipt.ReceiptQueryParam.ReceiptQueryParamFields.CREATED_ON;
import static com.hyperwallet.android.model.receipt.ReceiptQueryParam.ReceiptQueryParamFields.CURRENCY;
import static com.hyperwallet.android.model.receipt.ReceiptQueryParam.ReceiptQueryParamFields.LIMIT;
import static com.hyperwallet.android.model.receipt.ReceiptQueryParam.ReceiptQueryParamFields.OFFSET;
import static com.hyperwallet.android.model.receipt.ReceiptQueryParam.ReceiptQueryParamFields.SORT_BY;
import static com.hyperwallet.android.model.receipt.ReceiptQueryParam.ReceiptQueryParamFields.TYPE;
import static com.hyperwallet.android.model.receipt.ReceiptQueryParam.ReceiptSortables.ASCENDANT_AMOUNT;
import static com.hyperwallet.android.model.receipt.ReceiptQueryParam.ReceiptSortables.ASCENDANT_CREATE_ON;
import static com.hyperwallet.android.model.receipt.ReceiptQueryParam.ReceiptSortables.ASCENDANT_CURRENCY;
import static com.hyperwallet.android.model.receipt.ReceiptQueryParam.ReceiptSortables.ASCENDANT_TYPE;
import static com.hyperwallet.android.model.receipt.ReceiptQueryParam.ReceiptSortables.DESCENDANT_AMOUNT;
import static com.hyperwallet.android.model.receipt.ReceiptQueryParam.ReceiptSortables.DESCENDANT_CREATE_ON;
import static com.hyperwallet.android.model.receipt.ReceiptQueryParam.ReceiptSortables.DESCENDANT_CURRENCY;
import static com.hyperwallet.android.model.receipt.ReceiptQueryParam.ReceiptSortables.DESCENDANT_TYPE;
import static com.hyperwallet.android.util.DateUtil.fromDateTimeString;
import static com.hyperwallet.android.util.DateUtil.toDateTimeFormat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringDef;

import com.hyperwallet.android.model.HyperwalletPagination;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents the query parameters for getting list of receipts {@link Receipt}
 */
public class ReceiptQueryParam extends HyperwalletPagination {

    /**
     * Common Receipt detail field keys
     */
    public final class ReceiptQueryParamFields {
        public static final String LIMIT = "limit";
        public static final String OFFSET = "offset";
        public static final String CREATED_ON = "createdOn";
        public static final String CREATED_BEFORE = "createdBefore";
        public static final String CREATED_AFTER = "createdAfter";
        public static final String TYPE = "type";
        public static final String AMOUNT = "amount";
        public static final String CURRENCY = "currency";
        public static final String SORT_BY = "sortBy";
    }

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            OFFSET,
            LIMIT,
            CREATED_ON,
            CREATED_BEFORE,
            CREATED_AFTER,
            TYPE,
            AMOUNT,
            CURRENCY,
            SORT_BY
    })
    public @interface ReceiptQueryParamField {
    }

    public final class ReceiptSortables {
        public static final String ASCENDANT_CREATE_ON = "+createdOn";
        public static final String ASCENDANT_TYPE = "+type";
        public static final String ASCENDANT_AMOUNT = "+amount";
        public static final String ASCENDANT_CURRENCY = "+currency";
        public static final String DESCENDANT_CREATE_ON = "-createdOn";
        public static final String DESCENDANT_TYPE = "-type";
        public static final String DESCENDANT_AMOUNT = "-amount";
        public static final String DESCENDANT_CURRENCY = "-currency";
    }

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            ASCENDANT_CREATE_ON,
            ASCENDANT_TYPE,
            ASCENDANT_AMOUNT,
            ASCENDANT_CURRENCY,
            DESCENDANT_CREATE_ON,
            DESCENDANT_TYPE,
            DESCENDANT_AMOUNT,
            DESCENDANT_CURRENCY
    })
    public @interface ReceiptSortable {
    }

    // map should be protected
    private Map<String, String> mFields;
    private final Date mCreatedOn;
    private final Date mCreatedAfter;
    private final Date mCreatedBefore;
    private final String mType;
    private final String mAmount;
    private final String mCurrency;

    public ReceiptQueryParam(@NonNull final Map<String, String> fields) {
        super(fields);
        mFields = fields;
        mCreatedOn = getDateValueBy(fields, CREATED_ON);
        mCreatedAfter = getDateValueBy(fields, CREATED_AFTER);
        mCreatedBefore = getDateValueBy(fields, CREATED_BEFORE);

        if (containsKeyAndHasValue(fields, TYPE)) {
            mType = fields.get(TYPE);
        } else {
            mType = null;
        }
        if (containsKeyAndHasValue(fields, AMOUNT)) {
            mAmount = fields.get(AMOUNT);
        } else {
            mAmount = null;
        }

        if (containsKeyAndHasValue(fields, CURRENCY)) {
            mCurrency = fields.get(CURRENCY);
        } else {
            mCurrency = null;
        }
    }

    @Nullable
    public Date getCreatedOn() {
        return mCreatedOn;
    }

    @Nullable
    public Date getCreatedBefore() {
        return mCreatedBefore;
    }

    @Nullable
    public Date getCreatedAfter() {
        return mCreatedAfter;
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

    /**
     * Returns the valid Date type or null in case the content is invalid
     *
     * @param fields   the URL query map object
     * @param queryKey the key to get the object in the query map
     * @return the valid Date value or null
     */
    @Nullable
    Date getDateValueBy(@NonNull Map<String, String> fields, @NonNull String queryKey) {
        if (containsKeyAndHasValue(fields, queryKey)) {
            return fromDateTimeString(fields.get(queryKey));
        }
        return null;
    }

    @NonNull
    @Override
    public Map<String, String> buildQuery() {
        return mFields;
    }

    public static class Builder {
        Map<String, String> mFields = new HashMap<>();

        public Builder() {
            mFields.put(LIMIT, String.valueOf(DEFAULT_LIMIT));
            mFields.put(OFFSET, String.valueOf(DEFAULT_OFFSET));
        }

        public Builder createdOn(@NonNull final Date createdOn) {
            mFields.put(CREATED_ON, toDateTimeFormat(createdOn));
            return this;
        }

        public Builder createdBefore(@NonNull final Date createdBefore) {
            mFields.put(CREATED_BEFORE, toDateTimeFormat(createdBefore));
            return this;
        }

        public Builder createdAfter(@NonNull final Date createdAfter) {
            mFields.put(CREATED_AFTER, toDateTimeFormat(createdAfter));
            return this;
        }

        public Builder type(@NonNull @Receipt.ReceiptType final String type) {
            mFields.put(TYPE, type);
            return this;
        }

        public Builder amount(@NonNull final String amount) {
            mFields.put(AMOUNT, amount);
            return this;
        }

        public Builder currency(@NonNull final String currency) {
            mFields.put(CURRENCY, currency);
            return this;
        }

        public Builder limit(final int limit) {
            mFields.put(LIMIT, String.valueOf(limit));
            return this;
        }

        public Builder offset(final int offset) {
            mFields.put(OFFSET, String.valueOf(offset));
            return this;
        }

        public Builder sortByCreatedOnAsc() {
            mFields.put(SORT_BY, ASCENDANT_CREATE_ON);
            return this;
        }

        public Builder sortByCreatedOnDesc() {
            mFields.put(SORT_BY, DESCENDANT_CREATE_ON);
            return this;
        }

        public Builder sortByTypeAsc() {
            mFields.put(SORT_BY, ASCENDANT_TYPE);
            return this;
        }

        public Builder sortByTypeDesc() {
            mFields.put(SORT_BY, DESCENDANT_TYPE);
            return this;
        }

        public Builder sortByAmountAsc() {
            mFields.put(SORT_BY, ASCENDANT_AMOUNT);
            return this;
        }

        public Builder sortByAmountDesc() {
            mFields.put(SORT_BY, DESCENDANT_AMOUNT);
            return this;
        }

        public Builder sortByCurrencyAsc() {
            mFields.put(SORT_BY, ASCENDANT_CURRENCY);
            return this;
        }

        public Builder sortByCurrencyDesc() {
            mFields.put(SORT_BY, DESCENDANT_CURRENCY);
            return this;
        }

        public ReceiptQueryParam build() {
            return new ReceiptQueryParam(mFields);
        }
    }
}
