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
import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HyperwalletField {

    private static final String CATEGORY = "category";
    private static final String DATA_TYPE = "dataType";
    private static final String FIELD_SELECTION_OPTIONS = "fieldSelectionOptions";
    private static final String IS_REQUIRED = "isRequired";
    private static final String LABEL = "label";
    private static final String MAX_LENGTH = "maxLength";
    private static final String MIN_LENGTH = "minLength";
    private static final String NAME = "name";
    private static final String PLACEHOLDER = "placeholder";
    private static final String REGULAR_EXPRESSION = "regularExpression";
    private static final String VALIDATION_MESSAGE = "validationMessage";

    private final String mCategory;
    private final EDataType mDataType;
    private final List<HyperwalletFieldSelectionOption> mFieldSelectionOptions;
    private final boolean mIsRequired;
    private final String mLabel;
    private final int mMaxLength;
    private final int mMinLength;
    private final String mName;
    private final String mPlaceholder;
    private final String mRegularExpression;
    private final HyperwalletValidationMessage mHyperwalletValidationMessage;

    public HyperwalletField(@NonNull JSONObject field) {
        mCategory = field.optString(CATEGORY);
        mDataType = EDataType.getDataType(field.optString(DATA_TYPE));
        mIsRequired = field.optBoolean(IS_REQUIRED);
        mLabel = field.optString(LABEL);
        mMaxLength = field.optInt(MAX_LENGTH, Integer.MAX_VALUE);
        mMinLength = field.optInt(MIN_LENGTH);
        mName = field.optString(NAME);
        mPlaceholder = field.optString(PLACEHOLDER);
        mRegularExpression = field.optString(REGULAR_EXPRESSION);

        JSONArray jsonArray = field.optJSONArray(FIELD_SELECTION_OPTIONS);
        if (jsonArray != null) {
            mFieldSelectionOptions = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                mFieldSelectionOptions.add(new HyperwalletFieldSelectionOption(jsonArray.optJSONObject(i)));
            }
        } else {
            mFieldSelectionOptions = null;
        }

        JSONObject validationMessageObject = field.optJSONObject(VALIDATION_MESSAGE);
        if (validationMessageObject != null) {
            mHyperwalletValidationMessage = new HyperwalletValidationMessage(validationMessageObject);
        } else {
            mHyperwalletValidationMessage = null;
        }
    }

    public String getCategory() {
        return mCategory;
    }

    public EDataType getDataType() {
        return mDataType;
    }

    public boolean isRequired() {
        return mIsRequired;
    }

    public String getLabel() {
        return mLabel;
    }

    public int getMaxLength() {
        return mMaxLength;
    }

    public int getMinLength() {
        return mMinLength;
    }

    public String getName() {
        return mName;
    }

    public String getPlaceholder() {
        return mPlaceholder;
    }

    public String getRegularExpression() {
        return mRegularExpression;
    }

    @Nullable
    public List<HyperwalletFieldSelectionOption> getFieldSelectionOptions() {
        return mFieldSelectionOptions;
    }

    @Nullable
    public HyperwalletValidationMessage getHyperwalletValidationMessage() {
        return mHyperwalletValidationMessage;
    }
}
