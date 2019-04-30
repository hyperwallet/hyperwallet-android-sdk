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

package com.hyperwallet.android.model.meta.query;


import androidx.annotation.NonNull;

/**
 * The {@code HyperwalletTransferMethodConfigurationFieldQuery} class defines and builds a query to retrieve the fields
 * required to create a transfer method (Bank Account, Bank Card, PayPay Account, Prepaid Card, Paper Check)
 * with the Hyperwallet platform.
 */
public class HyperwalletTransferMethodConfigurationFieldQuery implements HyperwalletGqlQuery {

    private static final String GRAPH_QL_REQUEST =
            "query {\n" +
                    "  transferMethodUIConfigurations (idToken: \"%s\",\n" +
                    "    profileType: \"%s\",\n" +
                    "    country: \"%s\",\n" +
                    "    currency: \"%s\",\n" +
                    "    transferMethodType: \"%s\") {\n" +
                    "\t\tnodes {\n" +
                    "\t\t\tcountry\n" +
                    "\t\t\tcurrency\n" +
                    "\t\t\ttransferMethodType\n" +
                    "\t\t\tprofile\n" +
                    "\t\t\tfieldGroups {\n" +
                    "\t\t\t\tnodes {\n" +
                    "\t\t\t\t\tgroup\n" +
                    "\t\t\t\t\tfields {\n" +
                    "\t\t\t\t\t\tcategory\n" +
                    "\t\t\t\t\t\tdataType\n" +
                    "\t\t\t\t\t\tisRequired\n" +
                    "\t\t\t\t\t\tisEditable\n" +
                    "\t\t\t\t\t\tname\n" +
                    "\t\t\t\t\t\tlabel\n" +
                    "\t\t\t\t\t\tplaceholder\n" +
                    "\t\t\t\t\t\tvalue\n" +
                    "\t\t\t\t\t\tregularExpression\n" +
                    "\t\t\t\t\t\tfieldSelectionOptions {\n" +
                    "\t\t\t\t\t\t\tlabel\n" +
                    "\t\t\t\t\t\t\tvalue\n" +
                    "\t\t\t\t\t\t}\n" +
                    "\t\t\t\t\t\tvalidationMessage {\n" +
                    "\t\t\t\t\t\t\tlength\n" +
                    "\t\t\t\t\t\t\tpattern\n" +
                    "\t\t\t\t\t\t\tempty\n" +
                    "\t\t\t\t\t\t}\n" +
                    "\t\t\t\t\t}\n" +
                    "\t\t\t\t}\n" +
                    "\t\t\t}\n" +
                    "\t\t}\n" +
                    "\t},\n" +
                    "\tfee: fees (idToken: \"%s\",\n" +
                    "    country: \"%s\",\n" +
                    "    currency: \"%s\",\n" +
                    "    transferMethodType: \"%s\") {\n" +
                    "\t\tnodes {\n" +
                    "\t\t  value\n" +
                    "\t\t  feeRateType\n" +
                    "\t\t\tminimum\n" +
                    "\t\t\tmaximum\n" +
                    "\t\t}\n" +
                    "\t}\n" +
                    " }";

    private final String mCountry;
    private final String mCurrency;
    private final String mProfile;
    private final String mTransferMethodType;

    /**
     * Create a new {@code HyperwalletTransferMethodConfigurationQuery} from the country, currency, transferMethodType
     * and profile.
     *
     * @param country            the 2 letter ISO 3166-1 country code
     * @param currency           the 3 letter ISO 4217-1 currency code
     * @param transferMethodType {@code BANK_ACCOUNT}, {@code BANK_CARD}, {@code PAPER_CHECK} or {@code WIRE_ACCOUNT}
     * @param profile            {@code INDIVIDUAL} or {@code BUSINESS}
     */
    public HyperwalletTransferMethodConfigurationFieldQuery(final @NonNull String country,
            final @NonNull String currency,
            final @NonNull String transferMethodType,
            final @NonNull String profile) {
        mCountry = country;
        mCurrency = currency;
        mTransferMethodType = transferMethodType;
        mProfile = profile;
    }

    /**
     * {@inheritDoc}
     *
     * @param idToken {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public String toQuery(String idToken) {
        return String.format(GRAPH_QL_REQUEST, idToken, mProfile, mCountry, mCurrency, mTransferMethodType,
                idToken, mCountry, mCurrency, mTransferMethodType);
    }
}

