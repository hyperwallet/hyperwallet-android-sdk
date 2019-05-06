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
package com.hyperwallet.android.model.meta.keyed;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hyperwallet.android.exception.HyperwalletException;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Represents the countries which in combination with currencies will tell which transfer methods are
 * available for that country/currency combination
 */
public class Country implements KeyedNode {

    private static final String COUNTRY_CODE = NODE_CODE;
    private static final String COUNTRY_NAME = NODE_NAME;
    private static final String CURRENCIES = "currencies";

    private final Set<Currency> mCurrencies;
    private final String mCode;
    private final MappedConnection<Currency> mCurrencyMappedConnection;
    private final String mName;

    /**
     * Constructor to build Country based on json
     *
     * @param country JSON object that represents country data
     */
    public Country(@NonNull final JSONObject country)
            throws JSONException, NoSuchMethodException, IllegalAccessException,
            InvocationTargetException, InstantiationException, HyperwalletException {
        mCode = country.getString(COUNTRY_CODE);
        mName = country.getString(COUNTRY_NAME);
        mCurrencies = new LinkedHashSet<>(1);
        mCurrencyMappedConnection = new MappedConnection<>(country.getJSONObject(CURRENCIES), Currency.class);
    }

    /**
     * @return Country code represented in ISO 3166-1 alpha-2 code format
     */
    @NonNull
    @Override
    public String getCode() {
        return mCode;
    }

    /**
     * @return Country Name short name officially used by the ISO 3166 Maintenance Agency (ISO 3166/MA)
     */
    @NonNull
    @Override
    public String getName() {
        return mName;
    }

    /**
     * @return set of {@code Currency} represented by this {@code Country} instance
     */
    @NonNull
    public Set<Currency> getCurrencies() {
        if (mCurrencyMappedConnection != null && mCurrencies.isEmpty()) {
            mCurrencies.addAll(mCurrencyMappedConnection.getNodes());
            return mCurrencies;
        }
        return mCurrencies;
    }

    /**
     * Get specific currency
     *
     * @param currencyCode represented in ISO 4217 three-letter code format
     * @return Currency representation based from parameter if exists
     */
    @Nullable
    public Currency getCurrency(@NonNull final String currencyCode) {
        if (mCurrencyMappedConnection != null) {
            return mCurrencyMappedConnection.getNode(currencyCode);
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Country)) return false;
        Country country = (Country) o;
        return Objects.equals(getCode(), country.getCode()) &&
                Objects.equals(getName(), country.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCode(), getName());
    }
}
