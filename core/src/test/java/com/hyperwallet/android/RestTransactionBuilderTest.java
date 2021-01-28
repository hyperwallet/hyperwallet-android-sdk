package com.hyperwallet.android;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodTypes.BANK_ACCOUNT;
import static com.hyperwallet.android.util.HttpMethod.GET;
import static com.hyperwallet.android.util.HttpMethod.POST;

import android.os.Build;

import com.hyperwallet.android.listener.HyperwalletListener;
import com.hyperwallet.android.model.TypeReference;
import com.hyperwallet.android.model.paging.PageList;
import com.hyperwallet.android.model.transfermethod.BankAccount;
import com.hyperwallet.android.sdk.BuildConfig;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RobolectricTestRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RunWith(RobolectricTestRunner.class)
public class RestTransactionBuilderTest {

    @Rule
    public final MockitoRule mMockito = MockitoJUnit.rule();

    @Mock
    private HyperwalletListener<BankAccount> mListener;

    private final String contextId = UUID.randomUUID().toString();

    @Test
    public void testBuild_withRequiredParametersOnly() throws JSONException {

        final PathFormatter pathFormatter = new PathFormatter("users/{0}/bank-accounts");
        final String token = "eyJhbGciOiJIUzI1NiJ9.eyJncmFwaHFsLXVyaSI6Imh0dHA6XC9cLzEyNy4wLjAuMTo1MzEyN1wvZ3JhcGhxb";

        RestTransaction.Builder<BankAccount> builder = new RestTransaction.Builder<>(
                POST, pathFormatter,
                new TypeReference<BankAccount>() {
                }, mListener, contextId);
        final RestTransaction restTransaction = builder.build("http://hyperwallet.com/rest/v3/", token,
                "test-user-token");

        assertThat(restTransaction, is(notNullValue()));
        assertThat(restTransaction.getMethod(), is(POST));
        assertThat(restTransaction.getPayload(), is(nullValue()));
        assertThat(restTransaction.getQueries().size(), is(0));

        Map<String, String> headers = restTransaction.getHeaders();
        assertThat(headers, is(notNullValue()));
        assertThat(headers.get("Accept"), is("application/json"));
        assertThat(headers.get("Content-Type"), is("application/json"));
        assertThat(headers.get("User-Agent"), is("HyperwalletSDK/Android/" + BuildConfig.VERSION_NAME +
                "; App: HyperwalletSDK; Android: " + Build.VERSION.RELEASE));
        assertThat(headers.get("X-Sdk-Version"), is("1.0.0-beta08"));
        assertThat(headers.get("X-Sdk-Type"), is("android"));
        assertThat(headers.get("X-Sdk-ContextId"), is(notNullValue()));
        assertThat(headers.get("X-Sdk-ContextId"), is(contextId));
    }

    @Test
    public void testBuild_withJsonModelOptionalParameter() throws JSONException {
        final PathFormatter pathFormatter = new PathFormatter("users/{0}/bank-accounts");
        final String token = "eyJhbGciOiJIUzI1NiJ9.eyJncmFwaHFsLXVyaSI6Imh0dHA6XC9cLzEyNy4wLjAuMTo1MzEyN1wvZ3JhcGhxb";
        BankAccount.Builder builder = new BankAccount
                .Builder("US", "USD", "8017110254");
        final BankAccount bankAccount = builder.build();

        RestTransaction.Builder<BankAccount> accountBuilder = new RestTransaction.Builder<>(
                POST, pathFormatter,
                new TypeReference<BankAccount>() {
                }, mListener, contextId);
        final RestTransaction restTransaction = accountBuilder
                .jsonModel(bankAccount)
                .build("http://hyperwallet.com/rest/v3/", token, "test-user-token");

        assertThat(restTransaction, is(notNullValue()));
        assertThat(restTransaction.getMethod(), is(POST));
        assertThat(restTransaction.getQueries().size(), is(0));
        JSONObject resultPayload = new JSONObject(restTransaction.getPayload());
        assertThat(resultPayload.getString("type"), is(BANK_ACCOUNT));
        assertThat(resultPayload.getString("transferMethodCurrency"), is("USD"));
        assertThat(resultPayload.getString("transferMethodCountry"), is("US"));
        assertThat(resultPayload.getString("bankAccountId"), is("8017110254"));

        Map<String, String> headers = restTransaction.getHeaders();
        assertThat(headers, is(notNullValue()));
        assertThat(headers.get("Accept"), is("application/json"));
        assertThat(headers.get("Content-Type"), is("application/json"));
        assertThat(headers.get("User-Agent"), is("HyperwalletSDK/Android/" + BuildConfig.VERSION_NAME +
                "; App: HyperwalletSDK; Android: " + Build.VERSION.RELEASE));
        assertThat(headers.get("X-Sdk-Version"), is("1.0.0-beta08"));
        assertThat(headers.get("X-Sdk-Type"), is("android"));
        assertThat(headers.get("X-Sdk-ContextId"), is(notNullValue()));
        assertThat(headers.get("X-Sdk-ContextId"), is(contextId));
    }

    @Test
    public void testBuild_withQueryModelOptionalParameter() throws JSONException {
        final PathFormatter pathFormatter = new PathFormatter("users/{0}/bank-accounts");
        final Map<String, String> query = new HashMap<>();
        query.put("offset", "0");
        query.put("limit", "10");
        query.put("type", "BANK_ACCOUNT");

        final String token = "eyJhbGciOiJIUzI1NiJ9.eyJncmFwaHFsLXVyaSI6Imh0dHA6XC9cLzEyNy4wLjAuMTo1MzEyN1wvZ3JhcGhxb";

        RestTransaction.Builder<PageList<BankAccount>> pageListBuilder =
                new RestTransaction.Builder<>(GET, pathFormatter,
                        new TypeReference<PageList<BankAccount>>() {
                        }, mListener, contextId);
        final RestTransaction restTransaction = pageListBuilder
                .query(query)
                .build("http://hyperwallet.com/rest/v3/", token, "test-user-token");

        assertThat(restTransaction, is(notNullValue()));
        assertThat(restTransaction.getMethod(), is(GET));
        assertThat(restTransaction.getQueries(), is(equalTo(query)));

        assertThat(restTransaction.getPayload(), is(nullValue()));

        Map<String, String> headers = restTransaction.getHeaders();
        assertThat(headers, is(notNullValue()));
        assertThat(headers.get("Accept"), is("application/json"));
        assertThat(headers.get("Content-Type"), is("application/json"));
        assertThat(headers.get("User-Agent"), is("HyperwalletSDK/Android/" + BuildConfig.VERSION_NAME +
                "; App: HyperwalletSDK; Android: " + Build.VERSION.RELEASE));
        assertThat(headers.get("X-Sdk-Version"), is("1.0.0-beta08"));
        assertThat(headers.get("X-Sdk-Type"), is("android"));
        assertThat(headers.get("X-Sdk-ContextId"), is(notNullValue()));
        assertThat(headers.get("X-Sdk-ContextId"), is(contextId));
    }
}


