package com.hyperwallet.android.transfermethod;

import com.hyperwallet.android.Hyperwallet;
import com.hyperwallet.android.exception.HyperwalletException;
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

import static com.hyperwallet.android.model.StatusTransition.StatusDefinition.ACTIVATED;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.CREATED_ON;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.STATUS;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.TOKEN;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.TRANSFER_METHOD_COUNTRY;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.TRANSFER_METHOD_CURRENCY;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.TYPE;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.VENMO_ACCOUNT_ID;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodTypes.VENMO_ACCOUNT;
import static com.hyperwallet.android.util.HttpMethod.GET;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
public class GetVenmoAccountTest {
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
    private HyperwalletListener<VenmoAccount> mListener;
    @Captor
    private ArgumentCaptor<VenmoAccount> mVenmoAccountArgumentCaptor;
    @Captor
    private ArgumentCaptor<HyperwalletException> mExceptionCaptor;

    @Test
    public void testGetGetVenmoAccount_returnsAccount() throws InterruptedException {

        String responseBody = mExternalResourceManager.getResourceContent("venmo_account_response.json");
        mServer.mockResponse().withHttpResponseCode(HttpURLConnection.HTTP_OK).withBody(responseBody).mock();

        Hyperwallet.getDefault().getVenmoAccount("trm-fake-token", mListener);
        mAwait.await(COUNTDOWN_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);

        RecordedRequest recordedRequest = mServer.getRequest();
        assertThat(recordedRequest.getMethod(), is(GET.name()));
        assertThat(recordedRequest.getPath(),
                is("/rest/v3/users/test-user-token/venmo-accounts/trm-fake-token"));

        verify(mListener).onSuccess(mVenmoAccountArgumentCaptor.capture());
        verify(mListener, never()).onFailure(any(HyperwalletException.class));

        VenmoAccount venmoAccountResponse = mVenmoAccountArgumentCaptor.getValue();
        assertThat(venmoAccountResponse, is(notNullValue()));
        assertThat(venmoAccountResponse.getField(TYPE), is(VENMO_ACCOUNT));
        assertThat(venmoAccountResponse.getField(TOKEN), is("trm-fake-token"));
        assertThat(venmoAccountResponse.getField(STATUS), is(ACTIVATED));

        assertThat(venmoAccountResponse.getField(CREATED_ON), is("2019-01-09T22:50:14"));
        assertThat(venmoAccountResponse.getField(TRANSFER_METHOD_COUNTRY), is("US"));
        assertThat(venmoAccountResponse.getField(TRANSFER_METHOD_CURRENCY), is("USD"));
        assertThat(venmoAccountResponse.getField(VENMO_ACCOUNT_ID), is("9876543210"));

    }

    @Test
    public void testGetGetVenmoAccount_returnsNoAccount() throws InterruptedException {

        String responseBody = "";
        mServer.mockResponse().withHttpResponseCode(HttpURLConnection.HTTP_NO_CONTENT).withBody(responseBody).mock();

        Hyperwallet.getDefault().getVenmoAccount("trm-fake-token", mListener);
        mAwait.await(COUNTDOWN_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);

        RecordedRequest recordedRequest = mServer.getRequest();
        assertThat(recordedRequest.getMethod(), is(GET.name()));
        assertThat(recordedRequest.getPath(),
                is("/rest/v3/users/test-user-token/venmo-accounts/trm-fake-token"));

        verify(mListener).onSuccess(mVenmoAccountArgumentCaptor.capture());
        verify(mListener, never()).onFailure(any(HyperwalletException.class));

        VenmoAccount venmoAccount = mVenmoAccountArgumentCaptor.getValue();
        assertThat(venmoAccount, is(nullValue()));
    }

    @Test
    public void testGetGetVenmolAccount_returnsError() throws InterruptedException {

        String responseBody = mExternalResourceManager.getResourceContentError("system_error_response.json");
        mServer.mockResponse().withHttpResponseCode(HttpURLConnection.HTTP_BAD_REQUEST).withBody(responseBody).mock();

        Hyperwallet.getDefault().getVenmoAccount("trm-fake-token", mListener);
        mAwait.await(COUNTDOWN_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);

        RecordedRequest recordedRequest = mServer.getRequest();
        assertThat(recordedRequest.getMethod(), is(GET.name()));
        assertThat(recordedRequest.getPath(),
                is("/rest/v3/users/test-user-token/venmo-accounts/trm-fake-token"));

        verify(mListener, never()).onSuccess(any(VenmoAccount.class));
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
