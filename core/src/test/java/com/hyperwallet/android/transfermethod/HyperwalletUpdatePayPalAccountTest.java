package com.hyperwallet.android.transfermethod;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodFields.CREATED_ON;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodFields.EMAIL;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodFields.STATUS;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodFields.TOKEN;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodFields.TRANSFER_METHOD_COUNTRY;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodFields.TRANSFER_METHOD_CURRENCY;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodFields.TYPE;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodTypes.PAYPAL_ACCOUNT;
import static com.hyperwallet.android.util.HttpMethod.PUT;

import com.hyperwallet.android.Hyperwallet;
import com.hyperwallet.android.exception.HyperwalletException;
import com.hyperwallet.android.exception.HyperwalletRestException;
import com.hyperwallet.android.listener.HyperwalletListener;
import com.hyperwallet.android.model.HyperwalletError;
import com.hyperwallet.android.model.HyperwalletErrors;
import com.hyperwallet.android.model.transfermethod.PayPalAccount;
import com.hyperwallet.android.rule.HyperwalletExternalResourceManager;
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
public class HyperwalletUpdatePayPalAccountTest {
    @Rule
    public HyperwalletMockWebServer mServer = new HyperwalletMockWebServer();
    @Rule
    public HyperwalletSdkMock mSdkMock = new HyperwalletSdkMock(mServer);
    @Rule
    public HyperwalletExternalResourceManager mExternalResourceManager = new HyperwalletExternalResourceManager();
    @Rule
    public MockitoRule mMockito = MockitoJUnit.rule();

    @Mock
    private HyperwalletListener<PayPalAccount> mListener;

    @Captor
    private ArgumentCaptor<PayPalAccount> mPayPalAccountCaptor;
    @Captor
    private ArgumentCaptor<HyperwalletException> mExceptionCaptor;

    private final CountDownLatch mAwait = new CountDownLatch(1);

    @Test
    public void testUpdatePayPalAccount_withSuccess() throws InterruptedException {
        String responseBody = mExternalResourceManager.getResourceContent("paypal_account_update_response.json");
        mServer.mockResponse().withHttpResponseCode(HttpURLConnection.HTTP_OK).withBody(responseBody).mock();

        final PayPalAccount payPalAccount = new PayPalAccount
                .Builder()
                .email("jsmith2@paypal.com")
                .token("trm-ac5727ac-8fe7-42fb-b69d-977ebdd7b48b")
                .build();

        assertThat(payPalAccount.getField(EMAIL), is("jsmith2@paypal.com"));
        assertThat(payPalAccount.getField(TOKEN), is("trm-ac5727ac-8fe7-42fb-b69d-977ebdd7b48b"));

        Hyperwallet.getDefault().updatePayPalAccount(payPalAccount, mListener);
        mAwait.await(50, TimeUnit.MILLISECONDS);

        RecordedRequest recordedRequest = mServer.getRequest();
        assertThat(recordedRequest.getMethod(), is(PUT.name()));
        verify(mListener).onSuccess(mPayPalAccountCaptor.capture());
        verify(mListener, never()).onFailure(any(HyperwalletException.class));

        PayPalAccount payPalAccountResponse = mPayPalAccountCaptor.getValue();
        assertThat(payPalAccountResponse, is(notNullValue()));

        assertThat(recordedRequest.getPath(),
                is("/rest/v3/users/usr-fbfd5848-60d0-43c5-8462-099c959b49c7/paypal-accounts/trm-ac5727ac-8fe7-42fb"
                        + "-b69d-977ebdd7b48b"));

        assertThat(payPalAccountResponse.getField(TOKEN), is("trm-ac5727ac-8fe7-42fb-b69d-977ebdd7b48b"));
        assertThat(payPalAccountResponse.getField(STATUS), is("ACTIVATED"));
        assertThat(payPalAccountResponse.getField(TYPE), is(PAYPAL_ACCOUNT));
        assertThat(payPalAccountResponse.getField(EMAIL), is("jsmith2@paypal.com"));
        assertThat(payPalAccountResponse.getField(TRANSFER_METHOD_CURRENCY), is("USD"));
        assertThat(payPalAccountResponse.getField(TRANSFER_METHOD_COUNTRY), is("US"));
        assertThat(payPalAccountResponse.getField(CREATED_ON), is("2019-01-09T22:50:14"));
    }

    @Test
    public void testUpdatePayPalAccount_withValidationError() throws InterruptedException {

        String responseBody = mExternalResourceManager.getResourceContentError(
                "paypal_account_update_error_response.json");
        mServer.mockResponse().withHttpResponseCode(HttpURLConnection.HTTP_BAD_REQUEST).withBody(responseBody).mock();

        final PayPalAccount payPalAccount = new PayPalAccount
                .Builder()
                .email("00G0G1ema.com")
                .token("trm-56b976c5-26b2-42fa-87cf-14b3366673c6")
                .build();

        assertThat(payPalAccount.getField(EMAIL), is("00G0G1ema.com"));
        assertThat(payPalAccount.getField(TOKEN),
                is("trm-56b976c5-26b2-42fa-87cf-14b3366673c6"));

        Hyperwallet.getDefault().updatePayPalAccount(payPalAccount, mListener);
        mAwait.await(50, TimeUnit.MILLISECONDS);

        RecordedRequest recordedRequest = mServer.getRequest();
        assertThat(recordedRequest.getMethod(), is(PUT.name()));
        verify(mListener, never()).onSuccess(any(PayPalAccount.class));
        verify(mListener).onFailure(mExceptionCaptor.capture());

        HyperwalletException hyperwalletException = mExceptionCaptor.getValue();
        assertThat(hyperwalletException, is(notNullValue()));
        assertThat(((HyperwalletRestException) hyperwalletException).getHttpCode(),
                is(HttpURLConnection.HTTP_BAD_REQUEST));

        assertThat(recordedRequest.getPath(),
                is("/rest/v3/users/usr-fbfd5848-60d0-43c5-8462-099c959b49c7/paypal-accounts/trm-56b976c5-26b2-42fa-87cf"
                        + "-14b3366673c6"));

        HyperwalletErrors hyperwalletErrors = hyperwalletException.getHyperwalletErrors();
        assertThat(hyperwalletErrors, is(notNullValue()));
        assertThat(hyperwalletErrors.getErrors(), hasSize(1));

        HyperwalletError hyperwalletError1 = hyperwalletErrors.getErrors().get(0);
        assertThat(hyperwalletError1.getCode(), is("CONSTRAINT_VIOLATIONS"));
        assertThat(hyperwalletError1.getFieldName(), is("email"));
        assertThat(hyperwalletError1.getMessage(), is("Invalid Email"));
    }
}
