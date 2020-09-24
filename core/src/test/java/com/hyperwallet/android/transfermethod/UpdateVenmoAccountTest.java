package com.hyperwallet.android.transfermethod;

import com.hyperwallet.android.Hyperwallet;
import com.hyperwallet.android.exception.HyperwalletException;
import com.hyperwallet.android.exception.HyperwalletRestException;
import com.hyperwallet.android.listener.HyperwalletListener;
import com.hyperwallet.android.model.Error;
import com.hyperwallet.android.model.Errors;
import com.hyperwallet.android.model.transfermethod.VenmoAccount;
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

import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.CREATED_ON;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.EMAIL;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.STATUS;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.TOKEN;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.TRANSFER_METHOD_COUNTRY;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.TRANSFER_METHOD_CURRENCY;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.TYPE;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.VENMO_ACCOUNT_ID;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodTypes.VENMO_ACCOUNT;
import static com.hyperwallet.android.util.HttpMethod.PUT;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
public class UpdateVenmoAccountTest {
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
    private HyperwalletListener<VenmoAccount> mListener;
    @Captor
    private ArgumentCaptor<VenmoAccount> mVenmoAccountCaptor;
    @Captor
    private ArgumentCaptor<HyperwalletException> mExceptionCaptor;

    @Test
    public void testUpdateVenmoAccount_withSuccess() throws InterruptedException {
        String responseBody = mExternalResourceManager.getResourceContent("venmo_account_update_response.json");
        mServer.mockResponse().withHttpResponseCode(HttpURLConnection.HTTP_OK).withBody(responseBody).mock();

        final VenmoAccount venmoAccount = new VenmoAccount
                .Builder()
                .accountId("9876543211")
                .token("trm-fake-token")
                .build();

        assertThat(venmoAccount.getField(VENMO_ACCOUNT_ID), is("9876543211"));
        assertThat(venmoAccount.getField(TOKEN), is("trm-fake-token"));

        Hyperwallet.getDefault().updateVenmoAccount(venmoAccount, mListener);
        mAwait.await(50, TimeUnit.MILLISECONDS);

        RecordedRequest recordedRequest = mServer.getRequest();
        assertThat(recordedRequest.getMethod(), is(PUT.name()));
        verify(mListener).onSuccess(mVenmoAccountCaptor.capture());
        verify(mListener, never()).onFailure(any(HyperwalletException.class));

        VenmoAccount venmoAccountResponse = mVenmoAccountCaptor.getValue();
        assertThat(venmoAccountResponse, is(notNullValue()));

        assertThat(recordedRequest.getPath(),
                is("/rest/v3/users/test-user-token/venmo-accounts/trm-fake-token"));

        assertThat(venmoAccountResponse.getField(TOKEN), is("trm-fake-token"));
        assertThat(venmoAccountResponse.getField(STATUS), is("ACTIVATED"));
        assertThat(venmoAccountResponse.getField(TYPE), is(VENMO_ACCOUNT));
        assertThat(venmoAccountResponse.getField(VENMO_ACCOUNT_ID), is("9876543211"));
        assertThat(venmoAccountResponse.getField(TRANSFER_METHOD_CURRENCY), is("USD"));
        assertThat(venmoAccountResponse.getField(TRANSFER_METHOD_COUNTRY), is("US"));
        assertThat(venmoAccountResponse.getField(CREATED_ON), is("2019-01-09T22:50:14"));
    }

    @Test
    public void testUpdateVenmoAccount_withValidationError() throws InterruptedException {

        String responseBody = mExternalResourceManager.getResourceContentError(
                "venmo_account_update_error_response.json");
        mServer.mockResponse().withHttpResponseCode(HttpURLConnection.HTTP_BAD_REQUEST).withBody(responseBody).mock();

        final VenmoAccount venmoAccount = new VenmoAccount
                .Builder()
                .accountId("1234567890")
                .token("trm-fake-token")
                .build();

        assertThat(venmoAccount.getField(VENMO_ACCOUNT_ID), is("1234567890"));
        assertThat(venmoAccount.getField(TOKEN),
                is("trm-fake-token"));

        Hyperwallet.getDefault().updateVenmoAccount(venmoAccount, mListener);
        mAwait.await(50, TimeUnit.MILLISECONDS);

        RecordedRequest recordedRequest = mServer.getRequest();
        assertThat(recordedRequest.getMethod(), is(PUT.name()));
        verify(mListener, never()).onSuccess(any(VenmoAccount.class));
        verify(mListener).onFailure(mExceptionCaptor.capture());

        HyperwalletException hyperwalletException = mExceptionCaptor.getValue();
        assertThat(hyperwalletException, is(notNullValue()));
        assertThat(((HyperwalletRestException) hyperwalletException).getHttpCode(),
                is(HttpURLConnection.HTTP_BAD_REQUEST));

        assertThat(recordedRequest.getPath(),
                is("/rest/v3/users/test-user-token/venmo-accounts/trm-fake-token"));

        Errors errors = hyperwalletException.getErrors();
        assertThat(errors, is(notNullValue()));
        assertThat(errors.getErrors(), hasSize(1));

        Error error1 = errors.getErrors().get(0);
        assertThat(error1.getCode(), is("CONSTRAINT_VIOLATIONS"));
        assertThat(error1.getMessage(), is("Mobile Number is invalid. The maximum length of this field is 10."));
    }
}
