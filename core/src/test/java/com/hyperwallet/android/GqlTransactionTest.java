package com.hyperwallet.android;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.res.Resources;

import com.hyperwallet.android.exception.HyperwalletException;
import com.hyperwallet.android.exception.HyperwalletRestException;
import com.hyperwallet.android.listener.HyperwalletListener;
import com.hyperwallet.android.model.Error;
import com.hyperwallet.android.model.Errors;
import com.hyperwallet.android.model.TypeReference;
import com.hyperwallet.android.model.graphql.HyperwalletTransferMethodConfigurationKey;
import com.hyperwallet.android.model.graphql.query.TransferMethodConfigurationKeysQuery;
import com.hyperwallet.android.rule.ExternalResourceManager;
import com.hyperwallet.android.util.HttpClient;
import com.hyperwallet.android.util.HttpMethod;
import com.hyperwallet.android.sdk.R;

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
import java.util.List;

@RunWith(RobolectricTestRunner.class)
public class GqlTransactionTest {

    @Rule
    public final MockitoRule mMockito = MockitoJUnit.rule();
    @Rule
    public final ExpectedException mThrown = ExpectedException.none();
    @Rule
    public ExternalResourceManager mExternalResourceManager = new ExternalResourceManager();

    @Mock
    private HyperwalletListener<HyperwalletTransferMethodConfigurationKey> mListener;
    @Mock
    private HttpClient mHttpClient;
    @Mock
    private Resources mResources;

    @Captor
    private ArgumentCaptor<String> mPayloadCaptor;
    @Captor
    private ArgumentCaptor<HyperwalletException> mExceptionArgumentCaptor;


    @Test
    public void testPerformRequest_usingHttpPost() throws IOException {
        TransferMethodConfigurationKeysQuery keysQuery = new TransferMethodConfigurationKeysQuery();

        GqlTransaction.Builder<HyperwalletTransferMethodConfigurationKey> builder = new GqlTransaction.Builder<>(
                keysQuery,
                new TypeReference<HyperwalletTransferMethodConfigurationKey>() {
                }, mListener);
        final GqlTransaction gqlTransaction = builder.build("test", "test-user-token",
                "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9");
        assertThat(gqlTransaction.getMethod(), is(HttpMethod.POST));

        gqlTransaction.performRequest(mHttpClient);

        verify(mHttpClient).post(mPayloadCaptor.capture());
        String payload = mPayloadCaptor.getValue();
        String sampleQuery = "query {\n"
                + "\tcountries(idToken: \"test-user-token\") {\n"
                + "\t\tnodes {\n"
                + "\t\t\tcode\n"
                + "\t\t\tname\n"
                + "\t\t\tcurrencies {\n"
                + "\t\t\t\tnodes {\n"
                + "\t\t\t\t\tcode\n"
                + "\t\t\t\t\tname\n"
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

        TransferMethodConfigurationKeysQuery keysQuery = new TransferMethodConfigurationKeysQuery();

        GqlTransaction.Builder<HyperwalletTransferMethodConfigurationKey> builder =
                new GqlTransaction.Builder<>(keysQuery,
                        new TypeReference<HyperwalletTransferMethodConfigurationKey>() {
                        }, mListener);
        final GqlTransaction gqlTransaction = builder.build("test", "test-user-token",
                "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9");

        gqlTransaction.handleErrors(HttpURLConnection.HTTP_BAD_REQUEST, responseBody);
        verify(mListener).onFailure(mExceptionArgumentCaptor.capture());

        HyperwalletException exception = mExceptionArgumentCaptor.getValue();
        assertThat(exception.getErrors().getErrors().size(), is(1));
        Error error = exception.getErrors().getErrors().get(0);
        assertThat(error.getCode(), is("DataFetchingException"));
        assertThat(error.getMessage(), is("Could not find any currency."));
    }

    @Test
    public void testHandleErrors_responseUnauthorized() throws Exception {
        when(mResources.getString(R.string.authentication_token_provider_exception)).thenReturn(
                "Authentication token retrieval attempt resulted in an error");
        String responseBody = mExternalResourceManager.getResourceContentError("jwt_token_expired.json");

        TransferMethodConfigurationKeysQuery keysQuery = new TransferMethodConfigurationKeysQuery();

        GqlTransaction.Builder<HyperwalletTransferMethodConfigurationKey> builder =
                new GqlTransaction.Builder<>(keysQuery,
                        new TypeReference<HyperwalletTransferMethodConfigurationKey>() {
                        }, mListener);
        final GqlTransaction gqlTransaction = builder.build("test", "test-user-token",
                "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9");

        gqlTransaction.handleErrors(HttpURLConnection.HTTP_UNAUTHORIZED, responseBody);
        verify(mListener).onFailure(mExceptionArgumentCaptor.capture());

        HyperwalletException exception = mExceptionArgumentCaptor.getValue();
        assertThat(exception.getErrors().getErrors().size(), is(1));
        Error error = exception.getErrors().getErrors().get(0);

        assertThat(error.getCode(), is(equalTo("EC_AUTHENTICATION_TOKEN_PROVIDER_EXCEPTION")));
        assertThat(error.getMessageFromResourceWhenAvailable(mResources),
                is(equalTo("Authentication token retrieval attempt resulted in an error")));
    }

    @Test
    public void testHandleErrors_errorWhenParsingResponse() throws Exception {
        mThrown.expect(JSONException.class);
        mThrown.expectMessage("Value some of type java.lang.String cannot be converted to JSONObject");

        TransferMethodConfigurationKeysQuery keysQuery = new TransferMethodConfigurationKeysQuery();

        GqlTransaction.Builder<HyperwalletTransferMethodConfigurationKey> builder = new GqlTransaction.Builder<>(
                keysQuery,
                new TypeReference<HyperwalletTransferMethodConfigurationKey>() {
                }, mListener);
        final GqlTransaction gqlTransaction = builder.build("test", "test-user-token",
                "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9");

        gqlTransaction.handleErrors(HttpURLConnection.HTTP_BAD_REQUEST, "some non-parcelable error");
    }
}