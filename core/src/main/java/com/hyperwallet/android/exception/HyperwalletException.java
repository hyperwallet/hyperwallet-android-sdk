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

import com.hyperwallet.android.model.Errors;

/**
 * This exception is thrown when an issue is detected during the processing of a request to the Hyperwallet Core SDK.
 *
 * <p>The underlying details and reason for the exception being thrown can be derived by inspecting the
 * {@code Errors} value returned from {@link #getErrors()}.</p>
 */
public class HyperwalletException extends Exception {

    @NonNull
    Errors mErrors;

    HyperwalletException() {
        mErrors = Errors.getEmptyInstance();
    }

    public HyperwalletException(Throwable throwable) {
        super(throwable);
        mErrors = Errors.getEmptyInstance();
    }

    /**
     * Constructs a HyperwalletException with the specified throwable and {@link Errors} cause.
     *
     * @param throwable the exception cause
     * @param errors    the Errors cause of the exception, must not be null
     */
    public HyperwalletException(Throwable throwable, @NonNull Errors errors) {
        super(throwable);
        mErrors = errors;
    }

    /**
     * Constructs a HyperwalletException with the specified {@link Errors} cause.
     *
     * @param errors the Errors cause of the exception, must not be null
     */
    public HyperwalletException(@NonNull Errors errors) {
        mErrors = errors;
    }

    /**
     * Returns the specified {@link Errors} cause of the HyperwalletException.
     *
     * @return the specified Errors cause of the HyperwalletException
     */
    @NonNull
    public Errors getErrors() {
        return mErrors;
    }
}
