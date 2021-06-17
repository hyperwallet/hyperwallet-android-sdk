package com.hyperwallet.android.model.graphql.query;

import androidx.annotation.NonNull;

/**
 * The {@code TransferMethodConfigurationTransferTypesFeeAndProcessingTimeQuery} class defines and builds a query to retrieve the key set
 * that is required to construct a {@code TransferMethodConfigurationTransferTypesFeeAndProcessingTimeQuery}.
 *
 * <p>The query will retrieve the transfer method types, processing time and fees associated for given
 *   country, currency, and profile tuple.</p>
 */
public class TransferMethodConfigurationTransferTypesFeeAndProcessingTimeQuery implements GqlQuery {

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
     * Create a new {@code TransferMethodConfigurationTransferTypesFeeAndProcessingTimeQuery} from the country, currency,
     * and profile.
     *
     * @param country            the 2 letter ISO 3166-1 country code
     * @param currency           the 3 letter ISO 4217-1 currency code
     */
    public TransferMethodConfigurationTransferTypesFeeAndProcessingTimeQuery(@NonNull String country, @NonNull String currency) {
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
