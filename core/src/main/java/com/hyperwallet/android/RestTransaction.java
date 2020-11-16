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

import com.hyperwallet.android.exception.HyperwalletRestException;
import com.hyperwallet.android.listener.HyperwalletListener;
import com.hyperwallet.android.model.Errors;
import com.hyperwallet.android.model.JsonModel;
import com.hyperwallet.android.model.TypeReference;
import com.hyperwallet.android.util.HttpClient;
import com.hyperwallet.android.util.HttpMethod;
import com.hyperwallet.android.util.JsonUtils;

import org.json.JSONException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * {@code RestTransaction} HTTP transaction service that sends request
 * to Hyperwallet REST platform api
 */
class RestTransaction extends HttpTransaction {

    private RestTransaction(@NonNull final HttpMethod httpMethod, @NonNull final String uri,
            @NonNull final String authenticationToken, @NonNull final HyperwalletListener hyperwalletListener,
            @NonNull final TypeReference typeReference, @NonNull String contextId) {
        super(httpMethod, uri, typeReference, hyperwalletListener);
        addHeader(HTTP_HEADER_AUTHORIZATION, AUTHENTICATION_STRATEGY + authenticationToken);
        addHeader(HTTP_HEADER_X_SDK_CONTEXTID_KEY, contextId);
    }

    /**
     * Refer to {@link HttpTransaction#performRequest(HttpClient)}
     */
    @Override
    protected int performRequest(HttpClient client) throws IOException {
        int code;
        switch (getMethod()) {
            case GET:
                code = client.get();
                break;
            case PUT:
                code = client.put(getPayload());
                break;
            case POST:
                code = client.post(getPayload());
                break;
            default:
                throw new IllegalArgumentException("Unsupported http method");
        }

        return code;
    }

    /**
     * Refer to {@link HttpTransaction#handleErrors(int, String)}
     */
    @Override
    protected void handleErrors(int responseCode, String response) throws JSONException, InvocationTargetException,
            NoSuchMethodException, InstantiationException, IllegalAccessException {
        Errors errors = JsonUtils.fromJsonString(response, new TypeReference<Errors>() {
        });
        onFailure(new HyperwalletRestException(responseCode, errors));
    }

    /**
     * Builder for {@link RestTransaction}
     */
    protected static final class Builder<T> {
        //Required Parameters
        private final HttpMethod httpMethod;
        private final PathFormatter pathFormatter;
        private final TypeReference<T> typeReference;
        private final HyperwalletListener listener;
        private final String contextId;

        //Optional Parameters
        private JsonModel jsonModel = null;
        private Map<String, String> query = new HashMap<>();

        /**
         * Construct builder based from specified required parameters
         *
         * @param httpMethod    HTTP method to use for request transaction
         * @param pathFormatter Path formatter to use {@link PathFormatter}
         * @param typeReference Response type generator result
         * @param listener      callback object; refer to {@link HyperwalletListener}
         */
        protected Builder(@NonNull final HttpMethod httpMethod, @NonNull final PathFormatter pathFormatter,
                @NonNull final TypeReference<T> typeReference, @NonNull final HyperwalletListener listener, @NonNull final String contextId) {
            this.httpMethod = httpMethod;
            this.pathFormatter = pathFormatter;
            this.listener = listener;
            this.typeReference = typeReference;
            this.contextId = contextId;
        }

        protected Builder jsonModel(@NonNull final JsonModel jsonModel) {
            this.jsonModel = jsonModel;
            return this;
        }

        protected Builder query(@NonNull final Map<String, String> query) {
            this.query.putAll(query);
            return this;
        }

        protected RestTransaction build(@NonNull final String uri, @NonNull final String authenticationToken,
                @NonNull final String userToken) throws JSONException {
            RestTransaction restTransaction = new RestTransaction(httpMethod, uri, authenticationToken, listener,
                    typeReference, contextId);

            String path = pathFormatter.format(userToken);
            restTransaction.setPath(path);

            if (jsonModel != null) {
                restTransaction.setPayload(jsonModel.toJsonString());
            }

            restTransaction.addQuery(query);
            return restTransaction;
        }
    }
}
