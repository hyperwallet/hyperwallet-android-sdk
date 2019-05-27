package com.hyperwallet.android;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import static com.hyperwallet.android.util.HttpMethod.GET;
import static com.hyperwallet.android.util.HttpMethod.POST;

import com.hyperwallet.android.listener.HyperwalletListener;
import com.hyperwallet.android.model.TypeReference;
import com.hyperwallet.android.model.graphql.HyperwalletTransferMethodConfigurationKeyResult;
import com.hyperwallet.android.model.paging.HyperwalletPageList;
import com.hyperwallet.android.model.transfermethod.HyperwalletBankAccount;

import org.json.JSONException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RobolectricTestRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(RobolectricTestRunner.class)
public class RestTransactionBuilderTest {

    @Rule
    public final MockitoRule mMockito = MockitoJUnit.rule();

    @Mock
    private HyperwalletListener<HyperwalletTransferMethodConfigurationKeyResult> mListener;

    @Test
    public void testBuild_withRequiredParametersOnly() throws JSONException {

        final PathFormatter pathFormatter = new PathFormatter("users/{0}/bank-accounts");
        final String token = "eyJhbGciOiJIUzI1NiJ9.eyJncmFwaHFsLXVyaSI6Imh0dHA6XC9cLzEyNy4wLjAuMTo1MzEyN1wvZ3JhcGhxb";

        RestTransaction.Builder<HyperwalletBankAccount> builder = new RestTransaction.Builder<>(
                POST, pathFormatter,
                new TypeReference<HyperwalletBankAccount>() {
                }, mListener);
        final RestTransaction restTransaction = builder.build("http://hyperwallet.com/rest/v3/", token,
                "usr-fbfd5848-60d0-43c5-8462-099c959b49c7");

        assertThat(restTransaction, is(notNullValue()));
        assertThat(restTransaction.getMethod(), is(POST));
        assertThat(restTransaction.getPayload(), is(nullValue()));
        assertThat(restTransaction.getQueries().size(), is(0));

        Map<String, String> headers = restTransaction.getHeaders();
        assertThat(headers, is(notNullValue()));
        assertThat(headers.get("Accept"), is("application/json"));
        assertThat(headers.get("Content-Type"), is("application/json"));
    }

    @Test
    public void testBuild_withJsonModelOptionalParameter() throws JSONException {
        final PathFormatter pathFormatter = new PathFormatter("users/{0}/bank-accounts");
        final String token = "eyJhbGciOiJIUzI1NiJ9.eyJncmFwaHFsLXVyaSI6Imh0dHA6XC9cLzEyNy4wLjAuMTo1MzEyN1wvZ3JhcGhxb";
        HyperwalletBankAccount.Builder builder = new HyperwalletBankAccount
                .Builder("US", "USD", "8017110254");
        final HyperwalletBankAccount bankAccount = builder.build();

        RestTransaction.Builder<HyperwalletBankAccount> accountBuilder = new RestTransaction.Builder<>(
                POST, pathFormatter,
                new TypeReference<HyperwalletBankAccount>() {
                }, mListener);
        final RestTransaction restTransaction = accountBuilder
                .jsonModel(bankAccount)
                .build("http://hyperwallet.com/rest/v3/", token, "usr-fbfd5848-60d0-43c5-8462-099c959b49c7");

        assertThat(restTransaction, is(notNullValue()));
        assertThat(restTransaction.getMethod(), is(POST));
        assertThat(restTransaction.getQueries().size(), is(0));
        assertThat(restTransaction.getPayload(), is("{\"transferMethodCurrency\":\"USD\","
                + "\"transferMethodCountry\":\"US\",\"bankAccountId\":\"8017110254\",\"type\":\"BANK_ACCOUNT\"}"));

        Map<String, String> headers = restTransaction.getHeaders();
        assertThat(headers, is(notNullValue()));
        assertThat(headers.get("Accept"), is("application/json"));
        assertThat(headers.get("Content-Type"), is("application/json"));
    }

    @Test
    public void testBuild_withQueryModelOptionalParameter() throws JSONException {
        final PathFormatter pathFormatter = new PathFormatter("users/{0}/bank-accounts");
        final Map<String, String> query = new HashMap<>();
        query.put("offset", "0");
        query.put("limit", "10");
        query.put("type", "BANK_ACCOUNT");

        final String token = "eyJhbGciOiJIUzI1NiJ9.eyJncmFwaHFsLXVyaSI6Imh0dHA6XC9cLzEyNy4wLjAuMTo1MzEyN1wvZ3JhcGhxb";

        RestTransaction.Builder<HyperwalletPageList<HyperwalletBankAccount>> pageListBuilder =
                new RestTransaction.Builder<>(GET, pathFormatter,
                        new TypeReference<HyperwalletPageList<HyperwalletBankAccount>>() {
                        }, mListener);
        final RestTransaction restTransaction = pageListBuilder
                .query(query)
                .build("http://hyperwallet.com/rest/v3/", token, "usr-fbfd5848-60d0-43c5-8462-099c959b49c7");

        assertThat(restTransaction, is(notNullValue()));
        assertThat(restTransaction.getMethod(), is(GET));
        assertThat(restTransaction.getQueries(), is(equalTo(query)));

        assertThat(restTransaction.getPayload(), is(nullValue()));

        Map<String, String> headers = restTransaction.getHeaders();
        assertThat(headers, is(notNullValue()));
        assertThat(headers.get("Accept"), is("application/json"));
        assertThat(headers.get("Content-Type"), is("application/json"));
    }
}


