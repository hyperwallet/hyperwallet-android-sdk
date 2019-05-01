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
package com.hyperwallet.android.model.meta;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hyperwallet.android.exception.HyperwalletException;
import com.hyperwallet.android.model.meta.keyed.Country;
import com.hyperwallet.android.model.meta.keyed.MappedConnection;

import org.json.JSONObject;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Represents GQL Response Type (T) when parsing Transfer Method Configuration Keys
 */
public class TransferMethodConfigurationKey {

    private static final String TRANSFER_METHOD_COUNTRIES = "countries";

    private final Set<Country> mCountries;
    private MappedConnection<Country> mCountryMappedConnection;

    public TransferMethodConfigurationKey(@NonNull final JSONObject configuration) throws HyperwalletException {
        try {
            JSONObject countries = configuration.getJSONObject(TRANSFER_METHOD_COUNTRIES);
            mCountries = new LinkedHashSet<>(1);
            if (countries != null) {
                mCountryMappedConnection = new MappedConnection<>(countries, Country.class);
            }
        } catch (Exception e) {
            throw new HyperwalletException(e);
        }
    }

    @NonNull
    public Set<Country> getCountries() {
        if (mCountryMappedConnection != null && mCountries.isEmpty()) {
            mCountries.addAll(mCountryMappedConnection.getNodes());
            return mCountries;
        }
        return mCountries;
    }

    @Nullable
    public Country getCountry(@NonNull final String countryCode) {
        if (mCountryMappedConnection != null) {
            return mCountryMappedConnection.getNode(countryCode);
        }
        return null;
    }
}
