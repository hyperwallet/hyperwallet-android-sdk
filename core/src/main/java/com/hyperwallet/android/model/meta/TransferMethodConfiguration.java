/*
 *  The MIT License (MIT)
 *  Copyright (c) 2018 Hyperwallet Systems Inc.
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 *  associated documentation files (the "Software"), to deal in the Software without restriction,
 *  including without limitation the rights to use, copy, modify, merge, publish, distribute,
 *  sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 *  NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 *  NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 *  DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.hyperwallet.android.model.meta;

import androidx.annotation.NonNull;

import com.hyperwallet.android.model.meta.field.HyperwalletField;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class holds data about currencies, countries etc. used in TransferMethodConfigurationResult @see
 */
@Deprecated
public class TransferMethodConfiguration {

    private static final String COUNTRIES = "countries";
    private static final String CURRENCIES = "currencies";
    private static final String FEES = "fees";
    private static final String FIELDS = "fields";
    private static final String PROCESSING_TIME = "processingTime";
    private static final String PROFILE = "profile";
    private static final String TRANSFER_METHOD_TYPE = "transferMethodType";

    private final List<String> mCountries;
    private final List<String> mCurrencies;
    private final Connection<Fee> mFeeConnection;
    private final String mProcessingTime;
    private final String mProfile;
    private final String mTransferMethodType;

    private List<HyperwalletField> mFields;

    public TransferMethodConfiguration(@NonNull JSONObject node) throws JSONException,
            NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        mProfile = node.optString(PROFILE);
        mTransferMethodType = node.optString(TRANSFER_METHOD_TYPE);
        mProcessingTime = node.optString(PROCESSING_TIME);

        JSONArray jsonArray = node.optJSONArray(COUNTRIES);
        if (jsonArray != null) {
            mCountries = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                mCountries.add(jsonArray.optString(i));
            }
        } else {
            mCountries = null;
        }

        jsonArray = node.optJSONArray(CURRENCIES);
        if (jsonArray != null) {
            mCurrencies = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                mCurrencies.add(jsonArray.optString(i));
            }
        } else {
            mCurrencies = null;
        }

        jsonArray = node.optJSONArray(FIELDS);
        if (jsonArray != null) {
            mFields = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                mFields.add(new HyperwalletField(jsonArray.optJSONObject(i)));
            }
        }

        JSONObject feeObject = node.optJSONObject(FEES);
        if (feeObject != null) {
            mFeeConnection = new Connection<>(feeObject, Fee.class);
        } else {
            mFeeConnection = null;
        }
    }

    public List<String> getCountries() {
        return mCountries;
    }

    public List<String> getCurrencies() {
        return mCurrencies;
    }

    public String getProfile() {
        return mProfile;
    }

    public String getTransferMethodType() {
        return mTransferMethodType;
    }

    public Connection<Fee> getFeeConnection() {
        return mFeeConnection;
    }

    public String getProcessingTime() {
        return mProcessingTime;
    }

    public List<HyperwalletField> getFields() {
        return mFields;
    }

    public void setFields(List<HyperwalletField> mFields) {
        this.mFields = mFields;
    }
}
