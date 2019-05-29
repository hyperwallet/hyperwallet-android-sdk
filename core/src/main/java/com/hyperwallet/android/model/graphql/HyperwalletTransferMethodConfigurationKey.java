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

import androidx.annotation.NonNull;

import com.hyperwallet.android.model.graphql.keyed.Country;
import com.hyperwallet.android.model.graphql.keyed.Currency;
import com.hyperwallet.android.model.graphql.keyed.HyperwalletTransferMethodType;

import java.util.Set;

/**
 * Represents Transfer Method Configuration Keys
 * <ul>
 * <li>Country</li>
 * <li>Currency</li>
 * <li>Transfer Method Type</li>
 * </ul>
 *
 * Needed to transform form fields
 */
public interface HyperwalletTransferMethodConfigurationKey {

    /**
     * Retrieve a specific {@link Country} representation
     *
     * @param countryCode represented in ISO 3166-1 alpha-2 code format
     * @return specific Country identified by parameter if exists
     */
    Country getCountry(@NonNull final String countryCode);

    /**
     * Set of countries configured for Users' Program Context
     *
     * @return Set of {@code Country}
     */
    Set<Country> getCountries();

    /**
     * Set of currency configured from Users' Program Context
     *
     * @param countryCode represented in ISO 3166-1 alpha-2 code format
     * @return Set of {@code Currency} from country code information
     */
    Set<Currency> getCurrencies(@NonNull final String countryCode);

    /**
     * Set of transfer method types configured from Users' Program Context
     *
     * @param countryCode  represented in ISO 3166-1 alpha-2 code format
     * @param currencyCode represented in ISO 4217 three-letter code format
     * @return Set of {@code HyperwalletTransferMethodType} based from parameters specified
     */
    Set<HyperwalletTransferMethodType> getTransferMethodType(@NonNull final String countryCode,
            @NonNull final String currencyCode);
}
