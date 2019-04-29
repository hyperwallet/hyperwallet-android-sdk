/*
 * The MIT License (MIT)
 * Copyright (c) 2019 Hyperwallet Systems Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.hyperwallet.android.exception;

import static com.hyperwallet.android.ExceptionMapper.EC_UNEXPECTED_EXCEPTION;

import androidx.annotation.NonNull;

import com.hyperwallet.android.model.HyperwalletError;
import com.hyperwallet.android.model.HyperwalletErrors;
import com.hyperwallet.android.model.meta.error.GqlError;
import com.hyperwallet.android.model.meta.error.GqlErrors;

import java.util.ArrayList;
import java.util.List;

/**
 * Indicates an exceptional condition that occurred while executing a GraphQL transaction with the Hyperwallet platform.
 *
 * <p>A {@code HyperwalletGqlException} contains the {@link HyperwalletErrors} cause.</p>
 */
public class HyperwalletGqlException extends HyperwalletException {

    /**
     * Constructs a {@code HyperwalletGqlException} with the specified {@link GqlErrors} cause.
     *
     * @param gqlErrors the {@code GqlErrors} cause of the exception, must not be null
     */
    public HyperwalletGqlException(@NonNull final GqlErrors gqlErrors) {
        super();
        List<HyperwalletError> errorList = new ArrayList<>();
        List<GqlError> errors = gqlErrors.getGQLErrors();
        for (GqlError gqlError : errors) {
            String code = gqlError.getExtensions() != null ? gqlError.getExtensions().getCode()
                    : EC_UNEXPECTED_EXCEPTION;
            errorList.add(new HyperwalletError(gqlError.getMessage(), code));
        }
        mErrors = new HyperwalletErrors(errorList);
    }
}
