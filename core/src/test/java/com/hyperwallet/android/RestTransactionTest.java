package com.hyperwallet.android;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static com.hyperwallet.android.util.HttpMethod.GET;
import static com.hyperwallet.android.util.HttpMethod.POST;
import static com.hyperwallet.android.util.HttpMethod.PUT;

import android.os.Handler;

import com.hyperwallet.android.exception.HyperwalletException;
import com.hyperwallet.android.exception.HyperwalletGqlException;
import com.hyperwallet.android.listener.HyperwalletListener;
import com.hyperwallet.android.model.TypeReference;
import com.hyperwallet.android.model.graphql.HyperwalletTransferMethodConfigurationKeyResult;
import com.hyperwallet.android.model.graphql.error.GqlErrors;
import com.hyperwallet.android.model.transfermethod.HyperwalletBankAccount;
import com.hyperwallet.android.rule.HyperwalletExternalResourceManager;
import com.hyperwallet.android.util.HttpClient;
import com.hyperwallet.android.util.JsonUtils;

import org.hamcrest.CoreMatchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class RestTransactionTest {

    @Rule
    public final MockitoRule mMockito = MockitoJUnit.rule();
    @Rule
    public HyperwalletExternalResourceManager mExternalResourceManager = new HyperwalletExternalResourceManager();

    @Mock
    private HyperwalletListener<HyperwalletTransferMethodConfigurationKeyResult> mListener;
    @Mock
    private HttpClient mHttpClient;

    @Captor
    private ArgumentCaptor<String> mPayloadCaptor;

    @Captor
    private ArgumentCaptor<Runnable> mRunnableCaptor;
    @Captor
    private ArgumentCaptor<Runnable> mErrorRunnableCaptor;

    @Test
    public void testPerformRequest_usingHttpGet() throws Exception {
        final PathFormatter pathFormatter = new PathFormatter("users/{0}/bank-accounts");
        final String token = "eyJhbGciOiJIUzI1NiJ9.eyJncmFwaHFsLXVyaSI6Imh0dHA6XC9cLzEyNy4wLjAuMTo1MzEyN1wvZ3JhcGhxb";
        HyperwalletBankAccount.Builder bankAccountBuilder = new HyperwalletBankAccount.Builder("US", "USD",
                "8017110254");
        final HyperwalletBankAccount bankAccount = bankAccountBuilder.build();

        RestTransaction.Builder<HyperwalletBankAccount> accountBuilder =
                new RestTransaction.Builder<>(GET, pathFormatter, new TypeReference<HyperwalletBankAccount>() {
                }, mListener);
        final RestTransaction restTransaction = accountBuilder
                .jsonModel(bankAccount)
                .build("http://hyperwallet.com/rest/v3/", token, "usr-fbfd5848-60d0-43c5-8462-099c959b49c7");
        assertThat(restTransaction.getMethod(), is(GET));
        assertThat(restTransaction.getListener(), CoreMatchers.<HyperwalletListener>is(mListener));
        assertThat(restTransaction.getPath(), is("users/usr-fbfd5848-60d0-43c5-8462-099c959b49c7/bank-accounts"));

        restTransaction.performRequest(mHttpClient);
        verify(mHttpClient).get();
    }

    @Test
    public void testPerformRequest_usingHttpPost() throws Exception {
        final PathFormatter pathFormatter = new PathFormatter("users/{0}/bank-accounts");
        final String token = "eyJhbGciOiJIUzI1NiJ9.eyJncmFwaHFsLXVyaSI6Imh0dHA6XC9cLzEyNy4wLjAuMTo1MzEyN1wvZ3JhcGhxb";
        HyperwalletBankAccount.Builder bankAccountBuilder = new HyperwalletBankAccount.Builder("US", "USD",
                "8017110254");
        final HyperwalletBankAccount bankAccount = bankAccountBuilder.build();

        RestTransaction.Builder<HyperwalletBankAccount> accountBuilder =
                new RestTransaction.Builder<>(POST, pathFormatter, new TypeReference<HyperwalletBankAccount>() {
                }, mListener);
        final RestTransaction restTransaction = accountBuilder
                .jsonModel(bankAccount)
                .build("http://hyperwallet.com/rest/v3/", token, "usr-fbfd5848-60d0-43c5-8462-099c959b49c7");
        assertThat(restTransaction.getMethod(), is(POST));

        restTransaction.performRequest(mHttpClient);
        verify(mHttpClient).post(mPayloadCaptor.capture());

        String payload = mPayloadCaptor.getValue();
        assertThat(payload, is("{\"transferMethodCurrency\":\"USD\","
                + "\"transferMethodCountry\":\"US\",\"bankAccountId\":\"8017110254\",\"type\":\"BANK_ACCOUNT\"}"));
    }

    @Test
    public void testPerformRequest_usingHttpPut() throws Exception {
        final PathFormatter pathFormatter = new PathFormatter("users/{0}/bank-accounts");
        final String token = "eyJhbGciOiJIUzI1NiJ9.eyJncmFwaHFsLXVyaSI6Imh0dHA6XC9cLzEyNy4wLjAuMTo1MzEyN1wvZ3JhcGhxb";
        HyperwalletBankAccount.Builder bankAccountBuilder = new HyperwalletBankAccount.Builder("US", "USD",
                "8017110254");
        final HyperwalletBankAccount bankAccount = bankAccountBuilder.build();

        RestTransaction.Builder<HyperwalletBankAccount> accountBuilder =
                new RestTransaction.Builder<>(PUT, pathFormatter, new TypeReference<HyperwalletBankAccount>() {
                }, mListener);
        final RestTransaction restTransaction = accountBuilder
                .jsonModel(bankAccount)
                .build("http://hyperwallet.com/rest/v3/", token, "usr-fbfd5848-60d0-43c5-8462-099c959b49c7");
        assertThat(restTransaction.getMethod(), is(PUT));

        restTransaction.performRequest(mHttpClient);

        verify(mHttpClient).put(mPayloadCaptor.capture());

        String payload = mPayloadCaptor.getValue();
        assertThat(payload, is("{\"transferMethodCurrency\":\"USD\","
                + "\"transferMethodCountry\":\"US\",\"bankAccountId\":\"8017110254\",\"type\":\"BANK_ACCOUNT\"}"));
    }

    @Test
    public void testOnFailure_whenHandlerIsNull() throws Exception {
        final PathFormatter pathFormatter = new PathFormatter("users/{0}/bank-accounts");
        final String token = "eyJhbGciOiJIUzI1NiJ9.eyJncmFwaHFsLXVyaSI6Imh0dHA6XC9cLzEyNy4wLjAuMTo1MzEyN1wvZ3JhcGhxb";

        RestTransaction.Builder<HyperwalletBankAccount> accountBuilder =
                new RestTransaction.Builder<>(GET, pathFormatter, new TypeReference<HyperwalletBankAccount>() {
                }, mListener);
        final RestTransaction restTransaction = accountBuilder
                .build("http://hyperwallet.com/rest/v3/", token, "usr-fbfd5848-60d0-43c5-8462-099c959b49c7");

        GqlErrors gqlErrors = JsonUtils.fromJsonString(mExternalResourceManager.getResourceContentError(
                "gql_error_response.json"), new TypeReference<GqlErrors>() {
        });
        when(mListener.getHandler()).thenReturn(null);
        HyperwalletGqlException gqlException = new HyperwalletGqlException(gqlErrors);
        restTransaction.onFailure(gqlException);
        verify(mListener).onFailure(gqlException);
    }

    @Test
    public void testOnFailure_whenHandlerIsNotNull() throws Exception {
        final PathFormatter pathFormatter = new PathFormatter("users/{0}/bank-accounts");
        final String token = "eyJhbGciOiJIUzI1NiJ9.eyJncmFwaHFsLXVyaSI6Imh0dHA6XC9cLzEyNy4wLjAuMTo1MzEyN1wvZ3JhcGhxb";

        RestTransaction.Builder<HyperwalletBankAccount> accountBuilder =
                new RestTransaction.Builder<>(GET, pathFormatter, new TypeReference<HyperwalletBankAccount>() {
                }, mListener);
        final RestTransaction restTransaction = accountBuilder
                .build("http://hyperwallet.com/rest/v3/", token, "usr-fbfd5848-60d0-43c5-8462-099c959b49c7");

        GqlErrors gqlErrors = JsonUtils.fromJsonString(mExternalResourceManager.getResourceContentError(
                "gql_error_response.json"), new TypeReference<GqlErrors>() {
        });
        Handler handler = mock(Handler.class);
        when(mListener.getHandler()).thenReturn(handler);
        HyperwalletGqlException gqlException = new HyperwalletGqlException(gqlErrors);
        restTransaction.onFailure(gqlException);

        verify(handler).post(any(Runnable.class));
    }

    @Test
    public void testRunRequest_whenHttpClientIsInvalid() throws Exception {
        final PathFormatter pathFormatter = new PathFormatter("users/{0}/bank-accounts");
        final String token = "eyJhbGciOiJIUzI1NiJ9.eyJncmFwaHFsLXVyaSI6Imh0dHA6XC9cLzEyNy4wLjAuMTo1MzEyN1wvZ3JhcGhxb";

        RestTransaction.Builder<HyperwalletBankAccount> accountBuilder =
                new RestTransaction.Builder<>(GET, pathFormatter, new TypeReference<HyperwalletBankAccount>() {
                }, mListener);
        final RestTransaction restTransaction = accountBuilder
                .build("http://hyperwallet.com/rest/v3/", token, "usr-fbfd5848-60d0-43c5-8462-099c959b49c7");

        restTransaction.run();
        verify(mListener).onFailure(any(HyperwalletException.class));
    }

    @Test
    public void testRunRequest_checkFailRunnable() throws Exception {
        final PathFormatter pathFormatter = new PathFormatter("users/{0}/bank-accounts");
        final String token = "eyJhbGciOiJIUzI1NiJ9.eyJncmFwaHFsLXVyaSI6Imh0dHA6XC9cLzEyNy4wLjAuMTo1MzEyN1wvZ3JhcGhxb";

        RestTransaction.Builder<HyperwalletBankAccount> accountBuilder =
                new RestTransaction.Builder<>(GET, pathFormatter, new TypeReference<HyperwalletBankAccount>() {
                }, mListener);
        final RestTransaction restTransaction = accountBuilder
                .build("http://hyperwallet.com/rest/v3/", token, "usr-fbfd5848-60d0-43c5-8462-099c959b49c7");

        Handler handler = mock(Handler.class);
        when(mListener.getHandler()).thenReturn(handler);
        GqlErrors gqlErrors = JsonUtils.fromJsonString(mExternalResourceManager.getResourceContentError(
                "gql_error_response.json"), new TypeReference<GqlErrors>() {
        });
        HyperwalletGqlException gqlException = new HyperwalletGqlException(gqlErrors);
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

        RestTransaction.Builder<HyperwalletBankAccount> accountBuilder =
                new RestTransaction.Builder<>(GET, pathFormatter, new TypeReference<HyperwalletBankAccount>() {
                }, mListener);
        final RestTransaction restTransaction = accountBuilder
                .build("http://hyperwallet.com/rest/v3/", token, "usr-fbfd5848-60d0-43c5-8462-099c959b49c7");

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

        RestTransaction.Builder<HyperwalletBankAccount> accountBuilder =
                new RestTransaction.Builder<>(GET, pathFormatter, new TypeReference<HyperwalletBankAccount>() {
                }, mListener);
        final RestTransaction restTransaction = accountBuilder
                .build("http://hyperwallet.com/rest/v3/", token, "usr-fbfd5848-60d0-43c5-8462-099c959b49c7");

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

        RestTransaction.Builder<HyperwalletBankAccount> accountBuilder =
                new RestTransaction.Builder<>(GET, pathFormatter, new TypeReference<HyperwalletBankAccount>() {
                }, mListener);
        final RestTransaction restTransaction = accountBuilder
                .build("http://hyperwallet.com/rest/v3/", token, "usr-fbfd5848-60d0-43c5-8462-099c959b49c7");

        Handler handler = mock(Handler.class);
        when(mListener.getHandler()).thenReturn(handler);
        restTransaction.onSuccess(response);

        verify(handler).post(mRunnableCaptor.capture());

        Runnable r = mRunnableCaptor.getValue();
        r.run();
        verify(mListener).onSuccess((HyperwalletTransferMethodConfigurationKeyResult) any());
    }

    @Test
    public void testRunRequest_checkFailWithNullHandlerRunnable() throws Exception {
        final PathFormatter pathFormatter = new PathFormatter("users/{0}/bank-accounts");
        final String token = "eyJhbGciOiJIUzI1NiJ9.eyJncmFwaHFsLXVyaSI6Imh0dHA6XC9cLzEyNy4wLjAuMTo1MzEyN1wvZ3JhcGhxb";

        RestTransaction.Builder<HyperwalletBankAccount> accountBuilder =
                new RestTransaction.Builder<>(GET, pathFormatter, new TypeReference<HyperwalletBankAccount>() {
                }, mListener);
        final RestTransaction restTransaction = accountBuilder
                .build("http://hyperwallet.com/rest/v3/", token, "usr-fbfd5848-60d0-43c5-8462-099c959b49c7");

        Handler handler = mock(Handler.class);
        when(mListener.getHandler()).thenReturn(handler);
        restTransaction.onSuccess("some string");

        verify(handler).post(mRunnableCaptor.capture());

        Runnable r = mRunnableCaptor.getValue();
        r.run();
        verify(mListener, never()).onSuccess((HyperwalletTransferMethodConfigurationKeyResult) any());
    }

}
