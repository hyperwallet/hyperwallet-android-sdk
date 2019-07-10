package com.hyperwallet.android.transfermethod;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import static com.hyperwallet.android.model.transfer.Transfer.TransferStatuses.QUOTED;
import static com.hyperwallet.android.util.DateUtil.fromDateTimeString;
import static com.hyperwallet.android.util.HttpMethod.GET;

import com.hyperwallet.android.Hyperwallet;
import com.hyperwallet.android.exception.HyperwalletException;
import com.hyperwallet.android.listener.HyperwalletListener;
import com.hyperwallet.android.model.HyperwalletError;
import com.hyperwallet.android.model.HyperwalletErrors;
import com.hyperwallet.android.model.transfer.Transfer;
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
public class HyperwalletGetTransferTest {

    @Rule
    public HyperwalletMockWebServer mServer = new HyperwalletMockWebServer();
    @Rule
    public HyperwalletSdkMock mSdkMock = new HyperwalletSdkMock(mServer);
    @Rule
    public HyperwalletExternalResourceManager mExternalResourceManager = new HyperwalletExternalResourceManager();
    @Rule
    public MockitoRule mMockito = MockitoJUnit.rule();

    @Mock
    private HyperwalletListener<Transfer> mListener;
    @Captor
    private ArgumentCaptor<Transfer> mTransferArgumentCaptor;
    @Captor
    private ArgumentCaptor<HyperwalletException> mExceptionCaptor;

    private CountDownLatch mAwait = new CountDownLatch(1);

    @Test
    public void testGetTransfer_returnsTransfer() throws InterruptedException {

        String responseBody = mExternalResourceManager.getResourceContent("transfer_response.json");
        mServer.mockResponse().withHttpResponseCode(HttpURLConnection.HTTP_OK).withBody(responseBody).mock();

        Hyperwallet.getDefault().getTransfer("trf-123", mListener);
        mAwait.await(100, TimeUnit.MILLISECONDS);

        RecordedRequest recordedRequest = mServer.getRequest();
        assertThat(recordedRequest.getPath(),
                is("/rest/v3/transfers/trf-123"));
        assertThat(recordedRequest.getMethod(), is(GET.name()));

        verify(mListener).onSuccess(mTransferArgumentCaptor.capture());
        verify(mListener, never()).onFailure(any(HyperwalletException.class));

        Transfer transferResponse = mTransferArgumentCaptor.getValue();
        assertThat(transferResponse, is(notNullValue()));
        assertThat(transferResponse.getToken(), is("trf-123"));
        assertThat(transferResponse.getStatus(), is(QUOTED));
        assertThat(transferResponse.getCreatedOn(), is(fromDateTimeString("2019-07-01T00:00:00")));
        assertThat(transferResponse.getClientTransferId(), is("1234567890123"));
        assertThat(transferResponse.getSourceToken(), is("usr-4321"));
        assertThat(transferResponse.getSourceAmount(), is("80"));
        assertThat(transferResponse.getSourceCurrency(), is("CAD"));
        assertThat(transferResponse.getDestinationToken(), is("trm-246"));
        assertThat(transferResponse.getDestinationAmount(), is("62.29"));
        assertThat(transferResponse.getDestinationCurrency(), is("USD"));
        assertThat(transferResponse.getForeignExchanges(), is(notNullValue()));
        assertThat(transferResponse.getNotes(), is("Partial-Balance Transfer"));
        assertThat(transferResponse.getMemo(), is("TransferClientId321"));
        assertThat(transferResponse.getExpiresOn(), is(fromDateTimeString("2019-07-01T00:02:00")));
    }

    @Test
    public void testGetTransfer_returnsNoTransfer() throws InterruptedException {

        String responseBody = "";
        mServer.mockResponse().withHttpResponseCode(HttpURLConnection.HTTP_NO_CONTENT).withBody(responseBody).mock();

        Hyperwallet.getDefault().getTransfer("trf-123", mListener);
        mAwait.await(100, TimeUnit.MILLISECONDS);

        RecordedRequest recordedRequest = mServer.getRequest();
        assertThat(recordedRequest.getPath(),
                is("/rest/v3/transfers/trf-123"));
        assertThat(recordedRequest.getMethod(), is(GET.name()));

        verify(mListener).onSuccess(mTransferArgumentCaptor.capture());
        verify(mListener, never()).onFailure(any(HyperwalletException.class));

        Object transfer = mTransferArgumentCaptor.getValue();
        assertThat(transfer, is(nullValue()));
    }

    @Test
    public void testGetTransfer_returnsError() throws InterruptedException {

        String responseBody = mExternalResourceManager.getResourceContentError("system_error_response.json");
        mServer.mockResponse().withHttpResponseCode(HttpURLConnection.HTTP_BAD_REQUEST).withBody(responseBody).mock();

        Hyperwallet.getDefault().getTransfer("trf-123", mListener);
        mAwait.await(100, TimeUnit.MILLISECONDS);

        RecordedRequest recordedRequest = mServer.getRequest();
        assertThat(recordedRequest.getPath(), is("/rest/v3/transfers/trf-123"));
        assertThat(recordedRequest.getMethod(), is(GET.name()));

        verify(mListener, never()).onSuccess(any(Transfer.class));
        verify(mListener).onFailure(mExceptionCaptor.capture());

        HyperwalletException exception = mExceptionCaptor.getValue();
        assertThat(exception, is(notNullValue()));
        HyperwalletErrors errors = exception.getHyperwalletErrors();
        assertThat(errors, is(notNullValue()));
        assertThat(errors.getErrors(), is(notNullValue()));
        assertThat(errors.getErrors().size(), is(1));

        HyperwalletError error = errors.getErrors().get(0);
        assertThat(error.getCode(), is("SYSTEM_ERROR"));
        assertThat(error.getMessage(),
                is("A system error has occurred. Please try again. If you continue to receive this error, please "
                        + "contact customer support for assistance (Ref ID: 99b4ad5c-4aac-4cc2-aa9b-4b4f4844ac9b)."));

    }
}
