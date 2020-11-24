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

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;

import com.hyperwallet.android.listener.HyperwalletListener;
import com.hyperwallet.android.model.TypeReference;
import com.hyperwallet.android.sdk.BuildConfig;
import com.hyperwallet.android.util.HttpClient;
import com.hyperwallet.android.util.HttpMethod;
import com.hyperwallet.android.util.JsonUtils;

import org.json.JSONException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * {@code HttpTransaction} HTTP transaction service that sends request
 * to Hyperwallet API platforms
 */
public abstract class HttpTransaction implements Runnable {

    protected static final String AUTHENTICATION_STRATEGY = "Bearer ";
    protected static final String HTTP_HEADER_AUTHORIZATION = "Authorization";
    private static final String APPLICATION_JSON = "application/json";
    private static final String HTTP_HEADER_ACCEPT_KEY = "Accept";
    private static final String HTTP_HEADER_CONTENT_TYPE_KEY = "Content-Type";
    private static final String HTTP_HEADER_USER_AGENT_KEY = "User-Agent";
    private static final String HTTP_HEADER_USER_AGENT = "HyperwalletSDK/Android/%s; App: HyperwalletSDK; Android: %s";
    private static final String HTTP_HEADER_ACCEPT_LANGUAGE_KEY = "Accept-Language";
    private static final String HTTP_HEADER_X_SDK_VERSION_KEY = "X-Sdk-Version";
    private static final String HTTP_HEADER_X_SDK_TYPE_KEY = "X-Sdk-Type";
    protected static final String HTTP_HEADER_X_SDK_CONTEXTID_KEY = "X-Sdk-ContextId";
    private static final String HTTP_HEADER_X_SDK_TYPE = "android";

    private Map<String, String> mHeaderMap;
    private HyperwalletListener mListener;
    private HttpMethod mMethod;
    private String mPayload;
    private String mPath;
    private Map<String, String> mQueryMap;
    private String mUri;
    private TypeReference mTypeReference;

    /**
     * Construct a {@code HttpTransaction} object based from specified required parameters
     *
     * @param httpMethod    Http method to use; refer to {@link HttpMethod}
     * @param uri           location of api
     * @param typeReference Response type generator result
     * @param httpListener  callback object; refer to {@link HyperwalletListener}
     */
    public HttpTransaction(@NonNull final HttpMethod httpMethod, @NonNull final String uri,
            @NonNull final TypeReference typeReference,
            @NonNull final HyperwalletListener httpListener) {
        mMethod = httpMethod;
        mUri = uri;
        mTypeReference = typeReference;
        mListener = httpListener;
        mQueryMap = new HashMap<>();
        mHeaderMap = new HashMap<>();

        addHeader(HTTP_HEADER_ACCEPT_KEY, APPLICATION_JSON);
        addHeader(HTTP_HEADER_CONTENT_TYPE_KEY, APPLICATION_JSON);
        addHeader(HTTP_HEADER_USER_AGENT_KEY, getUserAgent());
        addHeader(HTTP_HEADER_ACCEPT_LANGUAGE_KEY, Locale.getDefault().toLanguageTag());
        addHeader(HTTP_HEADER_X_SDK_VERSION_KEY, BuildConfig.VERSION_NAME);
        addHeader(HTTP_HEADER_X_SDK_TYPE_KEY, HTTP_HEADER_X_SDK_TYPE);
    }

    /**
     * Background execution
     */
    public void run() {
        try {
            HttpClient client = new HttpClient.Builder(mUri).path(mPath).putHeaders(getHeaders()).putQueries(
                    getQueries()).build();

            int responseCode = performRequest(client);
            String response = client.getResponse();

            if (HttpClient.isSuccess(responseCode)) {
                onSuccess(response);
            } else {
                handleErrors(responseCode, response);
            }
        } catch (Exception exception) {
            onFailure(exception);
        }
    }

    /**
     * Process errors, if available, from the resulting HTTP request
     *
     * @param responseCode HTTP response code
     * @param response Serialized HTTP response
     * @throws JSONException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    protected abstract void handleErrors(int responseCode, String response) throws JSONException,
            InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException;

    /**
     * Perform HTTP request
     *
     * @param client Http client to use for this operation {@link HttpClient}
     * @return HTTP response code
     * @throws IOException
     */
    protected abstract int performRequest(HttpClient client) throws IOException;

    public HyperwalletListener getListener() {
        return mListener;
    }

    protected String getPayload() {
        return mPayload;
    }

    protected void setPayload(String payload) {
        mPayload = payload;
    }

    protected String getPath() {
        return mPath;
    }

    protected void setPath(@NonNull String path) {
        mPath = path;
    }

    protected HttpMethod getMethod() {
        return mMethod;
    }

    protected void addQuery(Map<String, String> query) {
        mQueryMap.putAll(query);
    }

    protected void addHeader(String key, String value) {
        mHeaderMap.put(key, value);
    }

    protected Map<String, String> getQueries() {
        return mQueryMap;
    }

    protected Map<String, String> getHeaders() {
        return mHeaderMap;
    }

    @SuppressWarnings("unchecked")
    @VisibleForTesting
    void onSuccess(final String content) throws JSONException, InvocationTargetException, NoSuchMethodException,
            InstantiationException,
            IllegalAccessException {
        if (mListener.getHandler() == null) {
            if (content != null && !content.trim().isEmpty()) {
                mListener.onSuccess(JsonUtils.fromJsonString(content, mTypeReference));
            } else {
                mListener.onSuccess(null); //204 case
            }
        } else {
            mListener.getHandler().post(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (content != null && !content.trim().isEmpty()) {
                            mListener.onSuccess(JsonUtils.fromJsonString(content, mTypeReference));
                        } else {
                            mListener.onSuccess(null); //204 case
                        }
                    } catch (Exception e) {
                        onFailure(e);
                    }
                }
            });
        }
    }

    protected void onFailure(@NonNull final Exception exception) {
        if (mListener.getHandler() == null) {
            mListener.onFailure(ExceptionMapper.toHyperwalletException(exception));
        } else {
            mListener.getHandler().post(new Runnable() {
                @Override
                public void run() {
                    mListener.onFailure(ExceptionMapper.toHyperwalletException(exception));
                }
            });
        }
    }

    private String getUserAgent() {
        return String.format(HTTP_HEADER_USER_AGENT, BuildConfig.VERSION_NAME, Build.VERSION.RELEASE);
    }
}
