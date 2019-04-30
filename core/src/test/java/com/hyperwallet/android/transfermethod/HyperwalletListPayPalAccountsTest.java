package com.hyperwallet.android.transfermethod;

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

import com.hyperwallet.android.Hyperwallet;
import com.hyperwallet.android.exception.HyperwalletException;
import com.hyperwallet.android.exception.HyperwalletRestException;
import com.hyperwallet.android.listener.HyperwalletListener;
import com.hyperwallet.android.model.HyperwalletError;
import com.hyperwallet.android.model.HyperwalletErrors;
import com.hyperwallet.android.model.HyperwalletPayPalAccountPagination;
import com.hyperwallet.android.model.PayPalAccount;
import com.hyperwallet.android.model.paging.HyperwalletPageList;
import com.hyperwallet.android.rule.HyperwalletExternalResourceManager;
import com.hyperwallet.android.rule.HyperwalletMockWebServer;
import com.hyperwallet.android.rule.HyperwalletSdkMock;

import org.hamcrest.Matchers;
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
public class HyperwalletListPayPalAccountsTest {
    @Rule
    public HyperwalletExternalResourceManager mExternalResourceManager = new HyperwalletExternalResourceManager();
    @Rule
    public HyperwalletMockWebServer mServer = new HyperwalletMockWebServer();
    @Rule
    public HyperwalletSdkMock mSdkMock = new HyperwalletSdkMock(mServer);
    @Rule
    public MockitoRule mMockito = MockitoJUnit.rule();
    @Mock
    private HyperwalletListener<HyperwalletPageList<PayPalAccount>> mListener;
    @Captor
    private ArgumentCaptor<HyperwalletPageList<PayPalAccount>> mListTransferMethodCaptor;
    @Captor
    private ArgumentCaptor<HyperwalletException> mExceptionCaptor;

    private CountDownLatch mAwait = new CountDownLatch(1);


    @Test
    public void testListPayPalAccounts_returnsActivatedCards() throws InterruptedException {

        String responseBody = mExternalResourceManager.getResourceContent("pay_pal_accounts_response.json");
        mServer.mockResponse().withHttpResponseCode(HTTP_OK).withBody(responseBody).mock();

        HyperwalletPayPalAccountPagination bankCardPagination = new HyperwalletPayPalAccountPagination();

        assertThat(bankCardPagination, is(notNullValue()));
        Hyperwallet.getDefault().listPayPalAccounts(bankCardPagination, mListener);

        mAwait.await(500, TimeUnit.MILLISECONDS);

        RecordedRequest recordedRequest = mServer.getRequest();
        verify(mListener).onSuccess(mListTransferMethodCaptor.capture());
        verify(mListener, never()).onFailure(any(HyperwalletException.class));

        HyperwalletPageList<PayPalAccount> payPalAccountsResponse = mListTransferMethodCaptor.getValue();

        assertThat(payPalAccountsResponse.getCount(), is(2));
        assertThat(payPalAccountsResponse.getDataList(), hasSize(2));
        assertThat(payPalAccountsResponse.getOffset(), is(0));
        assertThat(payPalAccountsResponse.getLimit(), is(10));

        assertThat(recordedRequest.getPath(),
                is("/rest/v3/users/usr-fbfd5848-60d0-43c5-8462-099c959b49c7/paypal-accounts?limit=10&offset=0&type"
                        + "=PAYPAL_ACCOUNT&status=ACTIVATED"));
    }

    @Test
    public void testListPayPalAccounts_returnsNoCards() throws InterruptedException {
        String responseBody = mExternalResourceManager.getResourceContent("pay_pal_no_accounts_response.json");
        mServer.mockResponse().withHttpResponseCode(HTTP_NO_CONTENT).withBody(responseBody).mock();

        HyperwalletPayPalAccountPagination payPalAccountPagination = new HyperwalletPayPalAccountPagination();

        assertThat(payPalAccountPagination, is(notNullValue()));
        Hyperwallet.getDefault().listPayPalAccounts(payPalAccountPagination, mListener);

        mAwait.await(500, TimeUnit.MILLISECONDS);

        RecordedRequest recordedRequest = mServer.getRequest();
        assertThat(recordedRequest.getPath(),
                containsString("/rest/v3/users/usr-fbfd5848-60d0-43c5-8462-099c959b49c7/paypal-accounts?"));
        assertThat(recordedRequest.getPath(), containsString("type=PAYPAL_ACCOUNT"));
        assertThat(recordedRequest.getPath(), containsString("limit=10"));
        assertThat(recordedRequest.getPath(), containsString("offset=0"));
        assertThat(recordedRequest.getPath(), containsString("status=ACTIVATED"));

        verify(mListener).onSuccess(mListTransferMethodCaptor.capture());
        verify(mListener, never()).onFailure(any(HyperwalletException.class));

        HyperwalletPageList<PayPalAccount> hyperwalletBankCardsResponse = mListTransferMethodCaptor.getValue();
        assertThat(hyperwalletBankCardsResponse, is(nullValue()));
    }

    @Test
    public void testListPayPalAccounts_returnsError() throws InterruptedException {
        String responseBody = mExternalResourceManager.getResourceContentError("pay_pal_accounts_error_response.json");
        mServer.mockResponse().withHttpResponseCode(HTTP_INTERNAL_ERROR).withBody(responseBody).mock();

        HyperwalletPayPalAccountPagination payPalAccountPagination = new HyperwalletPayPalAccountPagination();

        Hyperwallet.getDefault().listPayPalAccounts(payPalAccountPagination, mListener);
        mAwait.await(500, TimeUnit.MILLISECONDS);

        verify(mListener, never()).onSuccess(ArgumentMatchers.<HyperwalletPageList<PayPalAccount>>any());
        verify(mListener).onFailure(mExceptionCaptor.capture());

        HyperwalletException hyperwalletException = mExceptionCaptor.getValue();
        assertThat(hyperwalletException, is(notNullValue()));
        assertThat(((HyperwalletRestException) hyperwalletException).getHttpCode(),
                is(HTTP_INTERNAL_ERROR));

        HyperwalletErrors hyperwalletErrors = hyperwalletException.getHyperwalletErrors();
        assertThat(hyperwalletErrors, is(notNullValue()));
        assertThat(hyperwalletErrors.getErrors(), is(notNullValue()));
        assertThat(hyperwalletErrors.getErrors(), Matchers.<HyperwalletError>hasSize(1));

        HyperwalletError hyperwalletError = hyperwalletErrors.getErrors().get(0);
        assertThat(hyperwalletError.getCode(), is("SYSTEM_ERROR"));
        assertThat(hyperwalletError.getMessage(),
                is("A system error has occurred. Please try again. If you continue to receive this error, please "
                        + "contact customer support for assistance (Ref ID: 99b4ad5c-4aac-4cc2-aa9b-4b4f4844ac9b)."));
        assertThat(hyperwalletError.getFieldName(), is(nullValue()));
        RecordedRequest recordedRequest = mServer.getRequest();
        assertThat(recordedRequest.getPath(),
                containsString("/rest/v3/users/usr-fbfd5848-60d0-43c5-8462-099c959b49c7/paypal-accounts?"));
        assertThat(recordedRequest.getPath(), containsString("type=PAYPAL_ACCOUNT"));
        assertThat(recordedRequest.getPath(), containsString("limit=10"));
        assertThat(recordedRequest.getPath(), containsString("offset=0"));
        assertThat(recordedRequest.getPath(), containsString("status=ACTIVATED"));

    }
}
