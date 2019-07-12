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

import com.hyperwallet.android.util.JsonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;
import java.util.Map;

/**
 * Status transition representation that denotes Hyperwallet entity object status
 */
public class StatusTransition implements HyperwalletJsonModel, Parcelable {

    public static final Creator<StatusTransition> CREATOR =
            new Creator<StatusTransition>() {
                @Override
                public StatusTransition createFromParcel(Parcel source) {
                    final Map<String, Object> fields = new HashMap<>();
                    source.readMap(fields, this.getClass().getClassLoader());
                    return new StatusTransition(fields);
                }

                @Override
                public StatusTransition[] newArray(int size) {
                    return new StatusTransition[size];
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
        private StatusDefinition() {
        }
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
        private StatusCodeDefinition() {
        }
        public static final String RECALLED = "RECALLED";
        public static final String RETURNED = "RETURNED";
    }

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            StatusTransitionField.CREATED_ON,
            StatusTransitionField.FROM_STATUS,
            StatusTransitionField.TO_STATUS,
            StatusTransitionField.STATUS_CODE,
            StatusTransitionField.NOTES,
            StatusTransitionField.TRANSITION,
            StatusTransitionField.TOKEN
    })
    public @interface StatusTransitionFieldKeys {
    }

    public final class StatusTransitionField {
        private StatusTransitionField() {
        }
        public static final String CREATED_ON = "createdOn";
        public static final String FROM_STATUS = "fromStatus";
        public static final String TO_STATUS = "toStatus";
        public static final String STATUS_CODE = "statusCode";
        public static final String NOTES = "notes";
        public static final String TRANSITION = "transition";
        public static final String TOKEN = "token";
    }

    private Map<String, Object> mFields;

    public StatusTransition() {
        mFields = new HashMap<>();
    }

    private StatusTransition(@NonNull final Map<String, Object> fields) {
        mFields = fields;
    }

    public StatusTransition(@NonNull final JSONObject jsonObject) throws JSONException {
        toMap(jsonObject);
    }

    @Nullable
    private String getFieldValueToString(@NonNull @StatusTransitionFieldKeys final String key) {
        return mFields.get(key) != null ? (String) mFields.get(key) : null;
    }

    @Nullable
    public String getCreatedOn() {
        return getFieldValueToString(StatusTransitionField.CREATED_ON);
    }

    @Status
    @Nullable
    public String getFromStatus() {
        return getFieldValueToString(StatusTransitionField.FROM_STATUS);
    }

    @Nullable
    public String getNotes() {
        return getFieldValueToString(StatusTransitionField.NOTES);
    }

    @StatusCode
    @Nullable
    public String getStatusCode() {
        return getFieldValueToString(StatusTransitionField.STATUS_CODE);
    }

    @Nullable
    public String getToken() {
        return getFieldValueToString(StatusTransitionField.TOKEN);
    }

    @Status
    @Nullable
    public String getToStatus() {
        return getFieldValueToString(StatusTransitionField.TO_STATUS);
    }

    @Status
    @Nullable
    public String getTransition() {
        return getFieldValueToString(StatusTransitionField.TRANSITION);
    }

    @Override
    @NonNull
    public JSONObject toJsonObject() throws JSONException {
        return JsonUtils.mapToJsonObject(mFields);
    }

    @NonNull
    @Override
    public String toJsonString() throws JSONException {
        return toJsonObject().toString();
    }

    private void toMap(@NonNull final JSONObject jsonObject) throws JSONException {
        mFields = JsonUtils.jsonObjectToMap(jsonObject);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeMap(mFields);
    }

    public static class Builder {
        private Map<String, Object> fields;

        public Builder() {
            fields = new HashMap<>();
        }

        public Builder createdOn(@NonNull final String createdOn) {
            fields.put(StatusTransitionField.CREATED_ON, createdOn);
            return this;
        }

        public Builder fromStatus(@NonNull @Status final String fromStatus) {
            fields.put(StatusTransitionField.FROM_STATUS, fromStatus);
            return this;
        }

        public Builder toStatus(@NonNull @Status final String toStatus) {
            fields.put(StatusTransitionField.TO_STATUS, toStatus);
            return this;
        }

        public Builder notes(@Nullable final String notes) {
            fields.put(StatusTransitionField.NOTES, notes);
            return this;
        }

        public Builder statusCode(@NonNull @StatusCode final String statusCode) {
            fields.put(StatusTransitionField.STATUS_CODE, statusCode);
            return this;
        }

        public Builder transition(@NonNull @Status final String transition) {
            fields.put(StatusTransitionField.TRANSITION, transition);
            return this;
        }

        public StatusTransition build() {
            return new StatusTransition(fields);
        }
    }
}
