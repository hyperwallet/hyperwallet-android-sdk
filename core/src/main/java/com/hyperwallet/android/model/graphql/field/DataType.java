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
import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Input field data type, used to create input widget
 */
public final class DataType {
    public static final String TEXT = "TEXT";
    public static final String SELECTION = "SELECTION";
    public static final String BOOLEAN = "BOOLEAN";
    public static final String NUMBER = "NUMBER";
    public static final String RANGE = "RANGE";
    public static final String DATE = "DATE";
    public static final String DATETIME = "DATETIME";
    public static final String PHONE = "PHONE";
    public static final String EMAIL = "EMAIL";
    public static final String EXPIRY_DATE = "EXPIRY_DATE";
    public static final String FILE = "FILE";
    private static final String TAG = DataType.class.getName();

    private DataType() {
    }

    /**
     * Get {@link DataType} by name.
     *
     * @param name of the type.
     * @return String dataType or null if there is no constant with specified name
     */
    @DataTypes
    @Nullable
    public static String getDataType(@NonNull final String name) {
        String dataType = null;
        switch (name) {
            case TEXT:
                dataType = DataType.TEXT;
                break;
            case SELECTION:
                dataType = DataType.SELECTION;
                break;
            case BOOLEAN:
                dataType = DataType.BOOLEAN;
                break;
            case NUMBER:
                dataType = DataType.NUMBER;
                break;
            case RANGE:
                dataType = DataType.RANGE;
                break;
            case DATE:
                dataType = DataType.DATE;
                break;
            case DATETIME:
                dataType = DataType.DATETIME;
                break;
            case PHONE:
                dataType = DataType.PHONE;
                break;
            case EMAIL:
                dataType = DataType.EMAIL;
                break;
            case EXPIRY_DATE:
                dataType = DataType.EXPIRY_DATE;
                break;
            case FILE:
                dataType = DataType.FILE;
                break;
            default:
        }
        return dataType;
    }

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
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
            FILE
    })
    public @interface DataTypes {
    }
}
