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

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringDef;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class HyperwalletStatusTransition implements HyperwalletJsonModel, Parcelable {

    public static final Creator<HyperwalletStatusTransition> CREATOR =
            new Creator<HyperwalletStatusTransition>() {
                @Override
                public HyperwalletStatusTransition createFromParcel(Parcel source) {
                    return new HyperwalletStatusTransition(source);
                }

                @Override
                public HyperwalletStatusTransition[] newArray(int size) {
                    return new HyperwalletStatusTransition[size];
                }
            };

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            StatusDefinition.ACTIVATED,
            StatusDefinition.CANCELLED,
            StatusDefinition.COMPLETED,
            StatusDefinition.CREATED,
            StatusDefinition.DE_ACTIVATED,
            StatusDefinition.EXPIRED,
            StatusDefinition.FAILED,
            StatusDefinition.IN_PROGRESS,
            StatusDefinition.INVALID,
            StatusDefinition.LOST_OR_STOLEN,
            StatusDefinition.PENDING_ACCOUNT_ACTIVATION,
            StatusDefinition.PENDING_ID_VERIFICATION,
            StatusDefinition.PENDING_TAX_VERIFICATION,
            StatusDefinition.PENDING_TRANSACTION_VERIFICATION,
            StatusDefinition.PENDING_TRANSFER_METHOD_ACTION,
            StatusDefinition.RECALLED,
            StatusDefinition.RETURNED,
            StatusDefinition.SCHEDULED,
            StatusDefinition.SUSPENDED,
            StatusDefinition.UNSUSPENDED,
            StatusDefinition.VERIFIED
    })
    public @interface Status {
    }

    public final class StatusDefinition {
        public static final String ACTIVATED = "ACTIVATED";
        public static final String CANCELLED = "CANCELLED";
        public static final String COMPLETED = "COMPLETED";
        public static final String CREATED = "CREATED";
        public static final String DE_ACTIVATED = "DE_ACTIVATED";
        public static final String EXPIRED = "CANCELLED";
        public static final String FAILED = "FAILED";
        public static final String IN_PROGRESS = "IN_PROGRESS";
        public static final String INVALID = "INVALID";
        public static final String LOST_OR_STOLEN = "LOST_OR_STOLEN";
        public static final String PENDING_ACCOUNT_ACTIVATION = "PENDING_ACCOUNT_ACTIVATION";
        public static final String PENDING_ID_VERIFICATION = "PENDING_ID_VERIFICATION";
        public static final String PENDING_TAX_VERIFICATION = "PENDING_TAX_VERIFICATION";
        public static final String PENDING_TRANSACTION_VERIFICATION = "PENDING_TRANSACTION_VERIFICATION";
        public static final String PENDING_TRANSFER_METHOD_ACTION = "PENDING_TRANSFER_METHOD_ACTION";
        public static final String RECALLED = "RECALLED";
        public static final String RETURNED = "RETURNED";
        public static final String SCHEDULED = "SCHEDULED";
        public static final String SUSPENDED = "SUSPENDED";
        public static final String UNSUSPENDED = "UNSUSPENDED";
        public static final String VERIFIED = "VERIFIED";
    }

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            StatusCodeDefinition.RECALLED,
            StatusCodeDefinition.RETURNED
    })
    public @interface StatusCode {
    }

    public final class StatusCodeDefinition {
        public static final String RECALLED = "RECALLED";
        public static final String RETURNED = "RETURNED";
    }

    public static final String CREATED_ON = "createdOn";
    public static final String FROM_STATUS = "fromStatus";
    public static final String NOTES = "notes";
    public static final String TOKEN = "token";
    public static final String TO_STATUS = "toStatus";
    public static final String TRANSITION = "transition";
    public static final String STATUS_CODE = "statusCode";

    private String mCreatedOn;
    private @Status
    String mFromStatus;
    private String mNotes;
    private @StatusCode
    String mStatusCode;
    private String mToken;
    private @Status
    String mToStatus;
    private @Status
    String mTransition;

    public HyperwalletStatusTransition(@NonNull JSONObject jsonObject) throws JSONException {
        fromJson(jsonObject);
    }

    private HyperwalletStatusTransition(Parcel source) {
        mCreatedOn = source.readString();
        mFromStatus = source.readString();
        mNotes = source.readString();
        mStatusCode = source.readString();
        mToken = source.readString();
        mToStatus = source.readString();
        mTransition = source.readString();
    }

    public HyperwalletStatusTransition(@Status @NonNull String transition) {
        mTransition = transition;
    }

    @NonNull
    public String getCreatedOn() {
        return mCreatedOn;
    }

    public void setCreatedOn(@NonNull String createdOn) {
        mCreatedOn = createdOn;
    }

    @Status
    @NonNull
    public String getFromStatus() {
        return mFromStatus;
    }

    public void setFromStatus(@NonNull @Status String fromStatus) {
        mFromStatus = fromStatus;
    }

    @Nullable
    public String getNotes() {
        return mNotes;
    }

    public void setNotes(@Nullable String notes) {
        mNotes = notes;
    }

    @NonNull
    public String getStatusCode() {
        return mStatusCode;
    }

    public void setStatusCode(@NonNull @StatusCode String statusCode) {
        mStatusCode = statusCode;
    }

    @NonNull
    public String getToken() {
        return mToken;
    }

    public void setToken(@NonNull String token) {
        mToken = token;
    }

    @Status
    @NonNull
    public String getToStatus() {
        return mToStatus;
    }

    public void setToStatus(@NonNull @Status String toStatus) {
        mToStatus = toStatus;
    }

    @Status
    @NonNull
    public String getTransition() {
        return mTransition;
    }

    @Override
    @NonNull
    public JSONObject toJsonObject() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(NOTES, mNotes);
        jsonObject.put(TRANSITION, mTransition);
        return jsonObject;
    }

    @NonNull
    @Override
    public String toJsonString() throws JSONException {
        return toJsonObject().toString();
    }

    private void fromJson(@NonNull JSONObject jsonObject) {
        mCreatedOn = jsonObject.optString(CREATED_ON);
        mFromStatus = jsonObject.optString(FROM_STATUS);
        mNotes = jsonObject.optString(NOTES);
        mStatusCode = jsonObject.optString(STATUS_CODE);
        mToken = jsonObject.optString(TOKEN);
        mToStatus = jsonObject.optString(TO_STATUS);
        mTransition = jsonObject.optString(TRANSITION);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mCreatedOn);
        dest.writeString(mFromStatus);
        dest.writeString(mNotes);
        dest.writeString(mStatusCode);
        dest.writeString(mToken);
        dest.writeString(mToStatus);
        dest.writeString(mTransition);
    }
}

