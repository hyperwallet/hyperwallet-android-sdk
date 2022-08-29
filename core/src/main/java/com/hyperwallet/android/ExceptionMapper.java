/*
 * The MIT License (MIT)
 * Copyright (c) 2018 Hyperwallet Systems Inc.
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
package com.hyperwallet.android;

import androidx.annotation.NonNull;
import androidx.annotation.RestrictTo;
import androidx.annotation.StringRes;

import com.hyperwallet.android.exception.HyperwalletAuthenticationTokenProviderException;
import com.hyperwallet.android.exception.HyperwalletException;
import com.hyperwallet.android.exception.HyperwalletGqlException;
import com.hyperwallet.android.exception.HyperwalletJsonParseException;
import com.hyperwallet.android.exception.HyperwalletRestException;
import com.hyperwallet.android.model.Error;
import com.hyperwallet.android.model.Errors;
import com.hyperwallet.android.sdk.R;

import org.json.JSONException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@code ExceptionMapper} class provides functionality to convert any checked exception into a
 * {@link HyperwalletException}.
 *
 * <p>Exceptions that are created may be a subclass of {@code HyperwalletException}. Each exception will map to
 * a Hyperwallet specific error code, which will be used to build the {@code HyperwalletException}.</p>
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public final class ExceptionMapper {

    public static final String EC_AUTHENTICATION_TOKEN_PROVIDER_EXCEPTION =
            "EC_AUTHENTICATION_TOKEN_PROVIDER_EXCEPTION";
    public static final String EC_IO_EXCEPTION = "EC_IO_EXCEPTION";
    public static final String EC_JSON_EXCEPTION = "EC_JSON_EXCEPTION";
    public static final String EC_JSON_PARSE_EXCEPTION = "EC_JSON_PARSE_EXCEPTION";
    public static final String EC_UNEXPECTED_EXCEPTION = "EC_UNEXPECTED_EXCEPTION";

    /**
     * Converts a {@code HyperwalletException} containing a {@link Errors} object that is derived from the
     * {@code Exception} parameter or the REST/GraphQL response.
     *
     * <p>If an exception does not have a mapping defined the exception is created with the default error code,
     * {@code "EC_UNEXPECTED_EXCEPTION"}.</p>
     *
     * @param exception the {@code Exception} to be converted
     * @return a {@code HyperwalletException} containing a {code Errors} object
     */
    public static HyperwalletException toHyperwalletException(@NonNull final Exception exception) {
        if (exception instanceof HyperwalletGqlException) {
            HyperwalletGqlException hyperwalletGqlException = (HyperwalletGqlException) exception;
            if (hyperwalletGqlException.getHttpCode() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                return initHyperwalletException(R.string.authentication_token_provider_exception,
                        EC_AUTHENTICATION_TOKEN_PROVIDER_EXCEPTION, exception);
            }
            return (HyperwalletGqlException) exception;
        } else if (exception instanceof HyperwalletRestException) {
            HyperwalletRestException hyperwalletRestException = (HyperwalletRestException) exception;
            if (hyperwalletRestException.getHttpCode() == HttpURLConnection.HTTP_BAD_REQUEST) {
                return hyperwalletRestException;
            } else if (hyperwalletRestException.getHttpCode() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                return initHyperwalletException(R.string.authentication_token_provider_exception,
                        EC_AUTHENTICATION_TOKEN_PROVIDER_EXCEPTION, exception);
            }
            return initHyperwalletException(R.string.unexpected_exception, EC_UNEXPECTED_EXCEPTION, exception);
        } else if (exception instanceof HyperwalletException) {
            return (HyperwalletException) exception;
        } else if (exception instanceof IOException) {
            return initHyperwalletException(R.string.io_exception, EC_IO_EXCEPTION, exception);
        } else if (exception instanceof JSONException) {
            return initHyperwalletException(R.string.json_exception, EC_JSON_EXCEPTION, exception);
        } else if (exception instanceof HyperwalletJsonParseException) {
            return initHyperwalletException(R.string.json_parse_exception, EC_JSON_PARSE_EXCEPTION, exception);
        } else if (exception instanceof HyperwalletAuthenticationTokenProviderException) {
            return initHyperwalletException(R.string.authentication_token_provider_exception,
                    EC_AUTHENTICATION_TOKEN_PROVIDER_EXCEPTION, exception);
        } else {
            return initHyperwalletException(R.string.unexpected_exception, EC_UNEXPECTED_EXCEPTION, exception);
        }
    }

    private static HyperwalletException initHyperwalletException(@StringRes int stringResourceId,
                                                                 @NonNull final String code, @NonNull final Throwable throwable) {
        Error error = new Error(stringResourceId, code);
        List<Error> errorList = new ArrayList<>();
        errorList.add(error);
        Errors errors = new Errors(errorList);
        return new HyperwalletException(throwable, errors);
    }
}