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
package com.hyperwallet.android;

import androidx.annotation.NonNull;

import com.hyperwallet.android.exception.HyperwalletGqlException;
import com.hyperwallet.android.listener.HyperwalletListener;
import com.hyperwallet.android.model.TypeReference;
import com.hyperwallet.android.model.graphql.error.GqlErrors;
import com.hyperwallet.android.model.graphql.query.GqlQuery;
import com.hyperwallet.android.util.HttpClient;
import com.hyperwallet.android.util.HttpMethod;
import com.hyperwallet.android.util.JsonUtils;

import org.json.JSONException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * {@code GqlTransaction} HTTP transaction service that sends request
 * to Hyperwallet GQL platform api
 */
class GqlTransaction extends HttpTransaction {

    /**
     * Construct a {@code GqlTransaction} object based on specified required parameters
     *
     * @param uri                 GQL uri
     * @param body                request query information
     * @param authenticationToken authentication token assigned during authentication flow
     * @param hyperwalletListener callback information please refer to {@link HyperwalletListener}
     * @param typeReference       The class type reference to use in order to deserialize response into Hyperwallet SDK
     *                            object
     */
    private GqlTransaction(@NonNull final String uri,
                           @NonNull final String body,
                           @NonNull final String authenticationToken,
                           @NonNull final HyperwalletListener hyperwalletListener,
                           @NonNull final TypeReference typeReference) {
        super(HttpMethod.POST, uri, typeReference, hyperwalletListener);
        addHeader(HTTP_HEADER_AUTHORIZATION, AUTHENTICATION_STRATEGY + authenticationToken);
        setPayload(body);
    }

    /**
     * Refer to {@link HttpTransaction#performRequest(HttpClient)}
     */
    @Override
    protected int performRequest(final @NonNull HttpClient client) throws IOException {
        return client.post(getPayload());
    }

    /**
     * Refer to {@link HttpTransaction#handleErrors(int, String)}
     */
    @Override
    protected void handleErrors(final int responseCode,
                                @NonNull final String response) throws JSONException,
            InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        GqlErrors gqlErrors = JsonUtils.fromJsonString(response, new TypeReference<GqlErrors>() {
        });
        onFailure(new HyperwalletGqlException(responseCode, gqlErrors));
    }

    /**
     * Builder for {@link GqlTransaction}
     */
    protected static final class Builder<T> {
        private final GqlQuery gqlQuery;
        private final TypeReference<T> typeReference;
        private final HyperwalletListener listener;

        /**
         * Construct a builder based on parameters
         *
         * @param gqlQuery GQL query information
         * @param typeReference Response type generator result
         * @param listener callback object; refer to {@link HyperwalletListener}
         */
        protected Builder(@NonNull final GqlQuery gqlQuery,
                          @NonNull final TypeReference<T> typeReference,
                          @NonNull final HyperwalletListener listener) {
            this.gqlQuery = gqlQuery;
            this.typeReference = typeReference;
            this.listener = listener;
        }

        protected GqlTransaction build(@NonNull final String uri,
                                       @NonNull final String userToken,
                                       @NonNull final String authenticationToken) {
            String query = gqlQuery.toQuery(userToken);
            return new GqlTransaction(uri, query, authenticationToken, listener, typeReference);
        }
    }
}