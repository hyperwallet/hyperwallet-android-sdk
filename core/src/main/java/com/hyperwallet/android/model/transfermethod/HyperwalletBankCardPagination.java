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

import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodTypes.BANK_CARD;

import androidx.annotation.NonNull;

import com.hyperwallet.android.util.DateUtil;

import java.util.Date;
import java.util.Map;

/**
 * Represents the bank card pagination fields
 */
public class HyperwalletBankCardPagination extends HyperwalletTransferMethodPagination {

    protected static final String TRANSFER_METHOD_CREATE_ON = "createdOn";

    private Date mCreatedOn;

    /**
     * Constructors the bank card pagination
     */
    private HyperwalletBankCardPagination(Builder builder) {
        super(builder);
        mCreatedOn = builder.mCreatedOn;
    }

    /**
     * Constructor to build the pagination based in the preview request
     *
     * @param urlQueryMap the map with properties to build the pagination
     */
    public HyperwalletBankCardPagination(Map<String, String> urlQueryMap) {
        super(urlQueryMap);
        mCreatedOn = getDateValueBy(urlQueryMap, TRANSFER_METHOD_CREATE_ON);
    }

    public Date getCreatedOn() {
        return mCreatedOn;
    }

    @NonNull
    @Override
    public Map<String, String> buildQuery() {
        Map<String, String> query = super.buildQuery();
        if (mCreatedOn != null) {
            query.put(TRANSFER_METHOD_CREATE_ON, DateUtil.toDateTimeFormat(mCreatedOn));
        }
        return query;
    }

    /**
     * Builder Class for the {@link HyperwalletBankAccountPagination}
     */
    public static class Builder<T extends Builder> extends HyperwalletTransferMethodPagination.Builder<T> {
        private Date mCreatedOn;

        public HyperwalletBankCardPagination build() {
            T builder = type(BANK_CARD);
            return new HyperwalletBankCardPagination(builder);
        }

        /**
         * Define a Date created on.
         *
         * @param createdOn Date
         * @return Builder
         */
        public T createdAfter(Date createdOn) {
            this.mCreatedOn = createdOn;
            return (T) this;
        }
    }
}