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

import static com.hyperwallet.android.util.HttpMethod.GET;

import com.hyperwallet.android.Hyperwallet;
import com.hyperwallet.android.exception.HyperwalletException;
import com.hyperwallet.android.exception.HyperwalletRestException;
import com.hyperwallet.android.listener.HyperwalletListener;
import com.hyperwallet.android.model.Error;
import com.hyperwallet.android.model.Errors;
import com.hyperwallet.android.model.paging.PageList;
import com.hyperwallet.android.model.receipt.Receipt;
import com.hyperwallet.android.model.receipt.ReceiptDetails;
import com.hyperwallet.android.model.receipt.ReceiptQueryParam;
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
public class ListUserReceiptsTest {
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
    private HyperwalletListener<PageList<Receipt>> mListener;
    @Captor
    private ArgumentCaptor<PageList<Receipt>> mCaptor;
    @Captor
    private ArgumentCaptor<HyperwalletException> mExceptionCaptor;

    @Test
    public void testListUserReceipts_returnsReceipts() throws InterruptedException {

        String responseBody = mExternalResourceManager.getResourceContent("receipts_response.json");
        mServer.mockResponse().withHttpResponseCode(HTTP_OK).withBody(responseBody).mock();


        final ReceiptQueryParam.Builder builder = new ReceiptQueryParam.Builder();
        ReceiptQueryParam receiptQueryParam = builder.build();

        assertThat(receiptQueryParam, is(notNullValue()));
        Hyperwallet.getDefault().listUserReceipts(receiptQueryParam, mListener);

        mAwait.await(150, TimeUnit.MILLISECONDS);

        RecordedRequest recordedRequest = mServer.getRequest();
        assertThat(recordedRequest.getMethod(), is(GET.name()));
        verify(mListener).onSuccess(mCaptor.capture());
        verify(mListener, never()).onFailure(any(HyperwalletException.class));

        PageList<Receipt> receiptResponse = mCaptor.getValue();

        assertThat(receiptResponse.getCount(), is(2));
        assertThat(receiptResponse.getDataList(), hasSize(2));
        assertThat(receiptResponse.getOffset(), is(0));
        assertThat(receiptResponse.getLimit(), is(10));

        assertThat(recordedRequest.getPath(),
                containsString("/rest/v3/users/test-user-token/receipts?"));
        assertThat(recordedRequest.getPath(), containsString("limit=10"));
        assertThat(recordedRequest.getPath(), containsString("offset=0"));

        Receipt receipt = receiptResponse.getDataList().get(0);
        assertThat(receipt.getJournalId(), is("3051579"));
        assertThat(receipt.getType(), is("PAYMENT"));
        assertThat(receipt.getCreatedOn(), is("2017-11-01T17:08:58"));
        assertThat(receipt.getEntry(), is(Receipt.Entries.CREDIT));
        assertThat(receipt.getSourceToken(), is("act-12345"));
        assertThat(receipt.getDestinationToken(), is("test-user-token"));
        assertThat(receipt.getAmount(), is("20.00"));
        assertThat(receipt.getFee(), is("0.00"));
        assertThat(receipt.getDetails(), is(notNullValue()));
        final ReceiptDetails receiptDetail = receipt.getDetails();
        assertThat(receiptDetail.getClientPaymentId(), is("8OxXefx5"));
        assertThat(receiptDetail.getPayeeName(), is("A Person"));

    }

    @Test
    public void testListUserReceipts_returnsDebitReceipt() throws InterruptedException {

        String responseBody = mExternalResourceManager.getResourceContent("receipt_debit_response.json");
        mServer.mockResponse().withHttpResponseCode(HTTP_OK).withBody(responseBody).mock();

        final ReceiptQueryParam.Builder builder = new ReceiptQueryParam.Builder();
        ReceiptQueryParam receiptQueryParam = builder.build();

        assertThat(receiptQueryParam, is(notNullValue()));
        Hyperwallet.getDefault().listUserReceipts(receiptQueryParam, mListener);

        mAwait.await(150, TimeUnit.MILLISECONDS);

        RecordedRequest recordedRequest = mServer.getRequest();
        assertThat(recordedRequest.getMethod(), is(GET.name()));
        verify(mListener).onSuccess(mCaptor.capture());
        verify(mListener, never()).onFailure(any(HyperwalletException.class));

        PageList<Receipt> receiptResponse = mCaptor.getValue();

        assertThat(receiptResponse.getCount(), is(1));
        assertThat(receiptResponse.getDataList(), hasSize(1));
        assertThat(receiptResponse.getOffset(), is(0));
        assertThat(receiptResponse.getLimit(), is(10));

        assertThat(recordedRequest.getPath(),
                containsString("/rest/v3/users/test-user-token/receipts?"));
        assertThat(recordedRequest.getPath(), containsString("limit=10"));
        assertThat(recordedRequest.getPath(), containsString("offset=0"));

        Receipt receipt = receiptResponse.getDataList().get(0);
        assertThat(receipt.getEntry(), is(Receipt.Entries.DEBIT));
    }

    @Test
    public void testListUserReceipts_returnsNoReceipts() throws InterruptedException {
        mServer.mockResponse().withHttpResponseCode(HTTP_NO_CONTENT).withBody("").mock();

        final ReceiptQueryParam.Builder builder = new ReceiptQueryParam.Builder();
        ReceiptQueryParam receiptQueryParam = builder.build();

        assertThat(receiptQueryParam, is(notNullValue()));
        Hyperwallet.getDefault().listUserReceipts(receiptQueryParam, mListener);

        mAwait.await(100, TimeUnit.MILLISECONDS);

        RecordedRequest recordedRequest = mServer.getRequest();
        assertThat(recordedRequest.getMethod(), is(GET.name()));
        assertThat(recordedRequest.getPath(),
                containsString("/rest/v3/users/test-user-token/receipts?"));
        assertThat(recordedRequest.getPath(), containsString("limit=10"));
        assertThat(recordedRequest.getPath(), containsString("offset=0"));

        verify(mListener).onSuccess(mCaptor.capture());
        verify(mListener, never()).onFailure(any(HyperwalletException.class));

        PageList<Receipt> receiptsResponse = mCaptor.getValue();
        assertThat(receiptsResponse, is(nullValue()));
    }

    @Test
    public void testListUserReceipts_returnsError() throws InterruptedException {
        String responseBody = mExternalResourceManager.getResourceContentError("system_error_response.json");
        mServer.mockResponse().withHttpResponseCode(HTTP_INTERNAL_ERROR).withBody(responseBody).mock();

        final ReceiptQueryParam.Builder builder = new ReceiptQueryParam.Builder();
        ReceiptQueryParam receiptQueryParam = builder.build();

        Hyperwallet.getDefault().listUserReceipts(receiptQueryParam, mListener);
        mAwait.await(500, TimeUnit.MILLISECONDS);

        verify(mListener, never()).onSuccess(ArgumentMatchers.<PageList<Receipt>>any());
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
                containsString("/rest/v3/users/test-user-token/receipts?"));
        assertThat(recordedRequest.getPath(), containsString("limit=10"));
        assertThat(recordedRequest.getPath(), containsString("offset=0"));
    }

}
