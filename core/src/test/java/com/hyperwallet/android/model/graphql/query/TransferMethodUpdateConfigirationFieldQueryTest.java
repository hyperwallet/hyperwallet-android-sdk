package com.hyperwallet.android.model.graphql.query;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class TransferMethodUpdateConfigirationFieldQueryTest {

    private String sampleFieldQuery = String.format( "query {\n" +
            "\ttransferMethodUpdateUIConfigurations (\n" +
            "\t\ttrmToken: \"trm-717534f9-02b6-4b3e-b5cb-7707bbb9b791\"\n" +
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
            "}", "trmToken");

    @Test
    public void testToQuery_returnsQuery() {

        TransferMethodUpdateConfigurationFieldQuery query = new TransferMethodUpdateConfigurationFieldQuery("trmToken");
        String resultQuery = query.toQuery("trmToken");

        assertThat(resultQuery, is(sampleFieldQuery));
    }

}
