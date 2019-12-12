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

import com.hyperwallet.android.model.graphql.Connection;
import com.hyperwallet.android.model.graphql.Fee;
import com.hyperwallet.android.model.graphql.ProcessingTime;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;

/**
 * Represents Users' program context Transfer Method Configuration information with Fees
 */
public class TransferMethodConfigurationField {

    private static final String TRANSFER_FEE = "fees";
    private static final String TRANSFER_METHOD_CONFIGURATION = "transferMethodUIConfigurations";
    private static final String PROCESSING_TIMES = "processingTimes";

    private final Connection<Fee> mFeeConnection;
    private final Connection<TransferMethodConfiguration> mTransferMethodConfigurationConnection;
    private final Connection<ProcessingTime> mProcessingTimeConnection;

    /**
     * Constructor to build transfer method configuration based on {@link JSONObject} representation
     *
     * @param configuration JSON object that represents transfer method configuration data with fees
     */
    public TransferMethodConfigurationField(@NonNull final JSONObject configuration) throws JSONException,
            NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        JSONObject fees = configuration.optJSONObject(TRANSFER_FEE);
        if (fees != null && fees.length() != 0) {
            mFeeConnection = new Connection<>(fees, Fee.class);
        } else {
            mFeeConnection = null;
        }
        mTransferMethodConfigurationConnection = new Connection<>
                (configuration.getJSONObject(TRANSFER_METHOD_CONFIGURATION),
                        TransferMethodConfiguration.class);

        JSONObject processingTime = configuration.optJSONObject(PROCESSING_TIMES);
        if (processingTime != null && processingTime.length() != 0) {
            mProcessingTimeConnection = new Connection<>(processingTime, ProcessingTime.class);
        } else {
            mProcessingTimeConnection = null;
        }
    }

    /**
     * @return {@link Connection} of {@link Fee}
     */
    @Nullable
    public Connection<Fee> getFeeConnection() {
        return mFeeConnection;
    }

    /**
     * @return {@link Connection} of {@link TransferMethodConfiguration}
     */
    public Connection<TransferMethodConfiguration> getTransferMethodConfigurationConnection() {
        return mTransferMethodConfigurationConnection;
    }

    /**
     * @return {@link Connection} of {@link ProcessingTime}
     */
    @Nullable
    public Connection<ProcessingTime> getProcessingTimeConnection() {
        return mProcessingTimeConnection;
    }
}
