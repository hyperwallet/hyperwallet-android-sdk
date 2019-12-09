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
package com.hyperwallet.android.model.graphql;

import static com.hyperwallet.android.model.graphql.Connection.hasNodes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hyperwallet.android.model.graphql.keyed.Country;
import com.hyperwallet.android.model.graphql.keyed.MappedConnection;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Represents GQL Response Type (T) when parsing Transfer Method Configuration Keys
 */
public class TransferMethodConfigurationKey {

    private static final String TRANSFER_METHOD_COUNTRIES = "countries";

    private final Set<Country> mCountries;
    private final MappedConnection<Country> mCountryMappedConnection;

    /**
     * Construct a {@code TransferMethodConfigurationKey} object from {@link JSONObject} representation
     *
     * @param configuration raw data representation
     */
    public TransferMethodConfigurationKey(@NonNull final JSONObject configuration) throws JSONException,
            NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        JSONObject countries = configuration.optJSONObject(TRANSFER_METHOD_COUNTRIES);
        mCountries = new LinkedHashSet<>(1);
        if (countries != null && countries.length() != 0) {
            mCountryMappedConnection = new MappedConnection<>(countries, Country.class);
        } else {
            mCountryMappedConnection = null;
        }
    }

    /**
     * @return List of countries
     */
    @NonNull
    public Set<Country> getCountries() {
        if (mCountries.isEmpty() && hasNodes(mCountryMappedConnection)) {
            mCountries.addAll(mCountryMappedConnection.getNodes());
            return mCountries;
        }
        return mCountries;
    }

    /**
     * Retrieve Country information based from specified country code
     *
     * @return {@link Country}
     */
    @Nullable
    public Country getCountry(@NonNull final String countryCode) {
        if (mCountryMappedConnection != null) {
            return mCountryMappedConnection.getNode(countryCode);
        }
        return null;
    }
}
