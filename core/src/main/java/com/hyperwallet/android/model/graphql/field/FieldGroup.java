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
 *  NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 *  DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.hyperwallet.android.model.graphql.field;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringDef;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

/**
 * {@code FieldGroup} represents a logical grouping of fields
 */
public class FieldGroup {

    private static final String FIELDS = "fields";
    private static final String GROUP_NAME = "group";

    private final List<Field> mFields;
    private final String mGroupName;

    /**
     * Constructs a {@code FieldGroup} object from {@link JSONObject} representation
     */
    public FieldGroup(@NonNull final JSONObject fieldGroup) throws JSONException {
        mGroupName = fieldGroup.getString(GROUP_NAME);

        JSONArray jsonArray = fieldGroup.optJSONArray(FIELDS);
        if (jsonArray != null && jsonArray.length() != 0) {
            mFields = new ArrayList<>(jsonArray.length());
            for (int i = 0; i < jsonArray.length(); i++) {
                mFields.add(new Field(jsonArray.getJSONObject(i)));
            }
        } else {
            mFields = null;
        }
    }

    @Nullable
    public List<Field> getFields() {
        return mFields;
    }

    @NonNull
    @GroupType
    public String getGroupName() {
        return mGroupName;
    }

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            GroupTypes.ACCOUNT_HOLDER,
            GroupTypes.ACCOUNT_INFORMATION,
            GroupTypes.ADDRESS,
            GroupTypes.CONTACT_INFORMATION,
            GroupTypes.IDENTIFICATION,
            GroupTypes.INTERMEDIARY_ACCOUNT
    })
    public @interface GroupType {
    }

    public final class GroupTypes {
        public static final String ACCOUNT_HOLDER = "ACCOUNT_HOLDER";
        public static final String ACCOUNT_INFORMATION = "ACCOUNT_INFORMATION";
        public static final String ADDRESS = "ADDRESS";
        public static final String CONTACT_INFORMATION = "CONTACT_INFORMATION";
        public static final String IDENTIFICATION = "IDENTIFICATION";
        public static final String INTERMEDIARY_ACCOUNT = "INTERMEDIARY_ACCOUNT";
    }
}
