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
public class TransferMethodUpdateConfigurationFieldQuery implements GqlQuery {

    private static final String GRAPH_QL_REQUEST =
            "query {\n" +
                    "\ttransferMethodUpdateUIConfigurations (\n" +
                    "\t\ttrmToken: \"trm-307362d1-2032-4db5-8527-a018fa33e097\"\n" +
                    "\t) {\n" +
                    "\t\tnodes {\n" +
                    "\t\t\tcountry\n" +
                    "\t\t\tcurrency\n" +
                    "\t\t\ttransferMethodType\n" +
                    "\t\t\tprofile\n" +
                    "\t\t\tfieldGroups {\n" +
                    "\t\t\t\tnodes {\n" +
                    "\t\t\t\t\tgroup\n" +
                    "\t\t\t\t\tisEditable\n" +
                    "\t\t\t\t\tinstruction {\n" +
                    "\t\t\t\t\t\ttextTop\n" +
                    "\t\t\t\t\t\ttextBottom\n" +
                    "\t\t\t\t\t}\n" +
                    "\t\t\t\t\tfields {\n" +
                    "\t\t\t\t\t\tcategory\n" +
                    "\t\t\t\t\t\tvalue\n" +
                    "\t\t\t\t\t\tdataType\n" +
                    "\t\t\t\t\t\tisRequired\n" +
                    "\t\t\t\t\t\tisEditable\n" +
                    "\t\t\t\t\t\tlabel\n" +
                    "\t\t\t\t\t\tmaxLength\n" +
                    "\t\t\t\t\t\tminLength\n" +
                    "\t\t\t\t\t\tname\n" +
                    "\t\t\t\t\t\tplaceholder\n" +
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
                    "\t\t\t\t\t\tfieldValueMasked\n" +
                    "\t\t\t\t\t\tmask {\n" +
                    "\t\t\t\t\t\t\tconditionalPatterns {\n" +
                    "\t\t\t\t\t\t\t\tpattern\n" +
                    "\t\t\t\t\t\t\t\tregex\n" +
                    "\t\t\t\t\t\t\t}\n" +
                    "\t\t\t\t\t\t\tdefaultPattern\n" +
                    "\t\t\t\t\t\t\tscrubRegex\n" +
                    "\t\t\t\t\t\t}\n" +
                    "\t\t\t\t\t}\n" +
                    "\t\t\t\t}\n" +
                    "\t\t\t}\n" +
                    "\t\t\tbankBranches {\n" +
                    "\t\t\t\tnodes {\n" +
                    "\t\t\t\t\tbankCode\n" +
                    "\t\t\t\t\tbranches {\n" +
                    "\t\t\t\t\t\tvalue\n" +
                    "\t\t\t\t\t\tlabel\n" +
                    "\t\t\t\t\t}\n" +
                    "\t\t\t\t}\n" +
                    "\t\t\t\tbankElement\n" +
                    "\t\t\t\tbranchElement\n" +
                    "\t\t\t}\n" +
                    "\t\t}\n" +
                    "\t}\n" +
                    "}";

    private final String mTrmToken;

    /**
     * Create a new {@code TransferMethodConfigurationQuery} from the country, currency, transferMethodType
     * and profile.
     */
    public TransferMethodUpdateConfigurationFieldQuery(@NonNull final String trmToken) {
        mTrmToken = trmToken;
    }

    /**
     * {@inheritDoc}
     *
     * @param userToken {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public String toQuery(@NonNull final String userToken) {
        return String.format(GRAPH_QL_REQUEST, userToken, mTrmToken);
    }
}

