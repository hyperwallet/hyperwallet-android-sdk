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

import static com.hyperwallet.android.model.HyperwalletStatusTransition.StatusDefinition.ACTIVATED;
import static com.hyperwallet.android.model.HyperwalletStatusTransition.StatusDefinition.DE_ACTIVATED;
import static com.hyperwallet.android.model.HyperwalletStatusTransition.StatusDefinition.INVALID;
import static com.hyperwallet.android.model.HyperwalletStatusTransition.StatusDefinition.VERIFIED;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodTypes.BANK_ACCOUNT;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodTypes.BANK_CARD;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodTypes.PAYPAL_ACCOUNT;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodTypes.WIRE_ACCOUNT;
import static com.hyperwallet.android.util.DateUtil.fromDateTimeString;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringDef;

import com.hyperwallet.android.model.HyperwalletPagination;
import com.hyperwallet.android.util.DateUtil;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Date;
import java.util.Map;

/**
 * Represents the common pagination fields to the transfer methods
 */
public class HyperwalletTransferMethodPagination extends HyperwalletPagination {

    protected static final String TRANSFER_METHOD_CREATE_BEFORE = "createdBefore";
    protected static final String TRANSFER_METHOD_CREATE_AFTER = "createdAfter";
    protected static final String TRANSFER_METHOD_TYPE = "type";
    protected static final String TRANSFER_METHODT_STATUS = "status";
    protected static final String TRANSFER_METHOD_SORT_BY = "sortBy";

    private Date mCreatedAfter;
    private Date mCreatedBefore;
    private String mSortBy;
    private String mStatus;
    private String mType;

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            BANK_ACCOUNT,
            WIRE_ACCOUNT,
            BANK_CARD,
            PAYPAL_ACCOUNT
    })
    public @interface TransferMethodTypeQuery {
    }

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            ACTIVATED,
            DE_ACTIVATED,
            INVALID,
            VERIFIED
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

    public final class TransferMethodSortable {
        public static final String ASCENDANT_CREATE_ON = "+createdOn";
        public static final String ASCENDANT_STATUS = "+status";
        public static final String DESCENDANT_CREATE_ON = "-createdOn";
        public static final String DESCENDANT_STATUS = "-status";
    }

    /**
     * Constructors the Hyperwallet Transfer Method Pagination
     */
    public HyperwalletTransferMethodPagination() {
        super();
    }

    /**
     * Constructors a Hyperwallet Transfer Method Pagination based on Map object
     *
     * @param urlQueryMap the URL query map with the specific parameters
     */
    public HyperwalletTransferMethodPagination(@NonNull Map<String, String> urlQueryMap) {
        super(urlQueryMap);
        mCreatedBefore = getDateValueBy(urlQueryMap, TRANSFER_METHOD_CREATE_BEFORE);
        mCreatedAfter = getDateValueBy(urlQueryMap, TRANSFER_METHOD_CREATE_AFTER);

        if (containsKeyAndHasValue(urlQueryMap, TRANSFER_METHOD_TYPE)) {
            mType = urlQueryMap.get(TRANSFER_METHOD_TYPE);
        }

        if (containsKeyAndHasValue(urlQueryMap, TRANSFER_METHODT_STATUS)) {
            mStatus = urlQueryMap.get(TRANSFER_METHODT_STATUS);
        }

        if (containsKeyAndHasValue(urlQueryMap, TRANSFER_METHOD_SORT_BY)) {
            mSortBy = urlQueryMap.get(TRANSFER_METHOD_SORT_BY);
        }
    }

    /**
     * Returns the valid Date type or null in case the content is invalid
     *
     * @param urlQueryMap the URL query map object
     * @param queryKey    the key to get the object in the query map
     * @return the valid Date value or null
     */
    @Nullable
    Date getDateValueBy(@NonNull Map<String, String> urlQueryMap, @NonNull String queryKey) {
        if (containsKeyAndHasValue(urlQueryMap, queryKey)) {
            return fromDateTimeString(urlQueryMap.get(queryKey));
        }
        return null;
    }

    @Nullable
    public String getType() {
        return mType;
    }

    public void setType(@NonNull @TransferMethodTypeQuery String type) {
        mType = type;
    }

    @Nullable
    public String getSortBy() {
        return mSortBy;
    }

    public void setSortBy(@NonNull @TransferMethodSortableQuery String sortBy) {
        mSortBy = sortBy;
    }

    /**
     * Returns the begin date criteria.
     *
     * @return the begin date criteria
     */
    @Nullable
    public Date getCreatedBefore() {
        return this.mCreatedBefore;
    }

    /**
     * Defines the begin date criteria to list the data created before this date.
     */
    public void setCreatedBefore(@NonNull Date createdBefore) {
        this.mCreatedBefore = createdBefore;
    }

    @Nullable
    public Date getCreatedAfter() {
        return this.mCreatedAfter;
    }

    /**
     * Returns data created after this datetime.
     */
    public void setCreatedAfter(@NonNull Date createdAfter) {
        this.mCreatedAfter = createdAfter;
    }

    @Nullable
    public String getStatus() {
        return mStatus;
    }

    public void setStatus(@NonNull @TransferMethodStatusQuery String status) {
        mStatus = status;
    }

    @NonNull
    @Override
    public Map<String, String> buildQuery() {
        Map<String, String> query = super.buildQuery();

        if (mCreatedBefore != null) {
            query.put(TRANSFER_METHOD_CREATE_BEFORE, DateUtil.toDateTimeFormat(mCreatedBefore));
        }

        if (mCreatedAfter != null) {
            query.put(TRANSFER_METHOD_CREATE_AFTER, DateUtil.toDateTimeFormat(mCreatedAfter));
        }

        if (mType != null) {
            query.put(TRANSFER_METHOD_TYPE, mType);
        }

        if (mStatus != null) {
            query.put(TRANSFER_METHODT_STATUS, mStatus);
        }

        if (mSortBy != null) {
            query.put(TRANSFER_METHOD_SORT_BY, mSortBy);
        }

        return query;
    }
}
