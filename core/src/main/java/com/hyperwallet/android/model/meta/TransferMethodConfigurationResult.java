/*
 *  The MIT License (MIT)
 *  Copyright (c) 2018 Hyperwallet Systems Inc.
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
package com.hyperwallet.android.model.meta;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Deprecated
public class TransferMethodConfigurationResult extends GqlResponse<TransferMethodConfigurationData> implements
        HyperwalletTransferMethodConfigurationKeyResult, HyperwalletTransferMethodConfigurationFieldResult {

    private static final int INITIAL_CAPACITY = 0;
    private List<String> mCountries;
    private Map<String, Set<String>> mCountryToCurrencyMap;

    public TransferMethodConfigurationResult(JSONObject data) throws ReflectiveOperationException, JSONException {
        super(data, TransferMethodConfigurationData.class);
    }

    @NonNull
    @Override
    public List<String> getCountries() {
        if (mCountries == null) {
            mCountries = extractCountries();
        }
        return mCountries;
    }

    @NonNull
    @Override
    public List<String> getCurrencies(final @NonNull String country) {
        if (mCountryToCurrencyMap == null) {
            mCountryToCurrencyMap = extractCountryCurrencyMapping();
        }
        Set<String> currencySet = mCountryToCurrencyMap.get(country);
        List<String> currencyList;
        if (currencySet == null) {
            currencyList = new ArrayList<>(INITIAL_CAPACITY);
        } else {
            currencyList = new ArrayList<>(currencySet);
        }

        return currencyList;
    }

    @NonNull
    @Override
    public List<String> getTransferMethods(final @NonNull String country,
            final @NonNull String currency,
            final @NonNull String profileType) {
        final Connection<TransferMethodConfiguration> transferMethodConfigurationConnection =
                getTransferMethodConfigurationConnection();

        if (transferMethodConfigurationConnection == null
                || transferMethodConfigurationConnection.getNodes() == null) {
            return new ArrayList<>(INITIAL_CAPACITY);
        }
        Set<String> transferMethodSet = new HashSet<>();
        for (TransferMethodConfiguration node : transferMethodConfigurationConnection.getNodes()) {
            if (node.getCountries().contains(country) && node.getCurrencies().contains(currency)
                    && node.getProfile().equals(profileType)) {
                transferMethodSet.add(node.getTransferMethodType());
            }
        }
        return new ArrayList<>(transferMethodSet);
    }

    @Nullable
    private Connection<TransferMethodConfiguration> getTransferMethodConfigurationConnection() {
        return getData() == null ? null : getData().getRootType();
    }

    @NonNull
    private List<String> extractCountries() {
        final Connection<TransferMethodConfiguration> transferMethodConfigurationConnection =
                getTransferMethodConfigurationConnection();

        if (transferMethodConfigurationConnection == null
                || transferMethodConfigurationConnection.getNodes() == null) {
            return new ArrayList<>(INITIAL_CAPACITY);
        }

        Set<String> countrySet = new HashSet<>();
        for (TransferMethodConfiguration node : transferMethodConfigurationConnection.getNodes()) {
            countrySet.addAll(node.getCountries());
        }
        return countrySet.isEmpty() ? new ArrayList<String>(INITIAL_CAPACITY) : new ArrayList<>(countrySet);
    }

    @NonNull
    private Map<String, Set<String>> extractCountryCurrencyMapping() {
        final Connection<TransferMethodConfiguration> transferMethodConfigurationConnection =
                getTransferMethodConfigurationConnection();

        if (transferMethodConfigurationConnection == null
                || transferMethodConfigurationConnection.getNodes() == null) {
            return new HashMap<>(INITIAL_CAPACITY);
        }

        Map<String, Set<String>> countryToCurrencyMapping = new HashMap<>();
        for (TransferMethodConfiguration node : transferMethodConfigurationConnection.getNodes()) {
            for (String country : node.getCountries()) {
                if (!countryToCurrencyMapping.containsKey(country)) {
                    countryToCurrencyMapping.put(country, new HashSet<String>());
                }
                countryToCurrencyMapping.get(country).addAll(node.getCurrencies());
            }
        }
        return countryToCurrencyMapping;
    }

    @NonNull
    @Override
    public List<HyperwalletField> getFields() {
        final Connection<TransferMethodConfiguration> transferMethodConfigurationConnection =
                getTransferMethodConfigurationConnection();

        if (transferMethodConfigurationConnection == null
                || transferMethodConfigurationConnection.getNodes() == null) {
            return new ArrayList<>(INITIAL_CAPACITY);
        }

        List<HyperwalletField> fields = new ArrayList<>();
        for (TransferMethodConfiguration node : transferMethodConfigurationConnection.getNodes()) {
            // there will only one node returned as we are using unique key combination here
            fields.addAll(node.getFields());
        }
        return fields;
    }

    @NonNull
    @Override
    public List<Fee> getFees(@NonNull final String country,
            @NonNull final String currency,
            @NonNull final String transferMethodType,
            @NonNull final String profileType) {
        final Connection<TransferMethodConfiguration> transferMethodConfigurationConnection =
                getTransferMethodConfigurationConnection();

        if (transferMethodConfigurationConnection == null
                || transferMethodConfigurationConnection.getNodes() == null) {
            return new ArrayList<>(INITIAL_CAPACITY);
        }

        List<Fee> fees = new ArrayList<>();
        for (TransferMethodConfiguration node : transferMethodConfigurationConnection.getNodes()) {
            //there should be only one node in fee response
            Connection<Fee> feeConnection = node.getFeeConnection();
            //some fees are not available specially if there's no fee configuration so we need to check for null & empty
            if (node.getProfile().equals(profileType) && feeConnection != null && feeConnection.getNodes() != null
                    && !feeConnection.getNodes().isEmpty()) {
                for (Fee fee : feeConnection.getNodes()) {
                    if (fee.getCountry().equals(country)
                            && fee.getCurrency().equals(currency)
                            && fee.getTransferMethodType().equals(transferMethodType)) {
                        fees.add(fee);
                    }
                }
            }
        }
        return fees;
    }

    @Nullable
    @Override
    public String getProcessingTime(@NonNull final String country, @NonNull final String currency,
            @NonNull final String transferMethodType, @NonNull final String profileType) {

        final Connection<TransferMethodConfiguration> transferMethodConfigurationConnection =
                getTransferMethodConfigurationConnection();
        if (transferMethodConfigurationConnection == null
                || transferMethodConfigurationConnection.getNodes() == null) {
            return null;
        }

        List<TransferMethodConfiguration> nodes = transferMethodConfigurationConnection.getNodes();
        for (TransferMethodConfiguration configuration : nodes) {
            if (configuration.getProfile().equals(profileType)
                    && configuration.getCountries().contains(country)
                    && configuration.getCurrencies().contains(currency)
                    && configuration.getTransferMethodType().equals(transferMethodType)) {
                return configuration.getProcessingTime();
            }
        }

        return null;
    }
}
