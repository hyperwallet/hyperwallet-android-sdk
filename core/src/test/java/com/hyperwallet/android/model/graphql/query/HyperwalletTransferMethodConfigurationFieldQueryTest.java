package com.hyperwallet.android.model.graphql.query;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;


public class HyperwalletTransferMethodConfigurationFieldQueryTest {

    @Test
    public void testToQuery_returnsQuery() {

        String sampleFieldQuery = String.format(
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
                        + "}",
                "usr-2ff94da8-f0f6-4d7e-a99a-d13c7de8deaf", "CA", "CAD", "BANK_ACCOUNT", "INDIVIDUAL");

        HyperwalletTransferMethodConfigurationFieldQuery fieldQuery =
                new HyperwalletTransferMethodConfigurationFieldQuery("CA", "CAD", "BANK_ACCOUNT", "INDIVIDUAL");

        String resultQuery = fieldQuery.toQuery("usr-2ff94da8-f0f6-4d7e-a99a-d13c7de8deaf");

        assertThat(resultQuery, is(sampleFieldQuery));
    }

}