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

package com.hyperwallet.android.model.graphql.query;


import androidx.annotation.NonNull;

import com.hyperwallet.android.model.transfermethod.TransferMethod;

/**
 * The {@code TransferMethodConfigurationFieldQuery} class defines and builds a query to retrieve the fields
 * required to create a transfer method (Bank Account, Bank Card, PayPay Account, Prepaid Card, Paper Check)
 * with the Hyperwallet platform.
 */
public class TransferMethodConfigurationFieldQuery implements GqlQuery {

    private static final String GRAPH_QL_REQUEST =
            "query TransferMethodConfigurationFieldsWithFees (\n"
                    + "\t\t$idToken: String =  \"%s\"\n"
                    + "\t\t$country: Country = %s\n"
                    + "\t\t$currency: Currency = %s\n"
                    + "\t\t$transferMethodType: TransferMethodType = %s\n"
                    + "\t\t$profileType: Profile = %s\n"
                    + "){\n"
                    + "\ttransferMethodUIConfigurations (idToken: $idToken, profileType: $profileType\n"
                    + "\t\tcountry: $country,currency: $currency, transferMethodType: $transferMethodType) {\n"
                    + "\t\tnodes {\n"
                    + "\t\t\tcountry\n"
                    + "\t\t\tcurrency\n"
                    + "\t\t\ttransferMethodType\n"
                    + "\t\t\tprofile\n"
                    + "\t\t\tfieldGroups {\n"
                    + "\t\t\t\tnodes {\n"
                    + "\t\t\t\t\tgroup\n"
                    + "\t\t\t\t\tfields {\n"
                    + "\t\t\t\t\t\tcategory\n"
                    + "\t\t\t\t\t\tdataType\n"
                    + "\t\t\t\t\t\tisEditable\n"
                    + "\t\t\t\t\t\tfieldSelectionOptions {\n"
                    + "\t\t\t\t\t\t\tlabel\n"
                    + "\t\t\t\t\t\t\tvalue\n"
                    + "\t\t\t\t\t\t}\n"
                    + "\t\t\t\t\t\tfileSize {\n"
                    + "\t\t\t\t\t\t\tmin\n"
                    + "\t\t\t\t\t\t\tmax\n"
                    + "\t\t\t\t\t\t}\n"
                    + "\t\t\t\t\t\tfileTypes\n"
                    + "\t\t\t\t\t\tlabel\n"
                    + "\t\t\t\t\t\tmaxLength\n"
                    + "\t\t\t\t\t\tminLength\n"
                    + "\t\t\t\t\t\tname\n"
                    + "\t\t\t\t\t\tplaceholder\n"
                    + "\t\t\t\t\t\tregularExpression\n"
                    + "\t\t\t\t\t\tmask {\n"
                    + "\t\t\t\t\t\t\t\tdefaultPattern\n"
                    + "\t\t\t\t\t\t\t\tscrubRegex\n"
                    + "\t\t\t\t\t\t\t\tconditionalPatterns {\n"
                    + "\t\t\t\t\t\t\t\t\tpattern\n"
                    + "\t\t\t\t\t\t\t\t\tregex\n"
                    + "\t\t\t\t\t\t\t\t}\n"
                    + "\t\t\t\t\t\t}\n"
                    + "\t\t\t\t\t\tisRequired\n"
                    + "\t\t\t\t\t\tvalue\n"
                    + "\t\t\t\t\t\tvalidationMessage {\n"
                    + "\t\t\t\t\t\t\tlength\n"
                    + "\t\t\t\t\t\t\tpattern\n"
                    + "\t\t\t\t\t\t\tempty\n"
                    + "\t\t\t\t\t\t}\n"
                    + "\t\t\t\t\t}\n"
                    + "\t\t\t\t}\n"
                    + "\t\t\t}\n"
                    + "\t\t}\n"
                    + "\t},\n"
                    + "\tfees (idToken: $idToken, country: $country,currency: $currency, \n"
                    + "\t\t\ttransferMethodType: $transferMethodType) {\n"
                    + "\t\t\tnodes {\n"
                    + "\t\t\t\tvalue\n"
                    + "\t\t\t\tfeeRateType\n"
                    + "\t\t\t\tminimum\n"
                    + "\t\t\t\tmaximum\n"
                    + "\t\t\t\tcountry\n"
                    + "\t\t\t\tcurrency\n"
                    + "\t\t}\n"
                    + "\t},\n"
                    + "\tprocessingTimes (idToken: $idToken, country: $country, currency: $currency, \n"
                    + "\t\t\ttransferMethodType: $transferMethodType) {\n"
                    + "\t\t\tnodes {\n"
                    + "\t\t\t\tcountry\n"
                    + "\t\t\t\tcurrency\n"
                    + "\t\t\t\ttransferMethodType\n"
                    + "\t\t\t\tvalue\n"
                    + "\t\t}\n"
                    + "\t}\n"
                    + "}";

    private final String mCountry;
    private final String mCurrency;
    private final String mProfile;
    private final String mTransferMethodType;

    /**
     * Create a new {@code TransferMethodConfigurationQuery} from the country, currency, transferMethodType
     * and profile.
     *
     * @param country            the 2 letter ISO 3166-1 country code
     * @param currency           the 3 letter ISO 4217-1 currency code
     * @param transferMethodType available transfer types defined in
     *                           {@link TransferMethod.TransferMethodTypes}
     * @param profile            {@code INDIVIDUAL} or {@code BUSINESS}
     */
    public TransferMethodConfigurationFieldQuery(@NonNull final String country,
            @NonNull final String currency,
            @NonNull @TransferMethod.TransferMethodType final String transferMethodType,
            @NonNull final String profile) {
        mCountry = country;
        mCurrency = currency;
        mTransferMethodType = transferMethodType;
        mProfile = profile;
    }

    /**
     * {@inheritDoc}
     *
     * @param userToken {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public String toQuery(@NonNull final String userToken) {
        return String.format(GRAPH_QL_REQUEST, userToken, mCountry, mCurrency, mTransferMethodType, mProfile);
    }
}

