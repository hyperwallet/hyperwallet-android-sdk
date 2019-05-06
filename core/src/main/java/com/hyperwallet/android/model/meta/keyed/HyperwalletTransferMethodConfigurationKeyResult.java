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
package com.hyperwallet.android.model.meta.keyed;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hyperwallet.android.model.meta.GqlResponse;
import com.hyperwallet.android.model.meta.HyperwalletTransferMethodConfigurationKey;
import com.hyperwallet.android.model.meta.TransferMethodConfigurationKey;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashSet;
import java.util.Set;

public class HyperwalletTransferMethodConfigurationKeyResult extends GqlResponse<TransferMethodConfigurationKey>
        implements HyperwalletTransferMethodConfigurationKey {

    private final TransferMethodConfigurationKey mTransferMethodConfigurationKey;
    private final Set<Country> mCountries;

    /**
     * Constructor to build HyperwalletTransferMethodConfigurationKeyResult based on json
     *
     * @param data JSON object that represents key result data
     */
    public HyperwalletTransferMethodConfigurationKeyResult(@NonNull JSONObject data)
            throws ReflectiveOperationException, JSONException {
        super(data, TransferMethodConfigurationKey.class);
        mTransferMethodConfigurationKey = getData();
        mCountries = new LinkedHashSet<>(mTransferMethodConfigurationKey.getCountries());
    }

    @Override
    public Country getCountry(@NonNull String countryCode) {
        return mTransferMethodConfigurationKey.getCountry(countryCode);
    }

    @Override
    public Set<Country> getCountries() {
        return mCountries;
    }

    @Override
    @Nullable
    public Set<Currency> getCurrencies(@NonNull final String countryCode) {
        return mTransferMethodConfigurationKey.getCountry(countryCode) != null ?
                mTransferMethodConfigurationKey.getCountry(countryCode).getCurrencies() : null;
    }

    @Override
    @Nullable
    public Set<HyperwalletTransferMethodType> getTransferMethodType(@NonNull final String countryCode,
            @NonNull final String currencyCode) {
        return mTransferMethodConfigurationKey.getCountry(countryCode) != null ?
                mTransferMethodConfigurationKey.getCountry(countryCode).getCurrency(currencyCode) != null ?
                        mTransferMethodConfigurationKey.getCountry(countryCode).getCurrency(currencyCode)
                                .getTransferMethodTypes() : null : null;
    }
}
