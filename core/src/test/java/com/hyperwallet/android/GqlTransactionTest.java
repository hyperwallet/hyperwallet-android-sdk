package com.hyperwallet.android;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.verify;

import com.hyperwallet.android.exception.HyperwalletException;
import com.hyperwallet.android.listener.HyperwalletListener;
import com.hyperwallet.android.model.HyperwalletError;
import com.hyperwallet.android.model.TypeReference;
import com.hyperwallet.android.model.graphql.HyperwalletTransferMethodConfigurationKeyResult;
import com.hyperwallet.android.model.graphql.TransferMethodConfigurationResult;
import com.hyperwallet.android.model.graphql.query.HyperwalletTransferMethodConfigurationKeysQuery;
import com.hyperwallet.android.rule.HyperwalletExternalResourceManager;
import com.hyperwallet.android.util.HttpClient;
import com.hyperwallet.android.util.HttpMethod;

import org.json.JSONException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RobolectricTestRunner;

import java.io.IOException;
import java.net.HttpURLConnection;

@RunWith(RobolectricTestRunner.class)
public class GqlTransactionTest {

    @Rule
    public final MockitoRule mMockito = MockitoJUnit.rule();
    @Rule
    public final ExpectedException mThrown = ExpectedException.none();
    @Rule
    public HyperwalletExternalResourceManager mExternalResourceManager = new HyperwalletExternalResourceManager();

    @Mock
    private HyperwalletListener<HyperwalletTransferMethodConfigurationKeyResult> mListener;
    @Mock
    private HttpClient mHttpClient;

    @Captor
    private ArgumentCaptor<String> mPayloadCaptor;
    @Captor
    private ArgumentCaptor<HyperwalletException> mExceptionArgumentCaptor;


    @Test
    public void testPerformRequest_usingHttpPost() throws IOException {
        HyperwalletTransferMethodConfigurationKeysQuery keysQuery =
                new HyperwalletTransferMethodConfigurationKeysQuery();

        GqlTransaction.Builder<TransferMethodConfigurationResult> builder = new GqlTransaction.Builder<>(keysQuery,
                new TypeReference<TransferMethodConfigurationResult>() {
                }, mListener);
        final GqlTransaction gqlTransaction = builder.build("test", "usr-d8c65e1e-b3e5-460d-8b24-bee7cdae1636");
        assertThat(gqlTransaction.getMethod(), is(HttpMethod.POST));

        gqlTransaction.performRequest(mHttpClient);

        verify(mHttpClient).post(mPayloadCaptor.capture());
        String payload = mPayloadCaptor.getValue();
        String sampleQuery = "query {\n"
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
        assertThat(payload, is(notNullValue()));
        assertThat(payload, is(sampleQuery));
    }

    @Test
    public void testHandleErrors_responseParsedSuccessfully() throws Exception {
        String responseBody = mExternalResourceManager.getResourceContentError("tmc_gql_error_response.json");

        HyperwalletTransferMethodConfigurationKeysQuery keysQuery =
                new HyperwalletTransferMethodConfigurationKeysQuery();

        GqlTransaction.Builder<TransferMethodConfigurationResult> builder =
                new GqlTransaction.Builder<>(keysQuery,
                        new TypeReference<TransferMethodConfigurationResult>() {
                        }, mListener);
        final GqlTransaction gqlTransaction = builder.build("test", "usr-d8c65e1e-b3e5-460d-8b24-bee7cdae1636");

        gqlTransaction.handleErrors(HttpURLConnection.HTTP_BAD_REQUEST, responseBody);
        verify(mListener).onFailure(mExceptionArgumentCaptor.capture());

        HyperwalletException exception = mExceptionArgumentCaptor.getValue();
        assertThat(exception.getHyperwalletErrors().getErrors().size(), is(1));
        HyperwalletError error = exception.getHyperwalletErrors().getErrors().get(0);
        assertThat(error.getCode(), is("DataFetchingException"));
        assertThat(error.getMessage(), is("Could not find any currency."));
    }

    @Test
    public void testHandleErrors_errorWhenParsingResponse() throws Exception {
        mThrown.expect(JSONException.class);
        mThrown.expectMessage("Value some of type java.lang.String cannot be converted to JSONObject");

        HyperwalletTransferMethodConfigurationKeysQuery keysQuery =
                new HyperwalletTransferMethodConfigurationKeysQuery();

        GqlTransaction.Builder<TransferMethodConfigurationResult> builder = new GqlTransaction.Builder<>(keysQuery,
                new TypeReference<TransferMethodConfigurationResult>() {
                }, mListener);
        final GqlTransaction gqlTransaction = builder.build("test", "usr-d8c65e1e-b3e5-460d-8b24-bee7cdae1636");

        gqlTransaction.handleErrors(HttpURLConnection.HTTP_BAD_REQUEST, "some non-parcelable error");
    }
}