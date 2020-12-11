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

public class TransferMethodUpdateConfigurationFieldResult extends GqlResponse<TransferMethodUpdateConfigurationField> implements
        HyperwalletTransferMethodConfigurationField {

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
    public TransferMethodConfiguration getFields() {
        return !getData().getTransferMethodFieldGroupConnection().getNodes().isEmpty()
                ? getData().getTransferMethodFieldGroupConnection().getNodes().get(0) : null;
    }

    @Override
    public List<Fee> getFees() {
        return null;
    }

    @Nullable
    @Override
    public ProcessingTime getProcessingTime() {
        return null;
    }
}
