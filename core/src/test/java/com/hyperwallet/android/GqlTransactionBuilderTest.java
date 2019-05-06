package com.hyperwallet.android;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import static com.hyperwallet.android.util.HttpMethod.POST;

import com.hyperwallet.android.listener.HyperwalletListener;
import com.hyperwallet.android.model.TypeReference;
import com.hyperwallet.android.model.meta.HyperwalletTransferMethodConfigurationKeyResult;
import com.hyperwallet.android.model.meta.TransferMethodConfigurationResult;
import com.hyperwallet.android.model.meta.query.HyperwalletTransferMethodConfigurationKeysQuery;
import com.hyperwallet.android.rule.HyperwalletExternalResourceManager;

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
    public final HyperwalletExternalResourceManager mExternalResourceManager =
            new HyperwalletExternalResourceManager();

    @Mock
    private HyperwalletListener<HyperwalletTransferMethodConfigurationKeyResult> mListener;

    @Test
    public void testBuild_withRequiredParametersOnly() {
        HyperwalletTransferMethodConfigurationKeysQuery keysQuery =
                new HyperwalletTransferMethodConfigurationKeysQuery();

        GqlTransaction.Builder<TransferMethodConfigurationResult> builder = new GqlTransaction.Builder<>(keysQuery,
                new TypeReference<TransferMethodConfigurationResult>() {
                }, mListener);

        final GqlTransaction gqlTransaction = builder.build("test", "usr-d8c65e1e-b3e5-460d-8b24-bee7cdae1636");

        assertThat(gqlTransaction, is(notNullValue()));
        assertThat(gqlTransaction.getMethod(), is(POST));

        Map<String, String> headers = gqlTransaction.getHeaders();
        assertThat(headers, is(notNullValue()));
        assertThat(headers.get("Accept"), is("application/json"));
        assertThat(headers.get("Content-Type"), is("application/json"));

        final String QUERY_VALUE = "query {\n"
                + "\tcountries(idToken: \"usr-d8c65e1e-b3e5-460d-8b24-bee7cdae1636\") {\n"
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
                + "\t\t\t\t\t\t\tprocessingTime\n"
                + "\t\t\t\t\t\t\tfees {\n"
                + "\t\t\t\t\t\t\t\tnodes {\n"
                + "\t\t\t\t\t\t\t\t\tcountry\n"
                + "\t\t\t\t\t\t\t\t\tcurrency\n"
                + "\t\t\t\t\t\t\t\t\ttransferMethodType\n"
                + "\t\t\t\t\t\t\t\t\tvalue\n"
                + "\t\t\t\t\t\t\t\t\tfeeRateType\n"
                + "\t\t\t\t\t\t\t\t\tmaximum\n"
                + "\t\t\t\t\t\t\t\t\tminimum\n"
                + "\t\t\t\t\t\t\t\t\t}\n"
                + "\t\t\t\t\t\t\t\t}\n"
                + "\t\t\t\t\t\t}\n"
                + "\t\t\t\t\t}\n"
                + "\t\t\t\t}\n"
                + "\t\t\t}\n"
                + "\t\t}\n"
                + "\t}\n"
                + "}";

        assertThat(gqlTransaction.getPayload(), is(QUERY_VALUE));
    }
}
