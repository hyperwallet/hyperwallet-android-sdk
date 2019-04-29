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
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.hyperwallet.android.model;

import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.annotation.VisibleForTesting;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Represents the Hyperwallet Error
 */
public class HyperwalletError implements Parcelable {

    public static final Creator<HyperwalletError> CREATOR = new Creator<HyperwalletError>() {
        @Override
        public HyperwalletError createFromParcel(Parcel source) {
            return new HyperwalletError(source);
        }

        @Override
        public HyperwalletError[] newArray(int size) {
            return new HyperwalletError[size];
        }
    };
    private static final String MESSAGE = "message";
    private static final String FIELD_NAME = "fieldName";
    private static final String CODE = "code";
    private static final String RELATED_RESOURCES = "relatedResources";
    private int mMessageId;
    private String mMessage;
    private String mFieldName;
    private String mCode;
    private List<String> mRelatedResources;

    public HyperwalletError(@NonNull JSONObject jsonObject) throws JSONException {
        fromJson(jsonObject);
    }

    public HyperwalletError(@NonNull String message, @NonNull String code) {
        mMessage = message;
        mCode = code;
    }

    public HyperwalletError(@StringRes int messageId, @NonNull String code) {
        mMessageId = messageId;
        mCode = code;
    }

    public HyperwalletError(@NonNull String message, @Nullable String fieldName, @NonNull String code) {
        this(message, code);
        mFieldName = fieldName;
    }

    public HyperwalletError(@NonNull String message, @Nullable String fieldName, @NonNull String code,
            @Nullable List<String> relatedResources) {
        this(message, fieldName, code);
        mRelatedResources = relatedResources;
    }

    private HyperwalletError(Parcel source) {
        mMessageId = source.readInt();
        mMessage = source.readString();
        mFieldName = source.readString();
        mCode = source.readString();
        mRelatedResources = new ArrayList<>();
        source.readStringList(mRelatedResources);
    }

    @NonNull
    public String getMessage() {
        return mMessage;
    }

    public String getMessageFromResourceWhenAvailable(@NonNull final Resources resources) {
        if (mMessage != null) {
            return mMessage;
        }
        return resources.getString(mMessageId);
    }

    public int getMessageId() {
        return mMessageId;
    }

    @VisibleForTesting
    public void setMessageId(@StringRes int messageId) {
        mMessageId = messageId;
    }

    @Nullable
    public String getFieldName() {
        return mFieldName;
    }

    @NonNull
    public String getCode() {
        return mCode;
    }

    @Nullable
    public List<String> getRelatedResources() {
        return mRelatedResources;
    }

    private void fromJson(@NonNull JSONObject jsonObject) throws JSONException {
        mMessage = jsonObject.optString(MESSAGE);
        mCode = jsonObject.optString(CODE);
        // iterate safely to optional fields in the error representation
        Iterator<String> keys = jsonObject.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            switch (key) {
                case FIELD_NAME:
                    mFieldName = jsonObject.optString(key);
                    break;
                case RELATED_RESOURCES:
                    if (jsonObject.get(key) != null && JSONArray.class.isInstance(jsonObject.get(key))) {
                        JSONArray resources = (JSONArray) jsonObject.get(key);
                        mRelatedResources = new ArrayList<>();
                        for (int i = 0; i < resources.length(); i++) {
                            mRelatedResources.add(resources.getString(i));
                        }
                    }
                    break;
                default: //none
            }
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mMessageId);
        dest.writeString(mMessage);
        dest.writeString(mFieldName);
        dest.writeString(mCode);
        dest.writeStringList(mRelatedResources);
    }
}
