package com.hyperwallet.android.model.graphql.query;

import androidx.annotation.NonNull;

public class TransferMethodConfigurationFeeAndProcessingTimeQuery implements GqlQuery {

    private static final String GRAPH_QL_REQUEST ="query QueryFeeAndProcessing(\n"
            + "    $idToken: String = \"%s\"\n,"
            + "    $country: Country = %s\n,"
            + "    $currency: Currency = %s\n"
            + ") {\n"
            + "  countries (idToken: $idToken, code: $country){\n"
            + "              nodes {\n"
            + "                code\n"
            + "                name\n"
            + "                currencies (code: $currency){\n"
            + "                  nodes {\n"
            + "                    code\n"
            + "                    name\n"
            + "                    transferMethodTypes {\n"
            + "                      nodes {\n"
            + "                        code\n"
            + "                        name\n"
            + "                        processingTimes {\n"
            + "                          nodes {\n"
            + "                            country\n"
            + "                            currency\n"
            + "                            transferMethodType\n"
            + "                            value\n"
            + "                          }\n"
            + "                        }\n"
            + "                        fees {\n"
            + "                          nodes {\n"
            + "                            currency\n"
            + "                            feeRateType\n"
            + "                            value\n"
            + "                            minimum\n"
            + "                            maximum\n"
            + "                          }\n"
            + "                        }\n"
            + "                      }\n"
            + "                    }\n"
            + "                  }\n"
            + "                }\n"
            + "              }\n"
            + "            }\n"
            + "}\n";

    private final String mCountry;
    private final String mCurrency;

    /**
     * Create a new {@code TransferMethodConfigurationQuery} from the country, currency, transferMethodType
     * and profile.
     *
     * @param country            the 2 letter ISO 3166-1 country code
     * @param currency           the 3 letter ISO 4217-1 currency code
     */
    public TransferMethodConfigurationFeeAndProcessingTimeQuery(@NonNull String country, @NonNull String currency) {
        mCountry = country;
        mCurrency = currency;
    }


    /**
     * {@inheritDoc}
     *
     * @param userToken {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public String toQuery(String userToken) {
        return String.format(GRAPH_QL_REQUEST, userToken, mCountry, mCurrency);
    }
}
