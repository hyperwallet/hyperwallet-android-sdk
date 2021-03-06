package com.hyperwallet.android.transfermethod;

import android.content.res.Resources;

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

import static com.hyperwallet.android.model.StatusTransition.StatusDefinition.ACTIVATED;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.CREATED_ON;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.EMAIL;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.STATUS;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.TOKEN;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.TRANSFER_METHOD_COUNTRY;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.TRANSFER_METHOD_CURRENCY;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.TYPE;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodTypes.PAYPAL_ACCOUNT;
import static com.hyperwallet.android.util.HttpMethod.GET;
import static org.mockito.Mockito.when;

import com.hyperwallet.android.Hyperwallet;
import com.hyperwallet.android.exception.HyperwalletException;
import com.hyperwallet.android.listener.HyperwalletListener;
import com.hyperwallet.android.model.Error;
import com.hyperwallet.android.model.Errors;
import com.hyperwallet.android.model.paging.PageList;
import com.hyperwallet.android.model.transfermethod.PayPalAccount;
import com.hyperwallet.android.model.transfermethod.PayPalAccountQueryParam;
import com.hyperwallet.android.rule.ExternalResourceManager;
import com.hyperwallet.android.rule.HyperwalletMockWebServer;
import com.hyperwallet.android.rule.HyperwalletSdkMock;
import com.hyperwallet.android.sdk.R;

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
public class ListPayPalAccountsTest {
    @Rule
    public ExternalResourceManager mExternalResourceManager = new ExternalResourceManager();
    @Rule
    public HyperwalletMockWebServer mServer = new HyperwalletMockWebServer();
    @Rule
    public HyperwalletSdkMock mHyperwalletSdkMock = new HyperwalletSdkMock(mServer);
    @Rule
    public MockitoRule mMockito = MockitoJUnit.rule();
    @Mock
    private HyperwalletListener<PageList<PayPalAccount>> mListener;
    @Captor
    private ArgumentCaptor<PageList<PayPalAccount>> mListPayPalCaptor;
    @Captor
    private ArgumentCaptor<HyperwalletException> mExceptionCaptor;
    @Mock
    private Resources mResources;

    private CountDownLatch mAwait = new CountDownLatch(1);


    @Test
    public void testListPayPalAccounts_returnsActivatedAccounts() throws InterruptedException {

        String responseBody = mExternalResourceManager.getResourceContent("paypal_accounts_response.json");
        mServer.mockResponse().withHttpResponseCode(HTTP_OK).withBody(responseBody).mock();

        PayPalAccountQueryParam queryParam = new PayPalAccountQueryParam.Builder()
                .status(ACTIVATED)
                .build();

        assertThat(queryParam, is(notNullValue()));
        Hyperwallet.getDefault().listPayPalAccounts(queryParam, mListener);

        mAwait.await(500, TimeUnit.MILLISECONDS);

        RecordedRequest recordedRequest = mServer.getRequest();
        assertThat(recordedRequest.getMethod(), is(GET.name()));
        verify(mListener).onSuccess(mListPayPalCaptor.capture());
        verify(mListener, never()).onFailure(any(HyperwalletException.class));

        PageList<PayPalAccount> payPalAccountsResponse = mListPayPalCaptor.getValue();

        assertThat(payPalAccountsResponse.getCount(), is(2));
        assertThat(payPalAccountsResponse.getDataList(), hasSize(2));
        assertThat(payPalAccountsResponse.getOffset(), is(0));
        assertThat(payPalAccountsResponse.getLimit(), is(10));

        assertThat(recordedRequest.getPath(),
                is("/rest/v3/users/test-user-token/paypal-accounts?limit=10&offset=0&type"
                        + "=PAYPAL_ACCOUNT&status=ACTIVATED"));

        PayPalAccount payPalAccount = payPalAccountsResponse.getDataList().get(0);
        assertThat(payPalAccount.getField(TOKEN), is("trm-fake-token"));
        assertThat(payPalAccount.getField(TYPE), is(PAYPAL_ACCOUNT));
        assertThat(payPalAccount.getField(STATUS), is(ACTIVATED));
        assertThat(payPalAccount.getField(CREATED_ON), is("2019-01-09T22:50:14"));
        assertThat(payPalAccount.getField(TRANSFER_METHOD_COUNTRY), is("US"));
        assertThat(payPalAccount.getField(TRANSFER_METHOD_CURRENCY), is("USD"));
        assertThat(payPalAccount.getField(EMAIL), is("jsmith@paypal.com"));
    }

