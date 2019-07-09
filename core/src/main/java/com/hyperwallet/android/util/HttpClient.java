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
package com.hyperwallet.android.util;

import androidx.annotation.RestrictTo;

import com.hyperwallet.android.exception.InvalidRequestPathException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public final class HttpClient {
    private static final String TAG = HttpClient.class.getName();
    private static final int DEFAULT_TIMEOUT = 10_000;

    private final Map<String, String> mQueryMap;
    private final Map<String, String> mHeaderMap;
    private final HttpURLConnection mHttpUrlConnection;

    private HttpClient(final Builder builder) {
        mHeaderMap = builder.mHeaderMap;
        mQueryMap = builder.mQueryMap;
        mHttpUrlConnection = builder.mHttpUrlConnection;
    }

    public static boolean isSuccess(int httpCode) {
        return httpCode >= 200 && httpCode < 300;
    }

    public String getResponse() throws IOException {
        InputStream in = isSuccess(getResponseCode()) ? mHttpUrlConnection.getInputStream()
                : mHttpUrlConnection.getErrorStream();

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            for (int count; (count = in.read(buffer)) != -1; ) {
                out.write(buffer, 0, count);
            }

            return new String(out.toByteArray(), StandardCharsets.UTF_8.name());
        } finally {
            in.close();
            disconnect();
        }
    }

    public Map<String, String> getQueryMap() {
        return mQueryMap;
    }

    public Map<String, String> getHeaderMap() {
        return mHeaderMap;
    }

    public HttpURLConnection getHttpUrlConnection() {
        return mHttpUrlConnection;
    }

    public int get() throws IOException {
        return getResponseCode();
    }

    public int post(String data) throws IOException {
        return submit(HttpMethod.POST.name(), data);
    }

    public int put(String data) throws IOException {
        return submit(HttpMethod.PUT.name(), data);
    }

    private int submit(String method, String data) throws IOException {
        mHttpUrlConnection.setRequestMethod(method);
        mHttpUrlConnection.setDoOutput(true);
        writeOutputStream(mHttpUrlConnection.getOutputStream(), data);
        return getResponseCode();
    }

    private void writeOutputStream(OutputStream outputStream, String data) throws IOException {
        if (data != null) {
            Writer out = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8.name());
            try {
                out.write(data, 0, data.length());
                out.flush();
            } finally {
                out.close();
            }
        }
    }

    private void disconnect() {
        if (mHttpUrlConnection != null) {
            mHttpUrlConnection.disconnect();
        }
    }

    private int getResponseCode() throws IOException {
        return mHttpUrlConnection.getResponseCode();
    }

    public static class Builder {

        private final String mBaseUrl;
        private final Map<String, String> mQueryMap;
        private final Map<String, String> mHeaderMap;
        private int mConnectTimeout;
        private int mReadTimeout;

        private String mPath;
        private HttpURLConnection mHttpUrlConnection;

        public Builder(final String baseUrl) {
            mBaseUrl = baseUrl;
            mQueryMap = new HashMap<>();
            mHeaderMap = new HashMap<>();
            mConnectTimeout = DEFAULT_TIMEOUT;
            mReadTimeout = DEFAULT_TIMEOUT;
        }

        public Builder putHeaders(final Map<String, String> headers) {
            mHeaderMap.putAll(headers);
            return this;
        }

        public Builder putQueries(final Map<String, String> queries) {
            mQueryMap.putAll(queries);
            return this;
        }

        public Builder path(final String path) {
            mPath = path;
            return this;
        }

        /***
         * Defines the connection timeout value in milliseconds for new connections, to define the maximum
         * time to establish a connection with the server.
         *
         * @param milliseconds
         *
         * <p>The default timeout value is 5 seconds<p/>
         */
        public Builder connectTimeout(int milliseconds) {
            if (milliseconds < 0) {
                throw new IllegalArgumentException("connect timeout can not be negative");
            }
            mConnectTimeout = milliseconds;
            return this;
        }

        /***
         * Defines the read timeout value in milliseconds for new connections, to define the maximum
         * time for waiting to read IO operations.
         * @param milliseconds
         *
         * <p>The default timeout value is 10 seconds<p/>
         */
        public Builder readTimeout(int milliseconds) {
            if (milliseconds < 0) {
                throw new IllegalArgumentException("read timeout can not be negative");
            }
            mReadTimeout = milliseconds;
            return this;
        }

        public HttpClient build() throws IOException, URISyntaxException, InvalidRequestPathException {

            StringBuilder url = new StringBuilder(mBaseUrl);
            if (mPath != null) {
                url.append(mPath);
            }

            url.append(buildQuery());

            URI uri = new URI(url.toString());
            mHttpUrlConnection = (HttpURLConnection) uri.toURL().openConnection();
            mHttpUrlConnection.setConnectTimeout(mConnectTimeout);
            mHttpUrlConnection.setReadTimeout(mReadTimeout);

            initHeader();
            return new HttpClient(this);
        }

        private String buildQuery() {
            StringBuilder stringBuilder = new StringBuilder();
            if (!mQueryMap.isEmpty()) {
                for (String key : mQueryMap.keySet()) {
                    if (stringBuilder.length() == 0) {
                        stringBuilder.append("?");
                    } else {
                        stringBuilder.append("&");
                    }
                    stringBuilder.append(key).append("=").append(mQueryMap.get(key));
                }
            }
            return stringBuilder.toString();
        }

        private void initHeader() {
            if (!mHeaderMap.isEmpty()) {
                for (String key : mHeaderMap.keySet()) {
                    mHttpUrlConnection.setRequestProperty(key, mHeaderMap.get(key));
                }
            }
        }
    }

}


