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
import com.hyperwallet.android.model.balance.BalanceQueryParam;
import com.hyperwallet.android.model.paging.PageList;
import com.hyperwallet.android.rule.ExternalResourceManager;
import com.hyperwallet.android.rule.HyperwalletMockWebServer;
import com.hyperwallet.android.rule.HyperwalletSdkMock;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RobolectricTestRunner;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import okhttp3.mockwebserver.RecordedRequest;

@RunWith(RobolectricTestRunner.class)
public class ListBalancesTest {
    @Rule
    public ExternalResourceManager mExternalResourceManager = new ExternalResourceManager();
    @Rule
    public HyperwalletMockWebServer mServer = new HyperwalletMockWebServer();
    @Rule
    public HyperwalletSdkMock mHyperwalletSdkMock = new HyperwalletSdkMock(mServer);
    @Rule
    public MockitoRule mMockito = MockitoJUnit.rule();
    @Mock
    private HyperwalletListener<PageList<Balance>> mListener;
    @Captor
    private ArgumentCaptor<PageList<Balance>> mListBalanceCaptor;
    @Captor
    private ArgumentCaptor<HyperwalletException> mExceptionCaptor;

    private CountDownLatch mAwait = new CountDownLatch(1);

    @Test
    public void testListBalance_returnsBalances() throws InterruptedException {

        String responseBody = mExternalResourceManager.getResourceContent("balance_list_response.json");
        mServer.mockResponse().withHttpResponseCode(HTTP_OK).withBody(responseBody).mock();

        BalanceQueryParam queryParam = new BalanceQueryParam.Builder()
                .build();

        assertThat(queryParam, is(notNullValue()));
        Hyperwallet.getDefault().listUserBalances(queryParam, mListener);

        mAwait.await(500, TimeUnit.MILLISECONDS);

        RecordedRequest recordedRequest = mServer.getRequest();
        verify(mListener).onSuccess(mListBalanceCaptor.capture());
        verify(mListener, never()).onFailure(any(HyperwalletException.class));
        assertThat(recordedRequest.getMethod(), is(GET.name()));

        PageList<Balance> balanceResponse = mListBalanceCaptor.getValue();

        assertThat(balanceResponse, is(notNullValue()));
        assertThat(balanceResponse.getCount(), is(10));
        assertThat(balanceResponse.getDataList(), hasSize(10));
        assertThat(balanceResponse.getOffset(), is(0));
        assertThat(balanceResponse.getLimit(), is(10));

        assertThat(recordedRequest.getPath(),
                is("/rest/v3/users/usr-fbfd5848-60d0-43c5-8462-099c959b49c7/balances?offset=0&limit=10"));

        Balance balance = balanceResponse.getDataList().get(0);
        assertThat(balance.getCurrency(), is("CAD"));
        assertThat(balance.getAmount(), is("988.03"));

    }

    @Test
    public void testListBalance_returnsNoBalances() throws InterruptedException {
        String responseBody = mExternalResourceManager.getResourceContent("balance_no_balance_response.json");
        mServer.mockResponse().withHttpResponseCode(HTTP_NO_CONTENT).withBody(responseBody).mock();

        BalanceQueryParam queryParam = new BalanceQueryParam.Builder()
                .currency("GBP")
                .build();

        assertThat(queryParam, is(notNullValue()));
        Hyperwallet.getDefault().listUserBalances(queryParam, mListener);

        mAwait.await(500, TimeUnit.MILLISECONDS);

        RecordedRequest recordedRequest = mServer.getRequest();
        assertThat(recordedRequest.getPath(),
                containsString(
                        "/rest/v3/users/usr-fbfd5848-60d0-43c5-8462-099c959b49c7/balances?limit=10&currency=GBP"
                                + "&offset=0"));
        assertThat(recordedRequest.getMethod(), is(GET.name()));
        assertThat(recordedRequest.getPath(), containsString("currency=GBP"));
        assertThat(recordedRequest.getPath(), containsString("limit=10"));
        assertThat(recordedRequest.getPath(), containsString("offset=0"));

        verify(mListener).onSuccess(mListBalanceCaptor.capture());
        verify(mListener, never()).onFailure(any(HyperwalletException.class));

        PageList<Balance> balanceResponse = mListBalanceCaptor.getValue();
        assertThat(balanceResponse, is(nullValue()));
    }

