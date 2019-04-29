package com.hyperwallet.android.model.meta.query;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;


public class HyperwalletTransferMethodConfigurationFieldQueryTest {

    @Test
    public void testToQuery_returnsQuery() {

        String sampleFieldQuery = String.format("query {\n" +
                        "\ttransferMethodConfigurations (\n" +
                        "\t\tidToken: \"%s\",\n" +
                        "\t\tcountry : %s,\n" +
                        "\t\tcurrency : %s,\n" +
                        "\t\ttransferMethodType : %s,\n" +
                        "\t\tprofile : %s \n" +
                        "\t\t)\n" +
                        "\t\t{\n" +
                        "\t\t\tnodes {\n" +
                        "\t\t\t\tcountries\n" +
                        "\t\t\t\tcurrencies\n" +
                        "\t\t\t\ttransferMethodType\n" +
                        "\t\t\t\tprocessingTime\n" +
                        "\t\t\t\tfees {\n" +
                        "\t\t\t\t\tnodes {\n" +
                        "\t\t\t\t\t\ttransferMethodType\n" +
                        "\t\t\t\t\t\tcountry\n" +
                        "\t\t\t\t\t\tcurrency\n" +
                        "\t\t\t\t\t\tfeeRateType\n" +
                        "\t\t\t\t\t\tvalue\n" +
                        "\t\t\t\t\t\tminimum\n" +
                        "\t\t\t\t\t\tmaximum\n" +
                        "\t\t\t\t\t}\n" +
                        "\t\t\t\t}\n" +
                        "\t\t\t\tprofile\n" +
                        "\t\t\t\tfields {\n" +
                        "\t\t\t\t\tcategory\n" +
                        "\t\t\t\t\tdataType\n" +
                        "\t\t\t\t\tisRequired\n" +
                        "\t\t\t\t\tlabel\n" +
                        "\t\t\t\t\tmaxLength\n" +
                        "\t\t\t\t\tminLength\n" +
                        "\t\t\t\t\tname\n" +
                        "\t\t\t\t\tplaceholder\n" +
                        "\t\t\t\t\tregularExpression\n" +
                        "\t\t\t\t\tfieldSelectionOptions {\n" +
                        "\t\t\t\t\t\tlabel\n" +
                        "\t\t\t\t\t\tvalue\n" +
                        "\t\t\t\t\t}\n" +
                        "\t\t\t\t\tvalidationMessage {\n" +
                        "\t\t\t\t\t\tlength\n" +
                        "\t\t\t\t\t\tpattern\n" +
                        "\t\t\t\t\t\tempty\n" +
                        "\t\t\t\t\t}\n" +
                        "\t\t\t\t}\n" +
                        "\t\t\t}\n" +
                        "\t\t}\n" +
                        "}",
                "usr-d8c65e1e-b3e5-460d-8b24-bee7cdae1636", "CA", "CAD", "BANK_ACCOUNT", "INDIVIDUAL");

        HyperwalletTransferMethodConfigurationFieldQuery fieldQuery =
                new HyperwalletTransferMethodConfigurationFieldQuery("CA", "CAD", "BANK_ACCOUNT", "INDIVIDUAL");

        String resultQuery = fieldQuery.toQuery("usr-d8c65e1e-b3e5-460d-8b24-bee7cdae1636");

        assertThat(resultQuery, is(sampleFieldQuery));
    }

}