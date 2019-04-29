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

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Constructor;

public class TransferMethodConfigurationData implements Data<Connection<TransferMethodConfiguration>> {

    private static final String TRANSFER_METHOD_CONFIGURATIONS = "transferMethodConfigurations";

    private Connection<TransferMethodConfiguration> mTransferMethodConfigurationsConnection;

    public TransferMethodConfigurationData(@NonNull JSONObject data)
            throws ReflectiveOperationException, JSONException {
        Constructor<?> constructor = Connection.class.getConstructor(JSONObject.class, Class.class);
        mTransferMethodConfigurationsConnection = (Connection<TransferMethodConfiguration>)
                constructor.newInstance(data.get(TRANSFER_METHOD_CONFIGURATIONS), TransferMethodConfiguration.class);
    }

    public Connection<TransferMethodConfiguration> getRootType() {
        return mTransferMethodConfigurationsConnection;
    }

    public void setRootType(final Connection<TransferMethodConfiguration> transferMethodConfigurationsConnection) {
        this.mTransferMethodConfigurationsConnection = transferMethodConfigurationsConnection;
    }
}

