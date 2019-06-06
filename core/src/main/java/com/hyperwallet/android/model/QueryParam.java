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

package com.hyperwallet.android.model;

import static com.hyperwallet.android.util.DateUtil.fromDateTimeString;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringDef;

import com.hyperwallet.android.util.DateUtil;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents the common pagination fields
 */
public class QueryParam {

    private static final String TAG = QueryParam.class.getName();
    public static final String CREATED_BEFORE = "createdBefore";
    public static final String CREATED_AFTER = "createdAfter";
    public static final String SORT_BY = "sortBy";
    public static final String PAGINATION_OFFSET = "offset";
    public static final String PAGINATION_LIMIT = "limit";
    protected static final int DEFAULT_LIMIT = 10;
    protected final static int DEFAULT_OFFSET = 0;
    private final Date mCreatedAfter;
    private final Date mCreatedBefore;
    private final String mSortBy;
    private final int mOffset;
    private final int mLimit;

    /**
     * Constructors the QueryParam
     */
    protected QueryParam(@NonNull Builder builder) {
        mOffset = builder.mOffset;
        mLimit = builder.mLimit == 0 ? DEFAULT_LIMIT : builder.mLimit;
        mCreatedAfter = builder.mCreatedAfter;
        mCreatedBefore = builder.mCreatedBefore;
        mSortBy = builder.mSortBy;
    }

    /**
     * Returns the valid Date type or null in case the content is invalid
     *
     * @param urlQueryMap the URL query map object
     * @param queryKey    the key to get the object in the query map
     * @return the valid Date value or null
     */
    @Nullable
    protected final Date getDateValue(@NonNull Map<String, String> urlQueryMap, @NonNull String queryKey) {
        if (containsKeyAndHasValue(urlQueryMap, queryKey)) {
            return fromDateTimeString(urlQueryMap.get(queryKey));
        }
        return null;
    }

    /**
     * Returns the valid integer value or a default value
     *
     * @param urlQueryMap  the URL query map object
     * @param queryKey     the key to get the object in the query map
     * @param defaultValue the default value will be returned if required
     * @return the valid integer value
     */
    @SuppressWarnings("unchecked")
    private int getIntegerValue(@NonNull Map<String, String> urlQueryMap, @NonNull String queryKey,
            int defaultValue) {
        if (containsKeyAndHasValue(urlQueryMap, queryKey)) {
            try {
                return Integer.parseInt(urlQueryMap.get(queryKey));
            } catch (NumberFormatException exception) {
                return defaultValue;
            }
        }
        return defaultValue;
    }

    /**
     * Checks the query contains the key and has value
     *
     * @param urlQueryMap the map object
     * @param key         the query key to access the object in the map
     * @return the key has valid data to use
     */
    protected boolean containsKeyAndHasValue(@NonNull Map<String, String> urlQueryMap, @NonNull String key) {
        return urlQueryMap.containsKey(key) && urlQueryMap.get(key) != null;
    }

    /**
     * Returns the number of records to skip.
     *
     * @return the number of records to skip.
     */
    public int getOffset() {
        return mOffset;
    }

    /**
     * Returns the maximum number of records that will be returned per page.
     *
     * @return the maximum number of records that will be returned per page
     */
    public int getLimit() {
        return mLimit;
    }

    @Nullable
    public String getSortBy() {
        return mSortBy;
    }

    /**
     * Returns the begin date criteria.
     *
     * @return the begin date criteria
     */
    @Nullable
    public Date getCreatedBefore() {
        return mCreatedBefore;
    }

    @Nullable
    public Date getCreatedAfter() {
        return mCreatedAfter;
    }

