package com.hyperwallet.android.model.graphql.query;

import androidx.annotation.NonNull;

import com.hyperwallet.android.model.transfermethod.TransferMethod;

/**
 * The {@code TransferMethodConfigurationFieldQuery} class defines and builds a query to retrieve the fields
 * required to update transfer method (Bank Account, Bank Card, PayPay Account, Prepaid Card, Paper Check, Venmo)
 * with the Hyperwallet platform.
 */
public class TransferMethodUpdateConfigurationFieldQuery implements GqlQuery {

    private static final String GRAPH_QL_REQUEST = "query QueryTransferMethodUpdateUIConfiguration(\n" +
            "\t$trmToken:String = \"%s\"\n" +
            ") {\n" +
            "\ttransferMethodUpdateUIConfigurations (\n" +
            "\t\ttrmToken: $trmToken\n" +
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
            "\t\t\t\t\t\ttextTop \n" +
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
            "\t\t}\n" +
            "\t}\n" +
            "}";

    // private final String mTransferMethodToken;
    // FOR TESTING ONLY
    private String mTransferMethodToken = "trm-59b2f69d-bcbb-4caf-83ba-9ff6df446dfa";

    /**
     * Create a new {@code TransferMethodConfigurationQuery} from the country, currency, transferMethodType
     * and profile.
     *
     * @param transferMethodToken
     */
    public TransferMethodUpdateConfigurationFieldQuery(@NonNull final String transferMethodToken) {
        mTransferMethodToken = transferMethodToken;
    }

    /**
     * {@inheritDoc}
     *
     * @param userToken {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public String toQuery(String userToken) {
        return String.format(GRAPH_QL_REQUEST, mTransferMethodToken);
    }
}
