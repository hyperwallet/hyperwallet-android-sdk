/*
 *  The MIT License (MIT)
 *  Copyright (c) 2019 Hyperwallet Systems Inc.
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 *  associated documentation files (the "Software"), to deal in the Software without restriction,
 *  including without limitation the rights to use, copy, modify, merge, publish, distribute,
 *  sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 *  NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 *  NON INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 *  DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.hyperwallet.android.model.meta.field;

import androidx.annotation.NonNull;

import com.hyperwallet.android.exception.HyperwalletException;
import com.hyperwallet.android.model.meta.Connection;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Represents Users' program context Transfer Method Configuration information
 */
public class TransferMethodConfiguration {

    private static final String COUNTRY = "country";
    private static final String CURRENCY = "currency";
    private static final String FIELD_GROUPS = "fieldGroups";
    private static final String PROFILE = "profile";
    private static final String TRANSFER_METHOD_TYPE = "transferMethodType";

    private String mCountry;
    private String mCurrency;

    private String mProfile;
    private String mTransferMethodType;
    private Connection<HyperwalletFieldGroup> mHyperwalletFieldGroupConnection;

    /**
     * Constructor to build transfer method configuration based on json
     *
     * @param configuration JSON object that represents transfer method configuration data
     */
    public TransferMethodConfiguration(@NonNull final JSONObject configuration) throws HyperwalletException {
        try {
            mCountry = configuration.getString(COUNTRY);
            mCurrency = configuration.getString(CURRENCY);
            mProfile = configuration.getString(PROFILE);
            mTransferMethodType = configuration.getString(TRANSFER_METHOD_TYPE);

            JSONArray jsonArray = configuration.optJSONArray(FIELD_GROUPS);
            for (int i = 0; i < jsonArray.length(); i++) {
                mHyperwalletFieldGroupConnection = new Connection<>(jsonArray.getJSONObject(1),
                        HyperwalletFieldGroup.class);
            }

        } catch (Exception e) {
            throw new HyperwalletException(e);
        }
    }

    public String getCountry() {
        return mCountry;
    }

    public String getCurrency() {
        return mCurrency;
    }

    public String getProfile() {
        return mProfile;
    }

    public String getTransferMethodType() {
        return mTransferMethodType;
    }

    public Connection<HyperwalletFieldGroup> getHyperwalletFieldGroupConnection() {
        return mHyperwalletFieldGroupConnection;
    }
}
