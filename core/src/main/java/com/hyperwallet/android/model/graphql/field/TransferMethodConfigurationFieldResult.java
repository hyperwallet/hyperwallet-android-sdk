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

import com.hyperwallet.android.model.graphql.Fee;
import com.hyperwallet.android.model.graphql.GqlResponse;
import com.hyperwallet.android.model.graphql.HyperwalletTransferMethodConfigurationField;
import com.hyperwallet.android.model.graphql.ProcessingTime;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * {@code TransferMethodConfigurationFieldResult} implementation of
 * {@link HyperwalletTransferMethodConfigurationField}
 */
public class TransferMethodConfigurationFieldResult
        extends GqlResponse<com.hyperwallet.android.model.graphql.field.TransferMethodConfigurationField> implements
        HyperwalletTransferMethodConfigurationField {

    /**
     * Construct a {@code TransferMethodConfigurationFieldResult} object from {@link JSONObject}
     * representation
     */
    public TransferMethodConfigurationFieldResult(@NonNull final JSONObject response)
            throws ReflectiveOperationException, JSONException {
        super(response, com.hyperwallet.android.model.graphql.field.TransferMethodConfigurationField.class);
    }

    /**
     * @return {@link TransferMethodConfiguration} representation
     */
    @Override
    @Nullable
    public TransferMethodConfiguration getFields() {
        return !getData().getTransferMethodConfigurationConnection().getNodes().isEmpty()
                ? getData().getTransferMethodConfigurationConnection().getNodes().get(0) : null;
    }

    /**
     * @return List of {@link Fee}
     */
    @Override
    @Nullable
    public List<Fee> getFees() {
        return getData().getFeeConnection() != null ? getData().getFeeConnection().getNodes() : null;
    }

    /**
     * @return {@link ProcessingTime}
     */
    @Override
    @Nullable
    public ProcessingTime getProcessingTime() {
        return getData().getProcessingTimeConnection() != null
                ? getData().getProcessingTimeConnection().getNodeAt(0)
                : null;
    }
}
