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

import static com.hyperwallet.android.model.transfer.Transfer.TransferStatuses.QUOTED;
import static com.hyperwallet.android.util.DateUtil.fromDateTimeString;
import static com.hyperwallet.android.util.HttpMethod.GET;

import com.hyperwallet.android.Hyperwallet;
import com.hyperwallet.android.exception.HyperwalletException;
import com.hyperwallet.android.exception.HyperwalletRestException;
import com.hyperwallet.android.listener.HyperwalletListener;
import com.hyperwallet.android.model.Error;
import com.hyperwallet.android.model.Errors;
import com.hyperwallet.android.model.paging.PageList;
import com.hyperwallet.android.model.transfer.ForeignExchange;
import com.hyperwallet.android.model.transfer.Transfer;
import com.hyperwallet.android.model.transfer.TransferQueryParam;
import com.hyperwallet.android.rule.ExternalResourceManager;
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
public class ListTransfersTest {
    @Rule
    public final ExternalResourceManager mExternalResourceManager = new ExternalResourceManager();
    private final CountDownLatch mAwait = new CountDownLatch(1);
    @Rule
    public HyperwalletMockWebServer mServer = new HyperwalletMockWebServer();
    @Rule
    public HyperwalletSdkMock mHyperwalletSdkMock = new HyperwalletSdkMock(mServer);
    @Rule
    public MockitoRule mMockito = MockitoJUnit.rule();
    @Mock
    private HyperwalletListener<PageList<Transfer>> mListener;
    @Captor
    private ArgumentCaptor<PageList<Transfer>> mCaptor;
    @Captor
    private ArgumentCaptor<HyperwalletException> mExceptionCaptor;

    @Test
    public void testListTransfers_returnsTransfers() throws InterruptedException {

        String responseBody = mExternalResourceManager.getResourceContent("transfers_response.json");
        mServer.mockResponse().withHttpResponseCode(HTTP_OK).withBody(responseBody).mock();


        final TransferQueryParam.Builder builder = new TransferQueryParam.Builder();
        TransferQueryParam listTransfersQueryParam = builder.build();

        assertThat(listTransfersQueryParam, is(notNullValue()));
        Hyperwallet.getDefault().listTransfers(listTransfersQueryParam, mListener);

        mAwait.await(150, TimeUnit.MILLISECONDS);

        RecordedRequest recordedRequest = mServer.getRequest();
        assertThat(recordedRequest.getMethod(), is(GET.name()));
        verify(mListener).onSuccess(mCaptor.capture());
        verify(mListener, never()).onFailure(any(HyperwalletException.class));

        PageList<Transfer> receiptResponse = mCaptor.getValue();

        assertThat(receiptResponse.getCount(), is(426004));
        assertThat(receiptResponse.getDataList(), hasSize(1));
        assertThat(receiptResponse.getOffset(), is(0));
        assertThat(receiptResponse.getLimit(), is(10));

        assertThat(recordedRequest.getPath(),
                containsString("/transfers?"));
        assertThat(recordedRequest.getPath(), containsString("limit=10"));
        assertThat(recordedRequest.getPath(), containsString("offset=0"));

        Transfer transfer = receiptResponse.getDataList().get(0);
        assertThat(transfer.getToken(), is("trf-12345"));
        assertThat(transfer.getStatus(), is(QUOTED));
        assertThat(transfer.getCreatedOn(), is(fromDateTimeString("2019-07-12T16:00:00")));
        assertThat(transfer.getClientTransferId(), is("6712348070812"));
        assertThat(transfer.getSourceToken(), is("test-user-token"));
        assertThat(transfer.getSourceAmount(), is("90.13"));
        assertThat(transfer.getSourceCurrency(), is("CAD"));
        assertThat(transfer.getDestinationToken(), is("trm-fake-token"));
        assertThat(transfer.getDestinationAmount(), is("70"));
        assertThat(transfer.getDestinationCurrency(), is("USD"));
        assertThat(transfer.getForeignExchanges(), is(notNullValue()));
        assertThat(transfer.getForeignExchanges(), is(Matchers.<ForeignExchange>hasSize(1)));

        final ForeignExchange foreignExchange = transfer.getForeignExchanges().get(0);
        assertThat(foreignExchange.getDestinationAmount(), is("71.20"));
        assertThat(foreignExchange.getDestinationCurrency(), is("USD"));
        assertThat(foreignExchange.getRate(), is("0.79"));
        assertThat(foreignExchange.getSourceAmount(), is("90.13"));
        assertThat(foreignExchange.getSourceCurrency(), is("CAD"));

    }

