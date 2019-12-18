package com.hyperwallet.android.transfermethod;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

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

import com.hyperwallet.android.Hyperwallet;
import com.hyperwallet.android.exception.HyperwalletException;
import com.hyperwallet.android.listener.HyperwalletListener;
import com.hyperwallet.android.model.Error;
import com.hyperwallet.android.model.Errors;
import com.hyperwallet.android.model.transfermethod.PayPalAccount;
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

import java.net.HttpURLConnection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import okhttp3.mockwebserver.RecordedRequest;

@RunWith(RobolectricTestRunner.class)
public class GetPayPalTest {
    private static final long COUNTDOWN_TIMEOUT_MILLIS = 100L;
    private final CountDownLatch mAwait = new CountDownLatch(1);
    @Rule
    public HyperwalletMockWebServer mServer = new HyperwalletMockWebServer();
    @Rule
    public HyperwalletSdkMock mHyperwalletSdkMock = new HyperwalletSdkMock(mServer);
    @Rule
    public ExternalResourceManager mExternalResourceManager = new ExternalResourceManager();
    @Rule
    public MockitoRule mMockito = MockitoJUnit.rule();
    @Mock
    private HyperwalletListener<PayPalAccount> mListener;
    @Captor
    private ArgumentCaptor<PayPalAccount> mPayPalAccountArgumentCaptor;
    @Captor
    private ArgumentCaptor<HyperwalletException> mExceptionCaptor;

    @Test
    public void testGetGetPayPalAccount_returnsAccount() throws InterruptedException {

        String responseBody = mExternalResourceManager.getResourceContent("paypal_account_response.json");
        mServer.mockResponse().withHttpResponseCode(HttpURLConnection.HTTP_OK).withBody(responseBody).mock();

        Hyperwallet.getDefault().getPayPalAccount("trm-ac5727ac-8fe7-42fb-b69d-977ebdd7b48b", mListener);
        mAwait.await(COUNTDOWN_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);

        RecordedRequest recordedRequest = mServer.getRequest();
        assertThat(recordedRequest.getMethod(), is(GET.name()));
        assertThat(recordedRequest.getPath(),
                is("/rest/v3/users/usr-fbfd5848-60d0-43c5-8462-099c959b49c7/paypal-accounts/trm-ac5727ac-8fe7-42fb"
                        + "-b69d-977ebdd7b48b"));

        verify(mListener).onSuccess(mPayPalAccountArgumentCaptor.capture());
        verify(mListener, never()).onFailure(any(HyperwalletException.class));

        PayPalAccount payPalAccountResponse = mPayPalAccountArgumentCaptor.getValue();
        assertThat(payPalAccountResponse, is(notNullValue()));
        assertThat(payPalAccountResponse.getField(TYPE), is(PAYPAL_ACCOUNT));
        assertThat(payPalAccountResponse.getField(TOKEN), is("trm-ac5727ac-8fe7-42fb-b69d-977ebdd7b48b"));
        assertThat(payPalAccountResponse.getField(STATUS), is(ACTIVATED));

        assertThat(payPalAccountResponse.getField(CREATED_ON), is("2019-01-09T22:50:14"));
        assertThat(payPalAccountResponse.getField(TRANSFER_METHOD_COUNTRY), is("US"));
        assertThat(payPalAccountResponse.getField(TRANSFER_METHOD_CURRENCY), is("USD"));
        assertThat(payPalAccountResponse.getField(EMAIL), is("jsmith@paypal.com"));

    }

    @Test
    public void testGetGetPayPalAccount_returnsNoAccount() throws InterruptedException {

        String responseBody = "";
        mServer.mockResponse().withHttpResponseCode(HttpURLConnection.HTTP_NO_CONTENT).withBody(responseBody).mock();

        Hyperwallet.getDefault().getPayPalAccount("trm-ac5727ac-8fe7-42fb-b69d-977ebdd7b48b", mListener);
        mAwait.await(COUNTDOWN_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);

        RecordedRequest recordedRequest = mServer.getRequest();
        assertThat(recordedRequest.getMethod(), is(GET.name()));
        assertThat(recordedRequest.getPath(),
                is("/rest/v3/users/usr-fbfd5848-60d0-43c5-8462-099c959b49c7/paypal-accounts/trm-ac5727ac-8fe7-42fb"
                        + "-b69d-977ebdd7b48b"));

        verify(mListener).onSuccess(mPayPalAccountArgumentCaptor.capture());
        verify(mListener, never()).onFailure(any(HyperwalletException.class));

        PayPalAccount payPalAccount = mPayPalAccountArgumentCaptor.getValue();
        assertThat(payPalAccount, is(nullValue()));
    }

    @Test
    public void testGetGetPayPalAccount_returnsError() throws InterruptedException {

        String responseBody = mExternalResourceManager.getResourceContentError("system_error_response.json");
        mServer.mockResponse().withHttpResponseCode(HttpURLConnection.HTTP_BAD_REQUEST).withBody(responseBody).mock();

        Hyperwallet.getDefault().getPayPalAccount("trm-854c4ec1-9161-49d6-92e2-b8d15aa4bf56", mListener);
        mAwait.await(COUNTDOWN_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);

        RecordedRequest recordedRequest = mServer.getRequest();
        assertThat(recordedRequest.getMethod(), is(GET.name()));
        assertThat(recordedRequest.getPath(),
                is("/rest/v3/users/usr-fbfd5848-60d0-43c5-8462-099c959b49c7/paypal-accounts/trm-854c4ec1-9161-49d6-92e2"
                        + "-b8d15aa4bf56"));

        verify(mListener, never()).onSuccess(any(PayPalAccount.class));
        verify(mListener).onFailure(mExceptionCaptor.capture());

        HyperwalletException exception = mExceptionCaptor.getValue();
        assertThat(exception, is(notNullValue()));
        Errors errors = exception.getErrors();
        assertThat(errors, is(notNullValue()));
        assertThat(errors.getErrors(), hasSize(1));

        Error error = errors.getErrors().get(0);
        assertThat(error.getCode(), is("SYSTEM_ERROR"));
        assertThat(error.getMessage(),
                is("A system error has occurred. Please try again. If you continue to receive this error, please "
                        + "contact customer support for assistance (Ref ID: 99b4ad5c-4aac-4cc2-aa9b-4b4f4844ac9b)."));

    }
}
