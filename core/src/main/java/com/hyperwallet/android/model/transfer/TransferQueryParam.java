package com.hyperwallet.android.model.transfer;

import static com.hyperwallet.android.model.transfer.Transfer.TransferFields.CLIENT_TRANSFER_ID;
import static com.hyperwallet.android.model.transfer.Transfer.TransferFields.DESTINATION_TOKEN;
import static com.hyperwallet.android.model.transfer.Transfer.TransferFields.SOURCE_TOKEN;

import androidx.annotation.NonNull;

import com.hyperwallet.android.model.QueryParam;

import java.util.Map;

/**
 * Represents the query parameters for getting list of receipts {@link Transfer}
 */
public class TransferQueryParam extends QueryParam {

    private final String mClientTransferId;
    private final String mSourceToken;
    private final String mDestinationToken;

    public TransferQueryParam(@NonNull final Builder builder) {
        super(builder);
        mClientTransferId = builder.mClientTransferId;
        mSourceToken = builder.mSourceToken;
        mDestinationToken = builder.mDestinationToken;
    }

    public String getClientTransferId() {
        return mClientTransferId;
    }

    public String getSourceToken() {
        return mSourceToken;
    }

    public String getDestinationToken() {
        return mDestinationToken;
    }

    @NonNull
    @Override
    public Map<String, String> buildQuery() {
        Map<String, String> query = super.buildQuery();
        if (mClientTransferId != null) {
            query.put(CLIENT_TRANSFER_ID, mClientTransferId);
        }
        if (mSourceToken != null) {
            query.put(SOURCE_TOKEN, mSourceToken);
        }
        if (mDestinationToken != null) {
            query.put(DESTINATION_TOKEN, mDestinationToken);
        }
        return query;
    }

    /**
     * Builder Class for the {@link TransferQueryParam}
     */
    public static class Builder extends QueryParam.Builder<TransferQueryParam.Builder> {
        private String mClientTransferId;
        private String mSourceToken;
        private String mDestinationToken;

        /**
         * Define an Client Transfer Id filter
         *
         * @param clientTransferId The client transfer id of transfers
         * @return Builder
         */
        public Builder clientTransferId(@NonNull final String clientTransferId) {
            mClientTransferId = clientTransferId;
            return this;
        }

        /**
         * Define an Source Token filter
         *
         * @param sourceToken The Source Token of transfers
         * @return Builder
         */
        public Builder sourceToken(@NonNull final String sourceToken) {
            mSourceToken = sourceToken;
            return this;
        }

        /**
         * Define an Destination Token filter
         *
         * @param destinationToken The Destination Token of transfers
         * @return Builder
         */
        public Builder destinationToken(@NonNull final String destinationToken) {
            mDestinationToken = destinationToken;
            return this;
        }

        @Override
        public TransferQueryParam build() {
            return new TransferQueryParam(this);
        }

    }

}
