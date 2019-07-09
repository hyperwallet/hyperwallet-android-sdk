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
package com.hyperwallet.android.model.graphql.field;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Input field data type, used to create input widget
 */
public enum EDataType {
    TEXT,
    SELECTION,
    BOOLEAN,
    NUMBER,
    RANGE,
    DATE,
    DATETIME,
    PHONE,
    EMAIL,
    EXPIRY_DATE,
    FILE;

    private static final String TAG = EDataType.class.getName();

    /**
     * Get {@link EDataType} by name.
     *
     * @param name of the type.
     * @return {@link EDataType} or null if there is no constant with specified name
     */
    @Nullable
    public static EDataType getDataType(@NonNull String name) {
        EDataType dataType = null;
        try {
            dataType = EDataType.valueOf(name);
        } catch (IllegalArgumentException e) {
        }

        return dataType;
    }
}
