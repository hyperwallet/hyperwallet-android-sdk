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

import androidx.annotation.NonNull;

import com.hyperwallet.android.model.HyperwalletErrors;

/**
 * This exception is thrown when an issue is detected during the processing of a request to the Hyperwallet Core SDK.
 *
 * <p>The underlying details and reason for the exception being thrown can be derived by inspecting the
 * {@code HyperwalletErrors} value returned from {@link #getHyperwalletErrors()}.</p>
 */
public class HyperwalletException extends Exception {

    @NonNull
    HyperwalletErrors mErrors;

    HyperwalletException() {
        mErrors = HyperwalletErrors.getEmptyInstance();
    }

    public HyperwalletException(Throwable throwable) {
        super(throwable);
        mErrors = HyperwalletErrors.getEmptyInstance();
    }

    /**
     * Constructs a HyperwalletException with the specified throwable and {@link HyperwalletErrors} cause.
     *
     * @param throwable the exception cause
     * @param errors    the HyperwalletErrors cause of the exception, must not be null
     */
    public HyperwalletException(Throwable throwable, @NonNull HyperwalletErrors errors) {
        super(throwable);
        mErrors = errors;
    }

    /**
     * Constructs a HyperwalletException with the specified {@link HyperwalletErrors} cause.
     *
     * @param errors the HyperwalletErrors cause of the exception, must not be null
     */
    public HyperwalletException(@NonNull HyperwalletErrors errors) {
        mErrors = errors;
    }

    /**
     * Returns the specified {@link HyperwalletErrors} cause of the HyperwalletException.
     *
     * @return the specified HyperwalletErrors cause of the HyperwalletException
     */
    @NonNull
    public HyperwalletErrors getHyperwalletErrors() {
        return mErrors;
    }
}
