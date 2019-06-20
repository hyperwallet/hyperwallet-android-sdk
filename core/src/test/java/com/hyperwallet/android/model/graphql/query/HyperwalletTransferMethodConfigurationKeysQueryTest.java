package com.hyperwallet.android.model.graphql.query;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

public class HyperwalletTransferMethodConfigurationKeysQueryTest {

    @Test
    public void testToQuery_returnsQuery() {

        final String sampleKeyQuery = String.format("query {\n"
                + "\tcountries(idToken: \"%s\") {\n"
                + "\t\tnodes {\n"
                + "\t\t\tcode\n"
                + "\t\t\tname\n"
                + "\t\t\tcurrencies {\n"
                + "\t\t\t\tnodes {\n"
                + "\t\t\t\t\tcode\n"
                + "\t\t\t\t\tname\n"
                + "\t\t\t\t\ttransferMethodTypes {\n"
                + "\t\t\t\t\t\tnodes {\n"
                + "\t\t\t\t\t\t\tcode\n"
                + "\t\t\t\t\t\t\tname\n"
                + "\t\t\t\t\t\t\tfees {\n"
                + "\t\t\t\t\t\t\t\tnodes {\n"
                + "\t\t\t\t\t\t\t\t\tcountry\n"
                + "\t\t\t\t\t\t\t\t\tcurrency\n"
                + "\t\t\t\t\t\t\t\t\ttransferMethodType\n"
                + "\t\t\t\t\t\t\t\t\tvalue\n"
                + "\t\t\t\t\t\t\t\t\tfeeRateType\n"
                + "\t\t\t\t\t\t\t\t\tmaximum\n"
                + "\t\t\t\t\t\t\t\t\tminimum\n"
                + "\t\t\t\t\t\t\t\t}\n"
                + "\t\t\t\t\t\t\t}\n"
                + "\t\t\t\t\t\t\tprocessingTime {\n"
                + "\t\t\t\t\t\t\t\tnodes {\n"
                + "\t\t\t\t\t\t\t\t\tcountry\n"
                + "\t\t\t\t\t\t\t\t\tcurrency\n"
                + "\t\t\t\t\t\t\t\t\ttransferMethodType\n"
                + "\t\t\t\t\t\t\t\t\tvalue\n"
                + "\t\t\t\t\t\t\t\t}\n"
                + "\t\t\t\t\t\t\t}\n"
                + "\t\t\t\t\t\t}\n"
                + "\t\t\t\t\t}\n"
                + "\t\t\t\t}\n"
                + "\t\t\t}\n"
                + "\t\t}\n"
                + "\t}\n"
                + "}", "usr-123");

        HyperwalletTransferMethodConfigurationKeysQuery keysQuery =
                new HyperwalletTransferMethodConfigurationKeysQuery();

        String query = keysQuery.toQuery("usr-123");

        assertThat(query, is(sampleKeyQuery));
    }
}
