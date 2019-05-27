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
 *  NON INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 *  DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.hyperwallet.android.model.graphql.field;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hyperwallet.android.model.graphql.GqlResponse;
import com.hyperwallet.android.model.graphql.HyperwalletFee;
import com.hyperwallet.android.model.graphql.HyperwalletTransferMethodConfigurationField;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * {@code HyperwalletTransferMethodConfigurationFieldResult} implementation of
 * {@link HyperwalletTransferMethodConfigurationField}
 */
public class HyperwalletTransferMethodConfigurationFieldResult
        extends GqlResponse<TransferMethodConfigurationField> implements HyperwalletTransferMethodConfigurationField {

    public HyperwalletTransferMethodConfigurationFieldResult(@NonNull final JSONObject response)
            throws ReflectiveOperationException, JSONException {
        super(response, TransferMethodConfigurationField.class);
    }

    @Override
    @Nullable
    public HyperwalletTransferMethodConfiguration getFields() {
        return !getData().getTransferMethodConfigurationConnection().getNodes().isEmpty()
                ? getData().getTransferMethodConfigurationConnection().getNodes().get(0) : null;
    }

    @Override
    @Nullable
    public List<HyperwalletFee> getFees() {
        return getData().getFeeConnection() != null ? getData().getFeeConnection().getNodes() : null;
    }
}
