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

import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodTypes.PAYPAL_ACCOUNT;

import androidx.annotation.NonNull;

import com.hyperwallet.android.util.DateUtil;

import java.util.Date;
import java.util.Map;

/**
 * Represents the PayPal Account pagination fields
 */
public class PayPalAccountPagination extends HyperwalletTransferMethodPagination {

    protected static final String TRANSFER_METHOD_CREATE_ON = "createdOn";

    private Date mCreatedOn;

    /**
     * Constructs the default implementation of the PayPal Account pagination.
     */
    public PayPalAccountPagination() {
        super();
        setType(PAYPAL_ACCOUNT);
    }

    /**
     * Constructs the PayPal Account pagination based in the preview request with extra parameters.
     *
     * @param urlQueryMap the map with properties to build the pagination
     */
    public PayPalAccountPagination(Map<String, String> urlQueryMap) {
        super(urlQueryMap);
        mCreatedOn = getDateValueBy(urlQueryMap, TRANSFER_METHOD_CREATE_ON);
        setType(PAYPAL_ACCOUNT);
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
}