    @Test
    public void testListTransfers_returnsNoTransfers() throws InterruptedException {
        mServer.mockResponse().withHttpResponseCode(HTTP_NO_CONTENT).withBody("").mock();

        final TransferQueryParam.Builder builder = new TransferQueryParam.Builder();
        TransferQueryParam receiptQueryParam = builder.build();

        assertThat(receiptQueryParam, is(notNullValue()));
        Hyperwallet.getDefault().listTransfers(receiptQueryParam, mListener);

        mAwait.await(100, TimeUnit.MILLISECONDS);

        RecordedRequest recordedRequest = mServer.getRequest();
        assertThat(recordedRequest.getMethod(), is(GET.name()));
        assertThat(recordedRequest.getPath(),
                containsString("/transfers?"));
        assertThat(recordedRequest.getPath(), containsString("limit=10"));
        assertThat(recordedRequest.getPath(), containsString("offset=0"));

        verify(mListener).onSuccess(mCaptor.capture());
        verify(mListener, never()).onFailure(any(HyperwalletException.class));

        PageList<Transfer> receiptsResponse = mCaptor.getValue();
        assertThat(receiptsResponse, is(nullValue()));
    }

    @Test
    public void testListTransfers_returnsError() throws InterruptedException {
        String responseBody = mExternalResourceManager.getResourceContentError("system_error_response.json");
        mServer.mockResponse().withHttpResponseCode(HTTP_INTERNAL_ERROR).withBody(responseBody).mock();

        final TransferQueryParam.Builder builder = new TransferQueryParam.Builder();
        TransferQueryParam receiptQueryParam = builder.build();

        Hyperwallet.getDefault().listTransfers(receiptQueryParam, mListener);
        mAwait.await(500, TimeUnit.MILLISECONDS);

        verify(mListener, never()).onSuccess(ArgumentMatchers.<PageList<Transfer>>any());
        verify(mListener).onFailure(mExceptionCaptor.capture());

        HyperwalletException hyperwalletException = mExceptionCaptor.getValue();
        assertThat(hyperwalletException, is(notNullValue()));
        assertThat(((HyperwalletRestException) hyperwalletException).getHttpCode(),
                is(HTTP_INTERNAL_ERROR));

        Errors errors = hyperwalletException.getErrors();
        assertThat(errors, is(notNullValue()));
        assertThat(errors.getErrors(), is(notNullValue()));
        assertThat(errors.getErrors(), Matchers.<Error>hasSize(1));

        Error error = errors.getErrors().get(0);
        assertThat(error.getCode(), is("SYSTEM_ERROR"));
        assertThat(error.getMessage(),
                is("A system error has occurred. Please try again. If you continue to receive this error, please "
                        + "contact customer support for assistance (Ref ID: 99b4ad5c-4aac-4cc2-aa9b-4b4f4844ac9b)."));
        assertThat(error.getFieldName(), is(nullValue()));
        RecordedRequest recordedRequest = mServer.getRequest();
        assertThat(recordedRequest.getPath(),
                containsString("/transfers?"));
        assertThat(recordedRequest.getPath(), containsString("limit=10"));
        assertThat(recordedRequest.getPath(), containsString("offset=0"));
    }

}
