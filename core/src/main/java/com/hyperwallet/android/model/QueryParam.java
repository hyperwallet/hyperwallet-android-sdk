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
    protected static final String CREATE_BEFORE = "createdBefore";
    protected static final String CREATE_AFTER = "createdAfter";
    protected static final String SORT_BY = "sortBy";
    private final static String PAGINATION_OFFSET = "offset";
    private final static String PAGINATION_LIMIT = "limit";
    private final static int DEFAULT_LIMIT = 10;
    private final static int DEFAULT_OFFSET = 0;
    private final Date mCreatedAfter;
    private final Date mCreatedBefore;
    private final String mSortBy;
    private final int mOffset;
    private final int mLimit;

    /**
     * Constructors a Hyperwallet Pagination based on Map object
     *
     * @param urlQueryMap the URL query map with the specific parameters
     */
    public QueryParam(@NonNull Map<String, String> urlQueryMap) {
        mOffset = getIntegerValueBy(urlQueryMap, PAGINATION_OFFSET, DEFAULT_OFFSET);
        mLimit = getIntegerValueBy(urlQueryMap, PAGINATION_LIMIT, DEFAULT_LIMIT);
        mCreatedBefore = getDateValueBy(urlQueryMap, CREATE_BEFORE);
        mCreatedAfter = getDateValueBy(urlQueryMap, CREATE_AFTER);
        mSortBy = urlQueryMap.get(SORT_BY);

    }

    /**
     * Constructors the Hyperwallet Pagination
     */
    protected QueryParam(Builder<?, ?> builder) {
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
    protected Date getDateValueBy(@NonNull Map<String, String> urlQueryMap, @NonNull String queryKey) {
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
    private int getIntegerValueBy(@NonNull Map<String, String> urlQueryMap, @NonNull String queryKey,
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
            query.put(CREATE_BEFORE, DateUtil.toDateTimeFormat(mCreatedBefore));
        }

        if (mCreatedAfter != null) {
            query.put(CREATE_AFTER, DateUtil.toDateTimeFormat(mCreatedAfter));
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
            Sortable.DESCENDANT_CREATE_ON,
            Sortable.DESCENDANT_STATUS
    })
    public @interface TransferMethodSortableQuery {
    }

    public static final class Sortable {
        public static final String ASCENDANT_CREATE_ON = "+createdOn";
        public static final String ASCENDANT_STATUS = "+status";
        public static final String DESCENDANT_CREATE_ON = "-createdOn";
        public static final String DESCENDANT_STATUS = "-status";
    }

    @NonNull
    public static Builder<?, ?> builder() {
        return new Builder() {
            @Override
            public QueryParam build() {
                return new QueryParam(this);
            }
        };
    }

    /**
     * Builder Class for the {@link QueryParam}
     */
    public static abstract class Builder<S extends QueryParam, B extends Builder<S, B>> {
        private Date mCreatedAfter;
        private Date mCreatedBefore;
        private String mSortBy;
        private int mOffset;
        private int mLimit;

        /**
         * Builds an instance of T with the set of params.
         *
         * @return QueryParam
         */
        public abstract S build();

        /**
         * Defines the number of records to skip.
         */
        @SuppressWarnings("unchecked")
        public B offset(int offset) {
            mOffset = offset;
            return (B) this;
        }

        /**
         * Defines the maximum number of records that will be returned per page.
         * Default value is 10. Range is from 1 to 100.
         *
         * @param limit The limit of records to be returned.
         * @return Builder
         */
        @SuppressWarnings("unchecked")
        public B limit(int limit) {
            mLimit = limit;
            return (B) this;
        }

        /**
         * Define a Date created after.
         *
         * @param createdAfter Date
         * @return Builder
         */
        @SuppressWarnings("unchecked")
        public B createdAfter(Date createdAfter) {
            mCreatedAfter = createdAfter;
            return (B) this;
        }

        /**
         * Define a Date created before.
         *
         * @param createdBefore Date
         * @return Builder
         */
        @SuppressWarnings("unchecked")
        public B createdBefore(Date createdBefore) {
            mCreatedBefore = createdBefore;
            return (B) this;
        }

        /**
         * Specify the sort order one of the {@link Sortable}.
         *
         * @param sortBy Sort order string
         * @return Builder
         */
        @SuppressWarnings("unchecked")
        public B sortBy(@NonNull @TransferMethodSortableQuery String sortBy) {
            mSortBy = sortBy;
            return (B) this;
        }
    }
}
