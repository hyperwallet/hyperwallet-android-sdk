package com.hyperwallet.android.model.graphql.query;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

public class TransferMethodConfigurationKeysQueryTest {

    @Test
    public void testToQuery_returnsQuery() {

        final String sampleKeyQuery = String.format("query {\n"
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
                + "}", "test-user-token");

        TransferMethodConfigurationKeysQuery keysQuery =
                new TransferMethodConfigurationKeysQuery();

        String query = keysQuery.toQuery("test-user-token");

        assertThat(query, is(sampleKeyQuery));
    }
}
