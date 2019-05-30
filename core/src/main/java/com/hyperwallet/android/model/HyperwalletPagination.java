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

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the common pagination fields
 */
public class HyperwalletPagination {

    private static final String TAG = HyperwalletPagination.class.getName();
    private final static String PAGINATION_OFFSET = "offset";
    private final static String PAGINATION_LIMIT = "limit";
    protected final static int DEFAULT_LIMIT = 10;
    protected final static int DEFAULT_OFFSET = 0;
    private int mOffset;
    private int mLimit;

    /**
     * Constructors the Hyperwallet Pagination
     */
    public HyperwalletPagination() {
        mLimit = DEFAULT_LIMIT;
        mOffset = DEFAULT_OFFSET;
    }

    /**
     * Constructors a Hyperwallet Pagination based on Map object
     *
     * @param urlQueryMap the URL query map with the specific parameters
     */
    public HyperwalletPagination(@NonNull Map<String, String> urlQueryMap) {
        this();
        mOffset = getIntegerValueBy(urlQueryMap, PAGINATION_OFFSET, DEFAULT_OFFSET);
        mLimit = getIntegerValueBy(urlQueryMap, PAGINATION_LIMIT, DEFAULT_LIMIT);
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
        return this.mOffset;
    }

    /**
     * Defines the number of records to skip.
     */
    public void setOffset(int offset) {
        this.mOffset = offset;
    }

    /**
     * Returns the maximum number of records that will be returned per page.
     *
     * @return the maximum number of records that will be returned per page
     */
    public int getLimit() {
        return this.mLimit;
    }

    /**
     * Defines the maximum number of records that will be returned per page.
     * Default value is 10. Range is from 1 to 100.
     */
    public void setLimit(int limit) {
        this.mLimit = limit;
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
        return query;
    }
}
