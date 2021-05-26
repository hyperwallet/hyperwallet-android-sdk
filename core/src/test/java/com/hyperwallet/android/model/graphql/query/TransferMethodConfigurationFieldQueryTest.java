package com.hyperwallet.android.model.graphql.query;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;


public class TransferMethodConfigurationFieldQueryTest {

    @Test
    public void testToQuery_returnsQuery() {

        String sampleFieldQuery = String.format(
                "query QueryCreateTransferMethod(\n"
                        + "\t\t$usrToken: String =  \"%s\"\n"
                        + "\t\t$country: Country = %s\n"
                        + "\t\t$currency: Currency = %s\n"
                        + "\t\t$transferMethodType: TransferMethodType = %s\n"
                        + "\t\t$profileType: Profile = %s\n"
                        + "){\n"
                        + "\ttransferMethodCreateUIConfigurations (usrToken: $usrToken, profileType: $profileType\n"
                        + "\t\tcountry: $country, currency: $currency, transferMethodType: $transferMethodType) {\n"
                        + "\t\tnodes {\n"
                        + "\t\t\tcountry\n"
                        + "\t\t\tcurrency\n"
                        + "\t\t\ttransferMethodType\n"
                        + "\t\t\tprofile\n"
                        + "\t\t\tfieldGroups {\n"
                        + "\t\t\t\tnodes {\n"
                        + "\t\t\t\t\tgroup\n"
                        + "\t\t\t\t\tfields {\n"
                        + "\t\t\t\t\t\tdataType\n"
                        + "\t\t\t\t\t\tisRequired\n"
                        + "\t\t\t\t\t\tisEditable\n"
                        + "\t\t\t\t\t\tname\n"
                        + "\t\t\t\t\t\tlabel\n"
                        + "\t\t\t\t\t\tplaceholder\n"
                        + "\t\t\t\t\t\tvalue\n"
                        + "\t\t\t\t\t\tmaxLength\n"
                        + "\t\t\t\t\t\tminLength\n"
                        + "\t\t\t\t\t\tregularExpression\n"
                        + "\t\t\t\t\t\tfieldSelectionOptions {\n"
                        + "\t\t\t\t\t\t\tlabel\n"
                        + "\t\t\t\t\t\t\tvalue\n"
                        + "\t\t\t\t\t\t}\n"
                        + "\t\t\t\t\t\tvalidationMessage {\n"
                        + "\t\t\t\t\t\t\tlength\n"
                        + "\t\t\t\t\t\t\tpattern\n"
                        + "\t\t\t\t\t\t\tempty\n"
                        + "\t\t\t\t\t\t}\n"
                        + "\t\t\t\t\t\tmask {\n"
                        + "\t\t\t\t\t\t\tconditionalPatterns {\n"
                        + "\t\t\t\t\t\t\t\tpattern\n"
                        + "\t\t\t\t\t\t\t\tregex\n"
                        + "\t\t\t\t\t\t\t}\n"
                        + "\t\t\t\t\t\tdefaultPattern\n"
                        + "\t\t\t\t\t\tscrubRegex\n"
                        + "\t\t\t\t\t\t}\n"
                        + "\t\t\t\t\t}\n"
                        + "\t\t\t\t}\n"
                        + "\t\t\t}\n"
                        + "\t\t}\n"
                        + "\t},\n"
                        + "\tcountries (idToken: $usrToken, code: $country){\n"
                        + "\t\tnodes {\n"
                        + "\t\t\tcode\n"
                        + "\t\t\tname\n"
                        + "\t\t\tiso3\n"
                        + "\t\t\tcurrencies (code:$currency){\n"
                        + "\t\t\t\tnodes {\n"
                        + "\t\t\t\t\tcode\n"
                        + "\t\t\t\t\tname\n"
                        + "\t\t\t\t\ttransferMethodTypes (code:$transferMethodType){\n"
                        + "\t\t\t\t\t\tnodes {\n"
                        + "\t\t\t\t\t\t\tcode\n"
                        + "\t\t\t\t\t\t\tname\n"
                        + "\t\t\t\t\t\t\tprocessingTimes {\n"
                        + "\t\t\t\t\t\t\t\tnodes {\n"
                        + "\t\t\t\t\t\t\t\t\tcountry\n"
                        + "\t\t\t\t\t\t\t\t\tcurrency\n"
                        + "\t\t\t\t\t\t\t\t\ttransferMethodType\n"
                        + "\t\t\t\t\t\t\t\t\tvalue\n"
                        + "\t\t\t\t\t\t\t\t}\n"
                        + "\t\t\t\t\t\t\t}\n"
                        + "\t\t\t\t\t\t\tfees {\n"
                        + "\t\t\t\t\t\t\t\tnodes {\n"
                        + "\t\t\t\t\t\t\t\tcurrency\n"
                        + "\t\t\t\t\t\t\t\tfeeRateType\n"
                        + "\t\t\t\t\t\t\t\tvalue\n"
                        + "\t\t\t\t\t\t\t\tminimum\n"
                        + "\t\t\t\t\t\t\t\tmaximum\n"
                        + "\t\t\t\t\t\t\t\t}\n"
                        + "\t\t\t\t\t\t\t}\n"
                        + "\t\t\t\t\t\t}\n"
                        + "\t\t\t\t\t}\n"
                        + "\t\t\t\t}\n"
                        + "\t\t\t}\n"
                        + "\t\t}\n"
                        + "\t}\n"
                        + "}",
                "test-user-token", "CA", "CAD", "BANK_ACCOUNT", "INDIVIDUAL");

        TransferMethodConfigurationFieldQuery fieldQuery =
                new TransferMethodConfigurationFieldQuery("CA", "CAD", "BANK_ACCOUNT", "INDIVIDUAL");

        String resultQuery = fieldQuery.toQuery("test-user-token");

        assertThat(resultQuery, is(sampleFieldQuery));
    }

}