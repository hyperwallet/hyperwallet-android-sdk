package com.hyperwallet.android.transfer;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import static com.hyperwallet.android.model.transfer.Transfer.TransferStatuses.QUOTED;
import static com.hyperwallet.android.util.HttpMethod.POST;

import com.hyperwallet.android.Hyperwallet;
import com.hyperwallet.android.exception.HyperwalletException;
import com.hyperwallet.android.listener.HyperwalletListener;
import com.hyperwallet.android.model.Error;
import com.hyperwallet.android.model.Errors;
import com.hyperwallet.android.model.transfer.ForeignExchange;
import com.hyperwallet.android.model.transfer.Transfer;
import com.hyperwallet.android.rule.ExternalResourceManager;
import com.hyperwallet.android.rule.HyperwalletMockWebServer;
import com.hyperwallet.android.rule.HyperwalletSdkMock;
import com.hyperwallet.android.util.DateUtil;

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
public class CreateTransferTest {
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
    private HyperwalletListener<Transfer> mListener;
    @Captor
    private ArgumentCaptor<Transfer> mTransferCaptor;
    @Captor
    private ArgumentCaptor<HyperwalletException> mExceptionCaptor;

    @Test
    public void testCreateTransfer_withSuccess() throws InterruptedException {

        String responseBody = mExternalResourceManager.getResourceContent("transfer_response.json");
        mServer.mockResponse().withHttpResponseCode(HttpURLConnection.HTTP_CREATED).withBody(responseBody).mock();

        final Transfer.Builder transferBuilder = new Transfer.Builder()
                .clientTransferID("1234567890123")
                .destinationAmount("62.29")
                .destinationCurrency("USD")
                .notes("Partial-Balance Transfer")
                .memo("TransferClientId321")
                .sourceToken("usr-4321")
                .destinationToken("trm-246");
        final Transfer transfer = transferBuilder.build();

        Hyperwallet.getDefault().createTransfer(transfer, mListener);
        mAwait.await(50, TimeUnit.MILLISECONDS);

        RecordedRequest recordedRequest = mServer.getRequest();
        assertThat(recordedRequest.getMethod(), is(POST.name()));
        verify(mListener).onSuccess(mTransferCaptor.capture());
        verify(mListener, never()).onFailure(any(HyperwalletException.class));

        Transfer transferResponse = mTransferCaptor.getValue();
        assertThat(transferResponse, is(notNullValue()));

        assertThat(recordedRequest.getPath(), is("/rest/v3/transfers"));

        assertThat(transferResponse.getStatus(), is(QUOTED));
        assertThat(transferResponse.getClientTransferId(), is("1234567890123"));
        assertThat(transferResponse.getToken(), is("trf-123"));
        assertThat(transferResponse.getCreatedOn(), is(DateUtil.fromDateTimeString("2019-07-01T00:00:00")));
        assertThat(transferResponse.getSourceToken(), is("usr-4321"));
        assertThat(transferResponse.getSourceAmount(), is("80"));
        assertThat(transferResponse.getSourceCurrency(), is("CAD"));
        assertThat(transferResponse.getDestinationToken(), is("trm-246"));
        assertThat(transferResponse.getDestinationAmount(), is("62.29"));
        assertThat(transferResponse.getDestinationCurrency(), is("USD"));
        assertThat(transferResponse.getNotes(), is("Partial-Balance Transfer"));
        assertThat(transferResponse.getMemo(), is("TransferClientId321"));
        assertThat(transferResponse.getExpiresOn(), is(DateUtil.fromDateTimeString("2019-07-01T00:02:00")));

        assertThat(transferResponse.getForeignExchanges(), hasSize(1));
        final ForeignExchange foreignExchange = transferResponse.getForeignExchanges().get(0);
        assertThat(foreignExchange.getSourceAmount(), is("100.00"));
        assertThat(foreignExchange.getSourceCurrency(), is("CAD"));
        assertThat(foreignExchange.getDestinationAmount(), is("63.49"));
        assertThat(foreignExchange.getDestinationCurrency(), is("USD"));
        assertThat(foreignExchange.getRate(), is("0.79"));
    }

    @Test
    public void testCreateTransfer_withValidationError() throws InterruptedException {
        String responseBody = mExternalResourceManager.getResourceContentError("transfer_error_response.json");
        mServer.mockResponse().withHttpResponseCode(HttpURLConnection.HTTP_BAD_REQUEST).withBody(responseBody).mock();

        final Transfer transfer = new Transfer.Builder().build();

        Hyperwallet.getDefault().createTransfer(transfer, mListener);
        mAwait.await(150, TimeUnit.MILLISECONDS);

        RecordedRequest recordedRequest = mServer.getRequest();
        assertThat(recordedRequest.getMethod(), is(POST.name()));
        verify(mListener, never()).onSuccess(any(Transfer.class));
        verify(mListener).onFailure(mExceptionCaptor.capture());

        HyperwalletException hyperwalletException = mExceptionCaptor.getValue();
        assertThat(hyperwalletException, is(notNullValue()));

        assertThat(recordedRequest.getPath(), is("/rest/v3/transfers"));

        Errors errors = hyperwalletException.getErrors();
        assertThat(errors.getErrors(), hasSize(1));

        Error error = errors.getErrors().get(0);
        assertThat(error.getCode(), is("INVALID_DESTINATION_TOKEN"));
        assertThat(error.getMessage(),
                is("The destination token you provided doesn’t exist or is not a valid destination."));
    }
}
