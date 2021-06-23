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
import com.hyperwallet.android.model.graphql.keyed.Country;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Represents Users' program context Transfer Method Configuration information with Fees
 */
public class TransferMethodConfigurationField {

    private static final String TRANSFER_METHOD_CONFIGURATION = "transferMethodCreateUIConfigurations";
    private static final String COUNTRY = "countries";

    private final Set<Fee> mFee;
    private final Connection<TransferMethodConfiguration> mTransferMethodConfigurationConnection;
    private final ProcessingTime mProcessingTime;

    /**
     * Constructor to build transfer method configuration based on {@link JSONObject} representation
     *
     * @param configuration JSON object that represents transfer method configuration data with fees
     */
    public TransferMethodConfigurationField(@NonNull final JSONObject configuration) throws JSONException,
            NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        JSONObject countryList = configuration.optJSONObject(COUNTRY);
        if (countryList != null && countryList.length() != 0) {
            JSONArray country = countryList.optJSONArray("nodes");
            if (country != null && country.length() != 0) {
                if (country != null && country.length() != 0) {
                    Country result = new Country(country.getJSONObject(0));
                    mFee = result.getCurrencies().iterator().next().getTransferMethodTypes().iterator().next().getFees();
                    mProcessingTime = result.getCurrencies().iterator().next().getTransferMethodTypes().iterator().next().getProcessingTime();
                } else {
                    mFee = null;
                    mProcessingTime = null;
                }
            } else {
                mFee = null;
                mProcessingTime = null;
            }
        } else {
            mFee = null;
            mProcessingTime = null;
        }
        mTransferMethodConfigurationConnection = new Connection<>
                (configuration.getJSONObject(TRANSFER_METHOD_CONFIGURATION),
                        TransferMethodConfiguration.class);

    }

    /**
     * @return {@link Connection} of {@link Fee}
     */
    @Nullable
    public List<Fee> getFee() {
        return mFee != null ? new ArrayList<>(mFee) : null;
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
    public ProcessingTime getProcessingTime() {
        return mProcessingTime;
    }
}
