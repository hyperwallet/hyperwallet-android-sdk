package com.hyperwallet.android.model.graphql.field;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hyperwallet.android.model.graphql.Connection;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;

public class TransferMethodUpdateConfigurationField {

    private static final String TRANSFER_METHOD_CONFIGURATION = "transferMethodUpdateUIConfigurations";

    private final Connection<TransferMethodConfiguration> mTransferMethodFieldGroupConnection;

    /**
     * Constructor to build transfer method configuration based on {@link JSONObject} representation
     *
     * @param configuration JSON object that represents transfer method configuration
     */
    public TransferMethodUpdateConfigurationField(@NonNull final JSONObject configuration) throws JSONException,
            NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

        mTransferMethodFieldGroupConnection = new Connection<>
                (configuration.getJSONObject(TRANSFER_METHOD_CONFIGURATION),
                        TransferMethodConfiguration.class);
    }

    /**
     * @return {@link Connection} of {@link TransferMethodConfiguration}
     */
    public Connection<TransferMethodConfiguration> getTransferMethodFieldGroupConnection() {
        return mTransferMethodFieldGroupConnection;
    }
}
