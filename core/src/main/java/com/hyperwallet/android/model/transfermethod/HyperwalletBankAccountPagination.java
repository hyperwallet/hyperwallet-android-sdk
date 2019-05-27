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

import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodTypes.BANK_ACCOUNT;

import androidx.annotation.NonNull;

import java.util.Map;

/**
 * Represents the bank account pagination fields
 */
public class HyperwalletBankAccountPagination extends HyperwalletTransferMethodPagination {

    /**
     * Constructors the bank account pagination
     */
    public HyperwalletBankAccountPagination() {
        super();
        setType(BANK_ACCOUNT); //as default type
    }

    /**
     * Constructors to build the pagination based in the preview request
     *
     * @param urlQueryMap the url Map with properties to build the pagination
     */
    public HyperwalletBankAccountPagination(@NonNull Map<String, String> urlQueryMap) {
        super(urlQueryMap);
        setType(BANK_ACCOUNT);
    }
}
