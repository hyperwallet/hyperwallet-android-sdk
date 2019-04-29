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
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 */
package com.hyperwallet.android.exception;

import androidx.annotation.Nullable;

import com.hyperwallet.android.model.HyperwalletErrors;

/**
 * Indicates an exceptional condition that occurred while executing a REST transaction with the Hyperwallet platform.
 *
 * <p>A {@code HyperwalletRestException} contains the {@link HyperwalletErrors} cause and the HTTP response code from
 * the associated REST transaction.</p>
 */
public class HyperwalletRestException extends HyperwalletException {

    private final int mHttpCode;

    /**
     * Constructs a {@code HyperwalletRestException} with the specified {@link HyperwalletErrors} cause and HTTP
     * response code.
     *
     * @param httpCode the HTTP response code of the corresponding REST transaction
     * @param errors   the {@code HyperwalletErrors} cause of the exception, must be non null
     */
    public HyperwalletRestException(int httpCode, @Nullable HyperwalletErrors errors) {
        super(errors);
        mHttpCode = httpCode;
    }

    /**
     * Returns the HTTP response code.
     *
     * @return the HTTP response code
     */
    public int getHttpCode() {
        return mHttpCode;
    }
}
