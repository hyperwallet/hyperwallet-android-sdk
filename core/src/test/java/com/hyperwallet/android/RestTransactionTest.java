package com.hyperwallet.android;

import android.content.res.Resources;
import android.os.Handler;

import com.hyperwallet.android.exception.HyperwalletException;
import com.hyperwallet.android.exception.HyperwalletGqlException;
import com.hyperwallet.android.listener.HyperwalletListener;
import com.hyperwallet.android.model.Error;
import com.hyperwallet.android.model.Errors;
import com.hyperwallet.android.model.TypeReference;
import com.hyperwallet.android.model.graphql.HyperwalletTransferMethodConfigurationKey;
import com.hyperwallet.android.model.graphql.error.GqlErrors;
import com.hyperwallet.android.model.graphql.query.TransferMethodConfigurationKeysQuery;
import com.hyperwallet.android.model.transfermethod.BankAccount;
import com.hyperwallet.android.rule.ExternalResourceManager;
import com.hyperwallet.android.util.HttpClient;
import com.hyperwallet.android.util.JsonUtils;
import com.hyperwallet.android.sdk.R;

import org.hamcrest.CoreMatchers;
import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RobolectricTestRunner;

import java.net.HttpURLConnection;
import java.util.UUID;

import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodTypes.BANK_ACCOUNT;
import static com.hyperwallet.android.util.HttpMethod.GET;
import static com.hyperwallet.android.util.HttpMethod.POST;
import static com.hyperwallet.android.util.HttpMethod.PUT;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class RestTransactionTest {

    @Rule
    public final MockitoRule mMockito = MockitoJUnit.rule();
    @Rule
    public ExternalResourceManager mExternalResourceManager = new ExternalResourceManager();

    @Mock
    private HyperwalletListener<BankAccount> mListener;
    @Mock
    private HttpClient mHttpClient;
    @Mock
    private Resources mResources;


    @Captor
    private ArgumentCaptor<String> mPayloadCaptor;
    @Captor
    private ArgumentCaptor<HyperwalletException> mExceptionArgumentCaptor;

    @Captor
    private ArgumentCaptor<Runnable> mRunnableCaptor;
    @Captor
    private ArgumentCaptor<Runnable> mErrorRunnableCaptor;

    private final String contextId = UUID.randomUUID().toString();

    @Test
    public void testPerformRequest_usingHttpGet() throws Exception {
        final PathFormatter pathFormatter = new PathFormatter("users/{0}/bank-accounts");
        final String token = "eyJhbGciOiJIUzI1NiJ9.eyJncmFwaHFsLXVyaSI6Imh0dHA6XC9cLzEyNy4wLjAuMTo1MzEyN1wvZ3JhcGhxb";
        BankAccount.Builder bankAccountBuilder = new BankAccount.Builder("US", "USD",
                "8017110254");
        final BankAccount bankAccount = bankAccountBuilder.build();

        RestTransaction.Builder<BankAccount> accountBuilder =
                new RestTransaction.Builder<>(GET, pathFormatter, new TypeReference<BankAccount>() {
                }, mListener, contextId);
        final RestTransaction restTransaction = accountBuilder
                .jsonModel(bankAccount)
                .build("http://hyperwallet.com/rest/v3/", token, "test-user-token");
        assertThat(restTransaction.getMethod(), is(GET));
        assertThat(restTransaction.getListener(), CoreMatchers.<HyperwalletListener>is(mListener));
        assertThat(restTransaction.getPath(), is("users/test-user-token/bank-accounts"));

        restTransaction.performRequest(mHttpClient);
        verify(mHttpClient).get();
    }

    @Test
    public void testPerformRequest_usingHttpPost() throws Exception {
        final PathFormatter pathFormatter = new PathFormatter("users/{0}/bank-accounts");
        final String token = "eyJhbGciOiJIUzI1NiJ9.eyJncmFwaHFsLXVyaSI6Imh0dHA6XC9cLzEyNy4wLjAuMTo1MzEyN1wvZ3JhcGhxb";
        BankAccount.Builder bankAccountBuilder = new BankAccount.Builder("US", "USD",
                "8017110254");
        final BankAccount bankAccount = bankAccountBuilder.build();

        RestTransaction.Builder<BankAccount> accountBuilder =
                new RestTransaction.Builder<>(POST, pathFormatter, new TypeReference<BankAccount>() {
                }, mListener, contextId);
        final RestTransaction restTransaction = accountBuilder
                .jsonModel(bankAccount)
                .build("http://hyperwallet.com/rest/v3/", token, "test-user-token");
        assertThat(restTransaction.getMethod(), is(POST));

        restTransaction.performRequest(mHttpClient);
        verify(mHttpClient).post(mPayloadCaptor.capture());

        JSONObject resultPayload = new JSONObject(restTransaction.getPayload());
        assertThat(resultPayload.getString("type"), is(BANK_ACCOUNT));
        assertThat(resultPayload.getString("transferMethodCurrency"), is("USD"));
        assertThat(resultPayload.getString("transferMethodCountry"), is("US"));
        assertThat(resultPayload.getString("bankAccountId"), is("8017110254"));
    }

    @Test
    public void testPerformRequest_usingHttpPut() throws Exception {
        final PathFormatter pathFormatter = new PathFormatter("users/{0}/bank-accounts");
        final String token = "eyJhbGciOiJIUzI1NiJ9.eyJncmFwaHFsLXVyaSI6Imh0dHA6XC9cLzEyNy4wLjAuMTo1MzEyN1wvZ3JhcGhxb";
        BankAccount.Builder bankAccountBuilder = new BankAccount.Builder("US", "USD",
                "8017110254");
        final BankAccount bankAccount = bankAccountBuilder.build();

        RestTransaction.Builder<BankAccount> accountBuilder =
                new RestTransaction.Builder<>(PUT, pathFormatter, new TypeReference<BankAccount>() {
                }, mListener, contextId);
        final RestTransaction restTransaction = accountBuilder
                .jsonModel(bankAccount)
                .build("http://hyperwallet.com/rest/v3/", token, "test-user-token");
        assertThat(restTransaction.getMethod(), is(PUT));

        restTransaction.performRequest(mHttpClient);

        verify(mHttpClient).put(mPayloadCaptor.capture());

        String payload = mPayloadCaptor.getValue();
        JSONObject resultPayload = new JSONObject(payload);
        assertThat(resultPayload.getString("type"), is(BANK_ACCOUNT));
        assertThat(resultPayload.getString("transferMethodCurrency"), is("USD"));
        assertThat(resultPayload.getString("transferMethodCountry"), is("US"));
        assertThat(resultPayload.getString("bankAccountId"), is("8017110254"));
    }

    @Test
    public void testOnFailure_whenHandlerIsNull() throws Exception {
        final PathFormatter pathFormatter = new PathFormatter("users/{0}/bank-accounts");
        final String token = "eyJhbGciOiJIUzI1NiJ9.eyJncmFwaHFsLXVyaSI6Imh0dHA6XC9cLzEyNy4wLjAuMTo1MzEyN1wvZ3JhcGhxb";

        RestTransaction.Builder<BankAccount> accountBuilder =
                new RestTransaction.Builder<>(GET, pathFormatter, new TypeReference<BankAccount>() {
                }, mListener, contextId);
        final RestTransaction restTransaction = accountBuilder
                .build("http://hyperwallet.com/rest/v3/", token, "test-user-token");

        GqlErrors gqlErrors = JsonUtils.fromJsonString(mExternalResourceManager.getResourceContentError(
                "gql_error_response.json"), new TypeReference<GqlErrors>() {
        });
        when(mListener.getHandler()).thenReturn(null);
        HyperwalletGqlException gqlException = new HyperwalletGqlException(HttpURLConnection.HTTP_BAD_REQUEST, gqlErrors);
        restTransaction.onFailure(gqlException);
        verify(mListener).onFailure(gqlException);
    }

    @Test
    public void testOnFailure_whenHandlerIsNotNull() throws Exception {
        final PathFormatter pathFormatter = new PathFormatter("users/{0}/bank-accounts");
        final String token = "eyJhbGciOiJIUzI1NiJ9.eyJncmFwaHFsLXVyaSI6Imh0dHA6XC9cLzEyNy4wLjAuMTo1MzEyN1wvZ3JhcGhxb";

        RestTransaction.Builder<BankAccount> accountBuilder =
                new RestTransaction.Builder<>(GET, pathFormatter, new TypeReference<BankAccount>() {
                }, mListener, contextId);
        final RestTransaction restTransaction = accountBuilder
                .build("http://hyperwallet.com/rest/v3/", token, "test-user-token");

        GqlErrors gqlErrors = JsonUtils.fromJsonString(mExternalResourceManager.getResourceContentError(
                "gql_error_response.json"), new TypeReference<GqlErrors>() {
        });
        Handler handler = mock(Handler.class);
        when(mListener.getHandler()).thenReturn(handler);
        HyperwalletGqlException gqlException = new HyperwalletGqlException(HttpURLConnection.HTTP_BAD_REQUEST, gqlErrors);
        restTransaction.onFailure(gqlException);

        verify(handler).post(any(Runnable.class));
    }

    @Test
    public void testHandleErrors_responseUnauthorized() throws Exception {
        when(mResources.getString(R.string.authentication_token_provider_exception)).thenReturn(
                "Authentication token retrieval attempt resulted in an error");
        final PathFormatter pathFormatter = new PathFormatter("users/{0}/bank-accounts");
        final String token = "eyJhbGciOiJIUzI1NiJ9.eyJncmFwaHFsLXVyaSI6Imh0dHA6XC9cLzEyNy4wLjAuMTo1MzEyN1wvZ3JhcGhxb";
        String responseBody = mExternalResourceManager.getResourceContentError("jwt_token_expired.json");

        TransferMethodConfigurationKeysQuery keysQuery = new TransferMethodConfigurationKeysQuery();

        RestTransaction.Builder<BankAccount> accountBuilder =
                new RestTransaction.Builder<>(GET, pathFormatter, new TypeReference<BankAccount>() {
                }, mListener, contextId);
        final RestTransaction restTransaction = accountBuilder
                .build("http://hyperwallet.com/rest/v3/", token, "test-user-token");

        restTransaction.handleErrors(HttpURLConnection.HTTP_UNAUTHORIZED, responseBody);
        verify(mListener).onFailure(mExceptionArgumentCaptor.capture());

        HyperwalletException exception = mExceptionArgumentCaptor.getValue();
        assertThat(exception.getErrors().getErrors().size(), is(1));
        Error error = exception.getErrors().getErrors().get(0);

        assertThat(error.getCode(), is(equalTo("EC_AUTHENTICATION_TOKEN_PROVIDER_EXCEPTION")));
        assertThat(error.getMessageFromResourceWhenAvailable(mResources),
                is(equalTo("Authentication token retrieval attempt resulted in an error")));
    }

    @Test
    public void testRunRequest_whenHttpClientIsInvalid() throws Exception {
        final PathFormatter pathFormatter = new PathFormatter("users/{0}/bank-accounts");
        final String token = "eyJhbGciOiJIUzI1NiJ9.eyJncmFwaHFsLXVyaSI6Imh0dHA6XC9cLzEyNy4wLjAuMTo1MzEyN1wvZ3JhcGhxb";

        RestTransaction.Builder<BankAccount> accountBuilder =
                new RestTransaction.Builder<>(GET, pathFormatter, new TypeReference<BankAccount>() {
                }, mListener, contextId);
        final RestTransaction restTransaction = accountBuilder
                .build("http://hyperwallet.com/rest/v3/", token, "test-user-token");

        restTransaction.run();
        verify(mListener).onFailure(any(HyperwalletException.class));
    }

    @Test
    public void testRunRequest_checkFailRunnable() throws Exception {
        final PathFormatter pathFormatter = new PathFormatter("users/{0}/bank-accounts");
        final String token = "eyJhbGciOiJIUzI1NiJ9.eyJncmFwaHFsLXVyaSI6Imh0dHA6XC9cLzEyNy4wLjAuMTo1MzEyN1wvZ3JhcGhxb";

        RestTransaction.Builder<BankAccount> accountBuilder =
                new RestTransaction.Builder<>(GET, pathFormatter, new TypeReference<BankAccount>() {
                }, mListener, contextId);
        final RestTransaction restTransaction = accountBuilder
                .build("http://hyperwallet.com/rest/v3/", token, "test-user-token");

        Handler handler = mock(Handler.class);
        when(mListener.getHandler()).thenReturn(handler);
        GqlErrors gqlErrors = JsonUtils.fromJsonString(mExternalResourceManager.getResourceContentError(
                "gql_error_response.json"), new TypeReference<GqlErrors>() {
        });
        HyperwalletGqlException gqlException = new HyperwalletGqlException(HttpURLConnection.HTTP_BAD_REQUEST, gqlErrors);
        restTransaction.onFailure(gqlException);

        verify(handler).post(mRunnableCaptor.capture());

        Runnable r = mRunnableCaptor.getValue();
        r.run();
        verify(mListener).onFailure(any(HyperwalletException.class));
    }

    @Test
    public void testRunRequest_checkSuccessRunnable() throws Exception {
        final PathFormatter pathFormatter = new PathFormatter("users/{0}/bank-accounts");
        final String token = "eyJhbGciOiJIUzI1NiJ9.eyJncmFwaHFsLXVyaSI6Imh0dHA6XC9cLzEyNy4wLjAuMTo1MzEyN1wvZ3JhcGhxb";

        RestTransaction.Builder<BankAccount> accountBuilder =
                new RestTransaction.Builder<>(GET, pathFormatter, new TypeReference<BankAccount>() {
                }, mListener, contextId);
        final RestTransaction restTransaction = accountBuilder
                .build("http://hyperwallet.com/rest/v3/", token, "test-user-token");

        Handler handler = mock(Handler.class);
        when(mListener.getHandler()).thenReturn(handler);
        restTransaction.onSuccess(null);

        verify(handler).post(mRunnableCaptor.capture());

        Runnable r = mRunnableCaptor.getValue();
        r.run();
        verify(mListener).onSuccess(null);
    }

    @Test
    public void testRunRequest_checkSuccessRunnableEmpty() throws Exception {
        final PathFormatter pathFormatter = new PathFormatter("users/{0}/bank-accounts");
        final String token = "eyJhbGciOiJIUzI1NiJ9.eyJncmFwaHFsLXVyaSI6Imh0dHA6XC9cLzEyNy4wLjAuMTo1MzEyN1wvZ3JhcGhxb";

        RestTransaction.Builder<BankAccount> accountBuilder =
                new RestTransaction.Builder<>(GET, pathFormatter, new TypeReference<BankAccount>() {
                }, mListener, contextId);
        final RestTransaction restTransaction = accountBuilder
                .build("http://hyperwallet.com/rest/v3/", token, "test-user-token");

        Handler handler = mock(Handler.class);
        when(mListener.getHandler()).thenReturn(handler);
        restTransaction.onSuccess(" ");

        verify(handler).post(mRunnableCaptor.capture());

        Runnable r = mRunnableCaptor.getValue();
        r.run();
        verify(mListener).onSuccess(null);
    }

    @Test
    public void testRunRequest_checkSuccessWithNullHandlerRunnable() throws Exception {
        final PathFormatter pathFormatter = new PathFormatter("users/{0}/bank-accounts");
        final String token = "eyJhbGciOiJIUzI1NiJ9.eyJncmFwaHFsLXVyaSI6Imh0dHA6XC9cLzEyNy4wLjAuMTo1MzEyN1wvZ3JhcGhxb";
        final String response = mExternalResourceManager.getResourceContent(
                "tmc_configuration_connection_response.json");

        RestTransaction.Builder<BankAccount> accountBuilder =
                new RestTransaction.Builder<>(GET, pathFormatter, new TypeReference<BankAccount>() {
                }, mListener, contextId);
        final RestTransaction restTransaction = accountBuilder
                .build("http://hyperwallet.com/rest/v3/", token, "test-user-token");

        Handler handler = mock(Handler.class);
        when(mListener.getHandler()).thenReturn(handler);
        restTransaction.onSuccess(response);

        verify(handler).post(mRunnableCaptor.capture());

        Runnable r = mRunnableCaptor.getValue();
        r.run();
        verify(mListener).onSuccess((BankAccount) any());
    }

    @Test
    public void testRunRequest_checkFailWithNullHandlerRunnable() throws Exception {
        final PathFormatter pathFormatter = new PathFormatter("users/{0}/bank-accounts");
        final String token = "eyJhbGciOiJIUzI1NiJ9.eyJncmFwaHFsLXVyaSI6Imh0dHA6XC9cLzEyNy4wLjAuMTo1MzEyN1wvZ3JhcGhxb";

        RestTransaction.Builder<BankAccount> accountBuilder =
                new RestTransaction.Builder<>(GET, pathFormatter, new TypeReference<BankAccount>() {
                }, mListener, contextId);
        final RestTransaction restTransaction = accountBuilder
                .build("http://hyperwallet.com/rest/v3/", token, "test-user-token");

        Handler handler = mock(Handler.class);
        when(mListener.getHandler()).thenReturn(handler);
        restTransaction.onSuccess("some string");

        verify(handler).post(mRunnableCaptor.capture());

        Runnable r = mRunnableCaptor.getValue();
        r.run();
        verify(mListener, never()).onSuccess((BankAccount) any());
    }
}