    @Test
    public void testListPayPalAccounts_returnsNoAccounts() throws InterruptedException {
        mServer.mockResponse().withHttpResponseCode(HTTP_NO_CONTENT).withBody("").mock();

        PayPalAccountQueryParam queryParam = new PayPalAccountQueryParam.Builder()
                .status(ACTIVATED)
                .build();

        assertThat(queryParam, is(notNullValue()));
        Hyperwallet.getDefault().listPayPalAccounts(queryParam, mListener);

        mAwait.await(500, TimeUnit.MILLISECONDS);

        RecordedRequest recordedRequest = mServer.getRequest();
        assertThat(recordedRequest.getMethod(), is(GET.name()));
        assertThat(recordedRequest.getPath(),
                containsString("/rest/v3/users/test-user-token/paypal-accounts?"));
        assertThat(recordedRequest.getPath(), containsString("type=PAYPAL_ACCOUNT"));
        assertThat(recordedRequest.getPath(), containsString("limit=10"));
        assertThat(recordedRequest.getPath(), containsString("offset=0"));
        assertThat(recordedRequest.getPath(), containsString("status=ACTIVATED"));

        verify(mListener).onSuccess(mListPayPalCaptor.capture());
        verify(mListener, never()).onFailure(any(HyperwalletException.class));

        PageList<PayPalAccount> payPalAccountsResponse = mListPayPalCaptor.getValue();
        assertThat(payPalAccountsResponse, is(nullValue()));
    }

    @Test
    public void testListPayPalAccounts_returnsError() throws InterruptedException {
        when(mResources.getString(R.string.unexpected_exception)).thenReturn(
                "An unexpected error has occurred, please try again");
        String responseBody = mExternalResourceManager.getResourceContentError("system_error_response.json");
        mServer.mockResponse().withHttpResponseCode(HTTP_INTERNAL_ERROR).withBody(responseBody).mock();

        PayPalAccountQueryParam queryParam = new PayPalAccountQueryParam.Builder()
                .status(ACTIVATED)
                .build();

        Hyperwallet.getDefault().listPayPalAccounts(queryParam, mListener);
        mAwait.await(500, TimeUnit.MILLISECONDS);

        verify(mListener, never()).onSuccess(ArgumentMatchers.<PageList<PayPalAccount>>any());
        verify(mListener).onFailure(mExceptionCaptor.capture());

        HyperwalletException hyperwalletException = mExceptionCaptor.getValue();
        assertThat(hyperwalletException, is(notNullValue()));

        Errors errors = hyperwalletException.getErrors();
        assertThat(errors, is(notNullValue()));
        assertThat(errors.getErrors(), is(notNullValue()));
        assertThat(errors.getErrors(), Matchers.<Error>hasSize(1));

        Error error = errors.getErrors().get(0);
        assertThat(error.getCode(), is("EC_UNEXPECTED_EXCEPTION"));
        assertThat(error.getMessageFromResourceWhenAvailable(mResources),
                is("An unexpected error has occurred, please try again"));
        assertThat(error.getFieldName(), is(nullValue()));
        RecordedRequest recordedRequest = mServer.getRequest();
        assertThat(recordedRequest.getMethod(), is(GET.name()));
        assertThat(recordedRequest.getPath(),
                containsString("/rest/v3/users/test-user-token/paypal-accounts?"));
        assertThat(recordedRequest.getPath(), containsString("type=PAYPAL_ACCOUNT"));
        assertThat(recordedRequest.getPath(), containsString("limit=10"));
        assertThat(recordedRequest.getPath(), containsString("offset=0"));
        assertThat(recordedRequest.getPath(), containsString("status=ACTIVATED"));
    }
}
