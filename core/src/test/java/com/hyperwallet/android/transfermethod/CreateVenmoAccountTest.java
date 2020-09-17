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
import static com.hyperwallet.android.util.HttpMethod.POST;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
public class CreateVenmoAccountTest {

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
    public void testCreateVenmoAccount_withSuccess() throws InterruptedException {

        String responseBody = mExternalResourceManager.getResourceContent("venmo_account_response.json");
        mServer.mockResponse().withHttpResponseCode(HttpURLConnection.HTTP_CREATED).withBody(responseBody).mock();

        final VenmoAccount venmoAccount = new VenmoAccount
                .Builder("US", "USD", "9876543210")
                .build();

        assertThat(venmoAccount.getField(TRANSFER_METHOD_COUNTRY), is("US"));
        assertThat(venmoAccount.getField(TRANSFER_METHOD_CURRENCY), is("USD"));
        assertThat(venmoAccount.getField(VENMO_ACCOUNT_ID), is("9876543210"));

        Hyperwallet.getDefault().createVenmoAccount(venmoAccount, mListener);
        mAwait.await(50, TimeUnit.MILLISECONDS);

        RecordedRequest recordedRequest = mServer.getRequest();
        assertThat(recordedRequest.getMethod(), is(POST.name()));
        verify(mListener).onSuccess(mVenmoAccountCaptor.capture());
        verify(mListener, never()).onFailure(any(HyperwalletException.class));

        VenmoAccount venmoAccountResponse = mVenmoAccountCaptor.getValue();
        assertThat(venmoAccountResponse, is(notNullValue()));

        //venmo account info
        assertThat(recordedRequest.getPath(),
                is("/rest/v3/users/test-user-token/venmo-accounts"));

        assertThat(venmoAccountResponse.getField(STATUS), is(ACTIVATED));
        assertThat(venmoAccountResponse.getField(TOKEN), is("trm-fake-token"));
        assertThat(venmoAccountResponse.getField(TRANSFER_METHOD_COUNTRY), is("US"));
        assertThat(venmoAccountResponse.getField(TRANSFER_METHOD_CURRENCY), is("USD"));
        assertThat(venmoAccountResponse.getField(TYPE), is(VENMO_ACCOUNT));
        assertThat(venmoAccountResponse.getField(CREATED_ON), is(notNullValue()));
        assertThat(venmoAccountResponse.getField(VENMO_ACCOUNT_ID), is("9876543210"));
    }

    @Test
    public void testCreateVenmoAccount_withValidationError() throws InterruptedException {
        String responseBody = mExternalResourceManager.getResourceContentError("transfer_method_error_response.json");
        mServer.mockResponse().withHttpResponseCode(HttpURLConnection.HTTP_BAD_REQUEST).withBody(responseBody).mock();

        final VenmoAccount venmoAccount = new VenmoAccount
                .Builder("", "USD", "9876543210")
                .build();

        Hyperwallet.getDefault().createVenmoAccount(venmoAccount, mListener);
        mAwait.await(50, TimeUnit.MILLISECONDS);

        RecordedRequest recordedRequest = mServer.getRequest();
        assertThat(recordedRequest.getMethod(), is(POST.name()));
        verify(mListener, never()).onSuccess(any(VenmoAccount.class));
        verify(mListener).onFailure(mExceptionCaptor.capture());

        HyperwalletException hyperwalletException = mExceptionCaptor.getValue();
        assertThat(hyperwalletException, is(notNullValue()));

        assertThat(recordedRequest.getPath(),
                is("/rest/v3/users/test-user-token/venmo-accounts"));

        Errors errors = hyperwalletException.getErrors();
        assertThat(errors.getErrors(), hasSize(1));

        Error error = errors.getErrors().get(0);
        assertThat(error.getCode(), is("CONSTRAINT_VIOLATIONS"));
        assertThat(error.getFieldName(), is("transferMethodCountry"));
        assertThat(error.getMessage(), is("You must provide a value for this field"));
    }
}
