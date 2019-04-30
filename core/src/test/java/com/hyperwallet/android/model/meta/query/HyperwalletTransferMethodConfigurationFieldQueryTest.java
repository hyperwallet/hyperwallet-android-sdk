package com.hyperwallet.android.model.meta.query;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;


public class HyperwalletTransferMethodConfigurationFieldQueryTest {

    @Test
    public void testToQuery_returnsQuery() {

        String sampleFieldQuery = String.format("query {\n" +
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
                        " }",
                "usr-d8c65e1e-b3e5-460d-8b24-bee7cdae1636", "INDIVIDUAL", "CA", "CAD", "BANK_ACCOUNT",
                "usr-d8c65e1e-b3e5-460d-8b24-bee7cdae1636", "CA", "CAD", "BANK_ACCOUNT");

        HyperwalletTransferMethodConfigurationFieldQuery fieldQuery =
                new HyperwalletTransferMethodConfigurationFieldQuery("CA", "CAD", "BANK_ACCOUNT", "INDIVIDUAL");

        String resultQuery = fieldQuery.toQuery("usr-d8c65e1e-b3e5-460d-8b24-bee7cdae1636");

        assertThat(resultQuery, is(sampleFieldQuery));
    }

}