    @Test
    public void testListBalance_returnsError() throws InterruptedException {
        String responseBody = mExternalResourceManager.getResourceContentError("system_error_response.json");
        mServer.mockResponse().withHttpResponseCode(HTTP_INTERNAL_ERROR).withBody(responseBody).mock();

        BalanceQueryParam actualBalanceParam = new BalanceQueryParam.Builder()
                .build();

        Hyperwallet.getDefault().listUserBalances(actualBalanceParam, mListener);
        mAwait.await(500, TimeUnit.MILLISECONDS);

        verify(mListener, never()).onSuccess(any(PageList.class));
        verify(mListener).onFailure(mExceptionCaptor.capture());

        HyperwalletException hyperwalletException = mExceptionCaptor.getValue();
        assertThat(hyperwalletException, is(notNullValue()));
        assertThat(((HyperwalletRestException) hyperwalletException).getHttpCode(),
                is(HTTP_INTERNAL_ERROR));

        Errors errors = hyperwalletException.getErrors();
        assertThat(errors, is(notNullValue()));
        assertThat(errors.getErrors(), is(notNullValue()));
        assertThat(errors.getErrors().size(), is(1));

        Error error = errors.getErrors().get(0);
        assertThat(error.getCode(), is("SYSTEM_ERROR"));
        assertThat(error.getMessage(),
                is("A system error has occurred. Please try again. If you continue to receive this error, please "
                        + "contact customer support for assistance (Ref ID: 99b4ad5c-4aac-4cc2-aa9b-4b4f4844ac9b)."));
        assertThat(error.getFieldName(), is(nullValue()));

        RecordedRequest recordedRequest = mServer.getRequest();
        assertThat(recordedRequest.getPath(),
                containsString("/rest/v3/users/usr-fbfd5848-60d0-43c5-8462-099c959b49c7/balances?"));
        assertThat(recordedRequest.getMethod(), is(GET.name()));
        assertThat(recordedRequest.getPath(), containsString("limit=10"));
        assertThat(recordedRequest.getPath(), containsString("offset=0"));
    }

    @Test
    public void testListBalance_returnsEmptyResponse() throws InterruptedException {
        mServer.mockResponse().withHttpResponseCode(HTTP_OK).withBody("").mock();

        BalanceQueryParam queryParam = new BalanceQueryParam.Builder()
                .currency("GBP")
                .build();

        assertThat(queryParam, is(notNullValue()));
        Hyperwallet.getDefault().listUserBalances(queryParam, mListener);

        mAwait.await(500, TimeUnit.MILLISECONDS);

        RecordedRequest recordedRequest = mServer.getRequest();
        assertThat(recordedRequest.getPath(),
                containsString(
                        "/rest/v3/users/usr-fbfd5848-60d0-43c5-8462-099c959b49c7/balances?limit=10&currency=GBP"
                                + "&offset=0"));
        assertThat(recordedRequest.getMethod(), is(GET.name()));
        assertThat(recordedRequest.getPath(), containsString("currency=GBP"));
        assertThat(recordedRequest.getPath(), containsString("limit=10"));
        assertThat(recordedRequest.getPath(), containsString("offset=0"));

        verify(mListener).onSuccess(mListBalanceCaptor.capture());
        verify(mListener, never()).onFailure(any(HyperwalletException.class));

        PageList<Balance> balanceResponse = mListBalanceCaptor.getValue();
        assertThat(balanceResponse, is(nullValue()));
    }
}