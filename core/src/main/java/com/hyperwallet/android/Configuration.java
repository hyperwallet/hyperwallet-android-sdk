/*
 * Copyright 2018 Hyperwallet
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and
 * to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of
 * the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING
 * BUT NOT LIMITED TO
 * THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO
 * EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN
 * AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE
 * USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 */
package com.hyperwallet.android;

import android.os.SystemClock;
import android.util.Base64;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.Date;
import java.util.concurrent.TimeUnit;

class Configuration {

    private static final String JWT_EXP = "exp";
    private static final String JWT_IAT = "iat";
    private static final String JWT_ISS = "iss";
    private static final String JWT_GQL_URI = "graphql-uri";
    private static final String JWT_REST_URI = "rest-uri";
    private static final String JWT_SEPARATOR = "\\.";
    private static final String JWT_SUB = "sub";
    private static final Long STALE_PERIOD = 30000L;

    private final String mAuthenticationToken;
    private long mCreatedOn;
    private long mExpiresOn;
    private String mGraphQlUri;
    private String mProgramToken;
    private String mRestUri;
    private String mUserToken;
    private long mExpireOnBootTime;

    public Configuration(@NonNull final String token) throws JSONException {
        if (token.isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.mAuthenticationToken = token;
        parseAuthenticationToken();
    }

    public String getAuthenticationToken() {
        return mAuthenticationToken;
    }

    public Date getCreatedOn() {
        return new Date(mCreatedOn);
    }

    public Date getExpiresOn() {
        return new Date(mExpiresOn);
    }

    public String getGraphQlUri() {
        return mGraphQlUri;
    }

    public String getProgramToken() {
        return mProgramToken;
    }

    public String getRestUri() {
        return mRestUri;
    }

    public String getUserToken() {
        return mUserToken;
    }

    public boolean isStale() {
        return SystemClock.elapsedRealtime() >= mExpireOnBootTime - STALE_PERIOD;
    }

    private void parseAuthenticationToken() throws JSONException {
        String[] authenticationTokenParts = mAuthenticationToken.split(JWT_SEPARATOR);
        if (authenticationTokenParts.length != 3) {
            throw new IllegalArgumentException();
        }
        String decodedPayload = decode(authenticationTokenParts[1]);
        if (decodedPayload == null || decodedPayload.isEmpty()) {
            throw new IllegalArgumentException();
        }
        parsePayload(decodedPayload);
    }

    private String decode(String encoded) {
        byte[] decoded = Base64.decode(encoded, Base64.DEFAULT);
        if (decoded != null) {
            return new String(decoded, Charset.defaultCharset());
        } else {
            return null;
        }
    }

    private void parsePayload(String payload) throws JSONException {
        JSONObject jsonObject = new JSONObject(payload);
        mCreatedOn = TimeUnit.SECONDS.toMillis(jsonObject.getLong(JWT_IAT));
        mExpiresOn = TimeUnit.SECONDS.toMillis(jsonObject.getLong(JWT_EXP));
        mGraphQlUri = jsonObject.getString(JWT_GQL_URI);
        mProgramToken = jsonObject.optString(JWT_ISS, null);
        mRestUri = jsonObject.getString(JWT_REST_URI);
        mUserToken = jsonObject.getString(JWT_SUB);
        long tokenLifespan = mExpiresOn - mCreatedOn;
        mExpireOnBootTime = SystemClock.elapsedRealtime() + tokenLifespan;
    }

}
