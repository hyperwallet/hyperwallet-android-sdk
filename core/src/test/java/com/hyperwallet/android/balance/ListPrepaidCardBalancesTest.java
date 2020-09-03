package com.hyperwallet.android.balance;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import static java.net.HttpURLConnection.HTTP_INTERNAL_ERROR;
import static java.net.HttpURLConnection.HTTP_NO_CONTENT;
import static java.net.HttpURLConnection.HTTP_OK;

import static com.hyperwallet.android.util.HttpMethod.GET;

import com.hyperwallet.android.Hyperwallet;
import com.hyperwallet.android.exception.HyperwalletException;
import com.hyperwallet.android.exception.HyperwalletRestException;
import com.hyperwallet.android.listener.HyperwalletListener;
import com.hyperwallet.android.model.Error;
import com.hyperwallet.android.model.Errors;
import com.hyperwallet.android.model.balance.Balance;
import com.hyperwallet.android.model.balance.PrepaidCardBalanceQueryParam;
import com.hyperwallet.android.model.paging.PageList;
import com.hyperwallet.android.rule.ExternalResourceManager;
import com.hyperwallet.android.rule.HyperwalletMockWebServer;
import com.hyperwallet.android.rule.HyperwalletSdkMock;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RobolectricTestRunner;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import okhttp3.mockwebserver.RecordedRequest;

@RunWith(RobolectricTestRunner.class)
public class ListPrepaidCardBalancesTest {

    private static final String PREPAID_CARD_TOKEN = "trm-fake-token";

    @Rule
    public final ExternalResourceManager externalResourceManager = new ExternalResourceManager();
    @Rule
    public HyperwalletMockWebServer hyperwalletMockWebServer = new HyperwalletMockWebServer();
    @Rule
    public HyperwalletSdkMock hyperwalletSdkMock = new HyperwalletSdkMock(hyperwalletMockWebServer);
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private HyperwalletListener<PageList<Balance>> hyperwalletListener;
    @Captor
    private ArgumentCaptor<PageList<Balance>> balanceArgumentCaptor;
    @Captor
    private ArgumentCaptor<HyperwalletException> exceptionArgumentCaptor;

    private final CountDownLatch countDownLatch = new CountDownLatch(1);

    @Test
    public void listPrepaidCardBalances_returnsBalances() throws InterruptedException {

        String responseBody = externalResourceManager.getResourceContent("prepaid_card_balance_list_response.json");
        hyperwalletMockWebServer.mockResponse().withHttpResponseCode(HTTP_OK).withBody(responseBody).mock();

        PrepaidCardBalanceQueryParam prepaidCardBalanceQueryParam = new PrepaidCardBalanceQueryParam.Builder().build();

        Hyperwallet.getDefault().listPrepaidCardBalances(PREPAID_CARD_TOKEN, prepaidCardBalanceQueryParam, hyperwalletListener);
        countDownLatch.await(100, TimeUnit.MILLISECONDS);

        RecordedRequest recordedRequest = hyperwalletMockWebServer.getRequest();
        assertThat(recordedRequest.getMethod(), is(GET.name()));
        verify(hyperwalletListener).onSuccess(balanceArgumentCaptor.capture());
        verify(hyperwalletListener, never()).onFailure(any(HyperwalletException.class));

        PageList<Balance> balanceResponse = balanceArgumentCaptor.getValue();

        assertThat(balanceResponse, is(notNullValue()));
        assertThat(balanceResponse.getDataList(), hasSize(1));
        assertThat(balanceResponse.getCount(), is(1));
        assertThat(recordedRequest.getPath(),
                containsString(
                        "/rest/v3/users/test-user-token/prepaid-cards/trm-fake-token/balances?"));
        assertThat(recordedRequest.getPath(), containsString("limit"));
        assertThat(recordedRequest.getPath(), containsString("offset"));
        assertThat(balanceResponse.getOffset(), is(0));
        assertThat(balanceResponse.getLimit(), is(10));

        Balance balance = balanceResponse.getDataList().get(0);
        assertThat(balance.getCurrency(), is("USD"));
        assertThat(balance.getAmount(), is("991.05"));
    }

    @Test
    public void listPrepaidCardBalances_returnsNoBalances() throws InterruptedException {
        hyperwalletMockWebServer.mockResponse().withHttpResponseCode(HTTP_NO_CONTENT).withBody("").mock();

        PrepaidCardBalanceQueryParam prepaidCardBalanceQueryParam = new PrepaidCardBalanceQueryParam.Builder().build();

        Hyperwallet.getDefault().listPrepaidCardBalances(PREPAID_CARD_TOKEN, prepaidCardBalanceQueryParam, hyperwalletListener);

        countDownLatch.await(100, TimeUnit.MILLISECONDS);

        RecordedRequest recordedRequest = hyperwalletMockWebServer.getRequest();
        assertThat(recordedRequest.getMethod(), is(GET.name()));
        assertThat(recordedRequest.getPath(),
                containsString(
                        "/rest/v3/users/test-user-token/prepaid-cards/trm-fake-token/balances?"));

        verify(hyperwalletListener).onSuccess(balanceArgumentCaptor.capture());
        verify(hyperwalletListener, never()).onFailure(any(HyperwalletException.class));

        PageList<Balance> balancesResponse = balanceArgumentCaptor.getValue();
        assertThat(balancesResponse, is(nullValue()));
    }

    @Test
    public void listPrepaidCardBalances_returnsError() throws InterruptedException {
        String responseBody = externalResourceManager.getResourceContentError("system_error_response.json");
        hyperwalletMockWebServer.mockResponse().withHttpResponseCode(HTTP_INTERNAL_ERROR).withBody(responseBody).mock();

        PrepaidCardBalanceQueryParam prepaidCardBalanceQueryParam = new PrepaidCardBalanceQueryParam.Builder().build();

        Hyperwallet.getDefault().listPrepaidCardBalances(PREPAID_CARD_TOKEN, prepaidCardBalanceQueryParam, hyperwalletListener);
        countDownLatch.await(500, TimeUnit.MILLISECONDS);

        RecordedRequest recordedRequest = hyperwalletMockWebServer.getRequest();
        assertThat(recordedRequest.getPath(),
                containsString(
                        "/rest/v3/users/test-user-token/prepaid-cards/trm-fake-token/balances?"));

        verify(hyperwalletListener, never()).onSuccess(ArgumentMatchers.<PageList<Balance>>any());
        verify(hyperwalletListener).onFailure(exceptionArgumentCaptor.capture());

        HyperwalletException hyperwalletException = exceptionArgumentCaptor.getValue();
        assertThat(hyperwalletException, is(notNullValue()));
        assertThat(((HyperwalletRestException) hyperwalletException).getHttpCode(),
                is(HTTP_INTERNAL_ERROR));

        Errors errors = hyperwalletException.getErrors();
        assertThat(errors, is(notNullValue()));
        assertThat(errors.getErrors(), is(notNullValue()));
        assertThat(errors.getErrors(), hasSize(1));

        Error error = errors.getErrors().get(0);
        assertThat(error.getCode(), is("SYSTEM_ERROR"));
        assertThat(error.getMessage(),
                is("A system error has occurred. Please try again. If you continue to receive this error, please "
                        + "contact customer support for assistance (Ref ID: 99b4ad5c-4aac-4cc2-aa9b-4b4f4844ac9b)."));
        assertThat(error.getFieldName(), is(nullValue()));
    }
}
