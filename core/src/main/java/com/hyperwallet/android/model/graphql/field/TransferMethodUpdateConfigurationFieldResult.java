package com.hyperwallet.android.model.graphql.field;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hyperwallet.android.model.graphql.GqlResponse;
import com.hyperwallet.android.model.graphql.HyperwalletTransferMethodConfigurationField;

import org.json.JSONException;
import org.json.JSONObject;

public class TransferMethodUpdateConfigurationFieldResult  extends GqlResponse<TransferMethodUpdateConfigurationField> {
    /**
     * Constructor to build GqlResponse based on {@link JSONObject} representation and class type
     *
     * @param response JSON object that represents data
     */
    public TransferMethodUpdateConfigurationFieldResult(@NonNull JSONObject response) throws ReflectiveOperationException, JSONException {
        super(response, TransferMethodUpdateConfigurationField.class);
    }

    /**
     * @return {@link TransferMethodConfiguration} representation
     */
    @Nullable
    public TransferMethodConfiguration getFields(String userToken) {
        return !getData().getTransferMethodFieldGroupConnection().getNodes().isEmpty()
                ? getData().getTransferMethodFieldGroupConnection().getNodes().get(0) : null;
    }
}
