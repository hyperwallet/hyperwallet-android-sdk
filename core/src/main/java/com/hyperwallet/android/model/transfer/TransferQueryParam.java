/*
 * Copyright 2018 Hyperwallet
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and
 * to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of
 * the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING
 * BUT NOT LIMITED TO
 * THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO
 * EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN
 * AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE
 * USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 */

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
