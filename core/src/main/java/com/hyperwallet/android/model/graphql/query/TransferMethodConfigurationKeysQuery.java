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
import androidx.annotation.VisibleForTesting;

/**
 * The {@code TransferMethodConfigurationKeysQuery} class defines and builds a query to retrieve the key set
 * that is required to construct a {@code TransferMethodConfigurationFieldQuery}.
 *
 * <p>In addition to the key set, the query will retrieve the country, currency.</p>
 */
public class TransferMethodConfigurationKeysQuery implements GqlQuery {

    @VisibleForTesting
    static final String GRAPH_QL_REQUEST_CONFIGURATION =
            "query {\n"
                    + "\tcountries(idToken: \"%s\") {\n"
                    + "\t\tnodes {\n"
                    + "\t\t\tcode\n"
                    + "\t\t\tname\n"
                    + "\t\t\tdefaultCurrencyCode\n"
                    + "\t\t\tcurrencies {\n"
                    + "\t\t\t\tnodes {\n"
                    + "\t\t\t\t\tcode\n"
                    + "\t\t\t\t\tname\n"
                    + "\t\t\t\t}\n"
                    + "\t\t\t}\n"
                    + "\t\t}\n"
                    + "\t}\n"
                    + "}";

    /**
     * Constructs a {@code TransferMethodConfigurationKeysQuery}.
     */
    public TransferMethodConfigurationKeysQuery() {
    }

    /**
     * {@inheritDoc}
     *
     * @param userToken {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public String toQuery(@NonNull final String userToken) {
        return String.format(GRAPH_QL_REQUEST_CONFIGURATION, userToken);
    }
}

