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
package com.hyperwallet.android.model.meta.field;

import androidx.annotation.NonNull;

import com.hyperwallet.android.exception.HyperwalletException;
import com.hyperwallet.android.model.meta.Connection;
import com.hyperwallet.android.model.meta.HyperwalletFee;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Represents Users' program context Transfer Method Configuration information with Fees
 */
public class TransferMethodConfigurationField {

    private static final String TRANSFER_FEE = "fees";
    private static final String TRANSFER_METHOD_CONFIGURATION = "transferMethodUIConfigurations";

    private final Connection<HyperwalletFee> mFeeConnection;
    private final Connection<HyperwalletTransferMethodConfiguration> mTransferMethodConfigurationConnection;

    /**
     * Constructor to build transfer method configuration based on json
     *
     * @param configuration JSON object that represents transfer method configuration data with fees
     */
    public TransferMethodConfigurationField(@NonNull final JSONObject configuration) throws HyperwalletException,
            JSONException {
        mFeeConnection = new Connection<>(configuration.getJSONObject(TRANSFER_FEE), HyperwalletFee.class);
        mTransferMethodConfigurationConnection = new Connection<>
                (configuration.getJSONObject(TRANSFER_METHOD_CONFIGURATION),
                        HyperwalletTransferMethodConfiguration.class);
    }

    public Connection<HyperwalletFee> getFeeConnection() {
        return mFeeConnection;
    }

    public Connection<HyperwalletTransferMethodConfiguration> getTransferMethodConfigurationConnection() {
        return mTransferMethodConfigurationConnection;
    }
}
