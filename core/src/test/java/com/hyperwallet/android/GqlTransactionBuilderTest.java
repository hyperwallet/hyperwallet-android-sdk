package com.hyperwallet.android;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import static com.hyperwallet.android.util.HttpMethod.POST;

import com.hyperwallet.android.listener.HyperwalletListener;
import com.hyperwallet.android.model.TypeReference;
import com.hyperwallet.android.model.graphql.HyperwalletTransferMethodConfigurationKey;
import com.hyperwallet.android.model.graphql.query.TransferMethodConfigurationKeysQuery;
import com.hyperwallet.android.rule.ExternalResourceManager;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Map;

public class GqlTransactionBuilderTest {

    @Rule
    public final MockitoRule mMockito = MockitoJUnit.rule();
    @Rule
    public final ExternalResourceManager mExternalResourceManager =
            new ExternalResourceManager();

    @Mock
    private HyperwalletListener<HyperwalletTransferMethodConfigurationKey> mListener;

    @Test
    public void testBuild_withRequiredParametersOnly() {
        TransferMethodConfigurationKeysQuery keysQuery =
                new TransferMethodConfigurationKeysQuery();

        GqlTransaction.Builder<HyperwalletTransferMethodConfigurationKey> builder = new GqlTransaction.Builder<>(
                keysQuery,
                new TypeReference<HyperwalletTransferMethodConfigurationKey>() {
                }, mListener);

        final GqlTransaction gqlTransaction = builder.build("test", "test-user-token",
                "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9");
        assertThat(gqlTransaction, is(notNullValue()));
        assertThat(gqlTransaction.getMethod(), is(POST));

        Map<String, String> headers = gqlTransaction.getHeaders();
        assertThat(headers, is(notNullValue()));
        assertThat(headers.get("Accept"), is("application/json"));
        assertThat(headers.get("Content-Type"), is("application/json"));
        assertThat(headers.get("Authorization"), is("Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9"));

        final String QUERY_VALUE = "query {\n"
                + "\tcountries(idToken: \"test-user-token\") {\n"
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
                + "}";

        assertThat(gqlTransaction.getPayload(), is(QUERY_VALUE));
    }
}