    /**
     * Builds the query representation
     *
     * @return map
     */
    @NonNull
    public Map<String, String> buildQuery() {
        Map<String, String> query = new HashMap<>();
        query.put(PAGINATION_OFFSET, String.valueOf(mOffset));
        query.put(PAGINATION_LIMIT, String.valueOf(mLimit));

        if (mCreatedBefore != null) {
            query.put(CREATED_BEFORE, DateUtil.toDateTimeFormat(mCreatedBefore));
        }

        if (mCreatedAfter != null) {
            query.put(CREATED_AFTER, DateUtil.toDateTimeFormat(mCreatedAfter));
        }

        if (mSortBy != null) {
            query.put(SORT_BY, mSortBy);
        }

        return query;
    }

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            Sortable.ASCENDANT_CREATE_ON,
            Sortable.ASCENDANT_STATUS,
            Sortable.ASCENDANT_TYPE,
            Sortable.ASCENDANT_AMOUNT,
            Sortable.ASCENDANT_CURRENCY,
            Sortable.DESCENDANT_CREATE_ON,
            Sortable.DESCENDANT_STATUS,
            Sortable.DESCENDANT_TYPE,
            Sortable.DESCENDANT_AMOUNT,
            Sortable.DESCENDANT_CURRENCY
    })
    public @interface SortableQuery {
    }

    public static final class Sortable {
        public static final String ASCENDANT_CREATE_ON = "+createdOn";
        public static final String ASCENDANT_STATUS = "+status";
        public static final String ASCENDANT_TYPE = "+type";
        public static final String ASCENDANT_AMOUNT = "+amount";
        public static final String ASCENDANT_CURRENCY = "+currency";
        public static final String DESCENDANT_CREATE_ON = "-createdOn";
        public static final String DESCENDANT_STATUS = "-status";
        public static final String DESCENDANT_TYPE = "-type";
        public static final String DESCENDANT_AMOUNT = "-amount";
        public static final String DESCENDANT_CURRENCY = "-currency";
    }

    /**
     * Builder Class for the {@link QueryParam}
     */
    public static class Builder<B extends Builder> {
        private Date mCreatedAfter;
        private Date mCreatedBefore;
        protected String mSortBy;
        private int mOffset;
        private int mLimit;

        @SuppressWarnings("unchecked")
        protected B self() {
            return (B) this;
        }

        /**
         * Defines the number of records to skip.
         */
        public B offset(final int offset) {
            mOffset = offset;
            return self();
        }

        /**
         * Defines the maximum number of records that will be returned per page.
         * Default value is 10. Range is from 1 to 100.
         *
         * @param limit The limit of records to be returned.
         * @return Builder
         */
        public B limit(final int limit) {
            mLimit = limit;
            return self();
        }

        /**
         * Define a Date created after.
         *
         * @param createdAfter Date
         * @return Builder
         */
        public B createdAfter(@NonNull final Date createdAfter) {
            mCreatedAfter = new Date(createdAfter.getTime());
            return self();
        }

        /**
         * Define a Date created before.
         *
         * @param createdBefore Date
         * @return Builder
         */
        public B createdBefore(@NonNull final Date createdBefore) {
            mCreatedBefore = new Date(createdBefore.getTime());
            return self();
        }

        /**
         * Specify the sort order with the Created date ascendant param {@link Sortable#ASCENDANT_CREATE_ON}.
         *
         * @return Builder
         */
        public B sortByCreatedOnAsc() {
            mSortBy = Sortable.ASCENDANT_CREATE_ON;
            return self();
        }

        /**
         * Specify the sort order with the Created date descendant param {@link Sortable#DESCENDANT_CREATE_ON}.
         *
         * @return Builder
         */
        public B sortByCreatedOnDesc() {
            mSortBy = Sortable.DESCENDANT_CREATE_ON;
            return self();
        }

        /**
         * Specify the sort order with the Status ascendant param {@link Sortable#ASCENDANT_STATUS}.
         *
         * @return Builder
         */
        public B sortByStatusAsc() {
            mSortBy = Sortable.ASCENDANT_STATUS;
            return self();
        }

        /**
         * Specify the sort order with the Status descendant param {@link Sortable#DESCENDANT_STATUS}.
         *
         * @return Builder
         */
        public B sortByStatusDesc() {
            mSortBy = Sortable.DESCENDANT_STATUS;
            return self();
        }

        /**
         * Specify the sort order with the Type ascendant param {@link Sortable#ASCENDANT_TYPE}.
         *
         * @return Builder
         */
        public B sortByTypeAsc() {
            mSortBy = Sortable.ASCENDANT_TYPE;
            return self();
        }

        /**
         * Specify the sort order with the Type descendant param {@link Sortable#DESCENDANT_TYPE}.
         *
         * @return Builder
         */
        public B sortByTypeDesc() {
            mSortBy = Sortable.DESCENDANT_TYPE;
            return self();
        }

        /**
         * Specify the sort order with the Amount ascendant param {@link Sortable#ASCENDANT_AMOUNT}.
         *
         * @return Builder
         */
        public B sortByAmountAsc() {
            mSortBy = Sortable.ASCENDANT_AMOUNT;
            return self();
        }

        /**
         * Specify the sort order with the Amount descendant param {@link Sortable#DESCENDANT_AMOUNT}.
         *
         * @return Builder
         */
        public B sortByAmountDesc() {
            mSortBy = Sortable.DESCENDANT_AMOUNT;
            return self();
        }

        /**
         * Specify the sort order with the Currency ascendant param {@link Sortable#ASCENDANT_CURRENCY}.
         *
         * @return Builder
         */
        public B sortByCurrencyAsc() {
            mSortBy = Sortable.ASCENDANT_CURRENCY;
            return self();
        }

        /**
         * Specify the sort order with the Currency descendant param {@link Sortable#DESCENDANT_CURRENCY}.
         *
         * @return Builder
         */
        public B sortByCurrencyDesc() {
            mSortBy = Sortable.DESCENDANT_CURRENCY;
            return self();
        }

        /**
         * Builds an instance of QueryParam with the set of params.
         *
         * @return QueryParam
         */
        public QueryParam build() {
            return new QueryParam(this);
        }
    }
}
