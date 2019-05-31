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

import static com.hyperwallet.android.model.receipt.Receipt.ReceiptTypes.CARD_ACTIVATION_FEE;

import com.hyperwallet.android.Hyperwallet;
import com.hyperwallet.android.exception.HyperwalletException;
import com.hyperwallet.android.exception.HyperwalletRestException;
import com.hyperwallet.android.listener.HyperwalletListener;
import com.hyperwallet.android.model.HyperwalletError;
import com.hyperwallet.android.model.HyperwalletErrors;
import com.hyperwallet.android.model.paging.HyperwalletPageList;
import com.hyperwallet.android.model.receipt.Receipt;
import com.hyperwallet.android.model.receipt.ReceiptDetails;
import com.hyperwallet.android.model.receipt.ReceiptQueryParam;
import com.hyperwallet.android.rule.HyperwalletExternalResourceManager;
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

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import okhttp3.mockwebserver.RecordedRequest;

@RunWith(RobolectricTestRunner.class)
public class HyperwalletListReceiptsTest {
    @Rule
    public final HyperwalletExternalResourceManager mExternalResourceManager = new HyperwalletExternalResourceManager();
    @Rule
    public HyperwalletMockWebServer mServer = new HyperwalletMockWebServer();
    @Rule
    public HyperwalletSdkMock mSdkMock = new HyperwalletSdkMock(mServer);
    @Rule
    public MockitoRule mMockito = MockitoJUnit.rule();
    @Mock
    private HyperwalletListener<HyperwalletPageList<Receipt>> mListener;
    @Captor
    private ArgumentCaptor<HyperwalletPageList<Receipt>> mCaptor;
    @Captor
    private ArgumentCaptor<HyperwalletException> mExceptionCaptor;

    private final CountDownLatch mAwait = new CountDownLatch(1);


    @Test
    public void testListReceipts_returnsReceipts() throws InterruptedException {

        String responseBody = mExternalResourceManager.getResourceContent("receipts_response.json");
        mServer.mockResponse().withHttpResponseCode(HTTP_OK).withBody(responseBody).mock();


        final ReceiptQueryParam.Builder builder = new ReceiptQueryParam.Builder();
        ReceiptQueryParam receiptQueryParam = builder.build();

        assertThat(receiptQueryParam, is(notNullValue()));
        Hyperwallet.getDefault().listReceipts(receiptQueryParam, mListener);

        mAwait.await(150, TimeUnit.MILLISECONDS);

        RecordedRequest recordedRequest = mServer.getRequest();
        verify(mListener).onSuccess(mCaptor.capture());
        verify(mListener, never()).onFailure(any(HyperwalletException.class));

        HyperwalletPageList<Receipt> receiptResponse = mCaptor.getValue();

        assertThat(receiptResponse.getCount(), is(2));
        assertThat(receiptResponse.getDataList(), hasSize(2));
        assertThat(receiptResponse.getOffset(), is(0));
        assertThat(receiptResponse.getLimit(), is(10));

        assertThat(recordedRequest.getPath(),
                containsString("/rest/v3/users/usr-fbfd5848-60d0-43c5-8462-099c959b49c7/receipts?"));
        assertThat(recordedRequest.getPath(), containsString("limit=10"));
        assertThat(recordedRequest.getPath(), containsString("offset=0"));

        Receipt receipt = receiptResponse.getDataList().get(0);
        assertThat(receipt.getJournalId(), is("3051579"));
        assertThat(receipt.getType(), is("PAYMENT"));
        assertThat(receipt.getCreatedOn(), is("2017-11-01T17:08:58"));
        assertThat(receipt.getEntry(), is(Receipt.Entries.CREDIT));
        assertThat(receipt.getSourceToken(), is("act-12345"));
        assertThat(receipt.getDestinationToken(), is("usr-fbfd5848-60d0-43c5-8462-099c959b49c7"));
        assertThat(receipt.getAmount(), is("20.00"));
        assertThat(receipt.getFee(), is("0.00"));
        assertThat(receipt.getDetails(), is(notNullValue()));
        final ReceiptDetails receiptDetail = receipt.getDetails();
        assertThat(receiptDetail.getClientPaymentId(), is("8OxXefx5"));
        assertThat(receiptDetail.getPayeeName(), is("A Person"));

    }

    @Test
    public void testListReceipts_returnsCreditReceipt() throws InterruptedException {

        String responseBody = mExternalResourceManager.getResourceContent("receipt_credit_response.json");
        mServer.mockResponse().withHttpResponseCode(HTTP_OK).withBody(responseBody).mock();

        final ReceiptQueryParam.Builder builder = new ReceiptQueryParam.Builder();
        ReceiptQueryParam receiptQueryParam = builder.build();

        assertThat(receiptQueryParam, is(notNullValue()));
        Hyperwallet.getDefault().listReceipts(receiptQueryParam, mListener);

        mAwait.await(150, TimeUnit.MILLISECONDS);

        RecordedRequest recordedRequest = mServer.getRequest();
        verify(mListener).onSuccess(mCaptor.capture());
        verify(mListener, never()).onFailure(any(HyperwalletException.class));

        HyperwalletPageList<Receipt> receiptResponse = mCaptor.getValue();

        assertThat(receiptResponse.getCount(), is(1));
        assertThat(receiptResponse.getDataList(), hasSize(1));
        assertThat(receiptResponse.getOffset(), is(0));
        assertThat(receiptResponse.getLimit(), is(10));

        assertThat(recordedRequest.getPath(),
                containsString("/rest/v3/users/usr-fbfd5848-60d0-43c5-8462-099c959b49c7/receipts?"));
        assertThat(recordedRequest.getPath(), containsString("limit=10"));
        assertThat(recordedRequest.getPath(), containsString("offset=0"));

        Receipt receipt = receiptResponse.getDataList().get(0);
        assertThat(receipt.getEntry(), is(Receipt.Entries.CREDIT));
    }

    @Test
    public void testListReceipts_returnsDebitReceipt() throws InterruptedException {

        String responseBody = mExternalResourceManager.getResourceContent("receipt_debit_response.json");
        mServer.mockResponse().withHttpResponseCode(HTTP_OK).withBody(responseBody).mock();

        final ReceiptQueryParam.Builder builder = new ReceiptQueryParam.Builder();
        ReceiptQueryParam receiptQueryParam = builder.build();

        assertThat(receiptQueryParam, is(notNullValue()));
        Hyperwallet.getDefault().listReceipts(receiptQueryParam, mListener);

        mAwait.await(150, TimeUnit.MILLISECONDS);

        RecordedRequest recordedRequest = mServer.getRequest();
        verify(mListener).onSuccess(mCaptor.capture());
        verify(mListener, never()).onFailure(any(HyperwalletException.class));

        HyperwalletPageList<Receipt> receiptResponse = mCaptor.getValue();

        assertThat(receiptResponse.getCount(), is(1));
        assertThat(receiptResponse.getDataList(), hasSize(1));
        assertThat(receiptResponse.getOffset(), is(0));
        assertThat(receiptResponse.getLimit(), is(10));

        assertThat(recordedRequest.getPath(),
                containsString("/rest/v3/users/usr-fbfd5848-60d0-43c5-8462-099c959b49c7/receipts?"));
        assertThat(recordedRequest.getPath(), containsString("limit=10"));
        assertThat(recordedRequest.getPath(), containsString("offset=0"));

        Receipt receipt = receiptResponse.getDataList().get(0);
        assertThat(receipt.getEntry(), is(Receipt.Entries.DEBIT));
    }

    @Test
    public void testListReceipts_returnsNoReceipts() throws InterruptedException {
        mServer.mockResponse().withHttpResponseCode(HTTP_NO_CONTENT).withBody("").mock();

        final ReceiptQueryParam.Builder builder = new ReceiptQueryParam.Builder();
        ReceiptQueryParam receiptQueryParam = builder.build();

        assertThat(receiptQueryParam, is(notNullValue()));
        Hyperwallet.getDefault().listReceipts(receiptQueryParam, mListener);

        mAwait.await(100, TimeUnit.MILLISECONDS);

        RecordedRequest recordedRequest = mServer.getRequest();
        assertThat(recordedRequest.getPath(),
                containsString("/rest/v3/users/usr-fbfd5848-60d0-43c5-8462-099c959b49c7/receipts?"));
        assertThat(recordedRequest.getPath(), containsString("limit=10"));
        assertThat(recordedRequest.getPath(), containsString("offset=0"));

        verify(mListener).onSuccess(mCaptor.capture());
        verify(mListener, never()).onFailure(any(HyperwalletException.class));

        HyperwalletPageList<Receipt> receiptsResponse = mCaptor.getValue();
        assertThat(receiptsResponse, is(nullValue()));
    }

    @Test
    public void testListReceipts_returnsError() throws InterruptedException {
        String responseBody = mExternalResourceManager.getResourceContentError("system_error_response.json");
        mServer.mockResponse().withHttpResponseCode(HTTP_INTERNAL_ERROR).withBody(responseBody).mock();

        final ReceiptQueryParam.Builder builder = new ReceiptQueryParam.Builder();
        ReceiptQueryParam receiptQueryParam = builder.build();

        Hyperwallet.getDefault().listReceipts(receiptQueryParam, mListener);
        mAwait.await(500, TimeUnit.MILLISECONDS);

        verify(mListener, never()).onSuccess(ArgumentMatchers.<HyperwalletPageList<Receipt>>any());
        verify(mListener).onFailure(mExceptionCaptor.capture());

        HyperwalletException hyperwalletException = mExceptionCaptor.getValue();
        assertThat(hyperwalletException, is(notNullValue()));
        assertThat(((HyperwalletRestException) hyperwalletException).getHttpCode(),
                is(HTTP_INTERNAL_ERROR));

        HyperwalletErrors hyperwalletErrors = hyperwalletException.getHyperwalletErrors();
        assertThat(hyperwalletErrors, is(notNullValue()));
        assertThat(hyperwalletErrors.getErrors(), is(notNullValue()));
        assertThat(hyperwalletErrors.getErrors(), Matchers.<HyperwalletError>hasSize(1));

        HyperwalletError hyperwalletError = hyperwalletErrors.getErrors().get(0);
        assertThat(hyperwalletError.getCode(), is("SYSTEM_ERROR"));
        assertThat(hyperwalletError.getMessage(),
                is("A system error has occurred. Please try again. If you continue to receive this error, please "
                        + "contact customer support for assistance (Ref ID: 99b4ad5c-4aac-4cc2-aa9b-4b4f4844ac9b)."));
        assertThat(hyperwalletError.getFieldName(), is(nullValue()));
        RecordedRequest recordedRequest = mServer.getRequest();
        assertThat(recordedRequest.getPath(),
                containsString("/rest/v3/users/usr-fbfd5848-60d0-43c5-8462-099c959b49c7/receipts?"));
        assertThat(recordedRequest.getPath(), containsString("limit=10"));
        assertThat(recordedRequest.getPath(), containsString("offset=0"));
    }

    @Test
    public void testListReceipts_verifyQueryParams() throws InterruptedException {

        String responseBody = mExternalResourceManager.getResourceContent("receipts_response.json");
        mServer.mockResponse().withHttpResponseCode(HTTP_OK).withBody(responseBody).mock();


        Calendar calendar = Calendar.getInstance();
        calendar.set(2019, 5, 30, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date createdOn = calendar.getTime();
        calendar.set(2019, 4, 20);
        Date createdBefore = calendar.getTime();
        calendar.set(2018, 8, 10);
        Date createdAfter = calendar.getTime();

        final ReceiptQueryParam.Builder builder = new ReceiptQueryParam.Builder()
                .createdOn(createdOn)
                .createdBefore(createdBefore)
                .createdAfter(createdAfter)
                .sortByCreatedOnAsc()
                .amount("20.00")
                .currency("USD")
                .type(CARD_ACTIVATION_FEE)
                .limit(40)
                .offset(120)
                .currency("USD");

        ReceiptQueryParam receiptQueryParam = builder.build();
        Hyperwallet.getDefault().listReceipts(receiptQueryParam, mListener);
        mAwait.await(50, TimeUnit.MILLISECONDS);

        RecordedRequest recordedRequest = mServer.getRequest();
        assertThat(recordedRequest.getPath(),
                containsString("/rest/v3/users/usr-fbfd5848-60d0-43c5-8462-099c959b49c7/receipts?"));
        assertThat(recordedRequest.getPath(), containsString("amount=20.00"));
        assertThat(recordedRequest.getPath(), containsString("limit=40"));
        assertThat(recordedRequest.getPath(), containsString("offset=120"));
        assertThat(recordedRequest.getPath(), containsString("type=CARD_ACTIVATION_FEE"));
        assertThat(recordedRequest.getPath(), containsString("currency=USD"));
        assertThat(recordedRequest.getPath(), containsString("createdOn=2019-06-30T00:00:00"));
        assertThat(recordedRequest.getPath(), containsString("createdAfter=2018-09-10T00:00:00"));
        assertThat(recordedRequest.getPath(), containsString("createdBefore=2019-05-20T00:00:00"));
        assertThat(recordedRequest.getPath(), containsString("sortBy=+createdOn"));
    }

    @Test
    public void testListReceipts_verifyDescSortQueryParams() throws InterruptedException {

        String responseBody = mExternalResourceManager.getResourceContent("receipts_response.json");
        mServer.mockResponse().withHttpResponseCode(HTTP_OK).withBody(responseBody).mock();


        final ReceiptQueryParam.Builder builder = new ReceiptQueryParam.Builder()
                .sortByCurrencyDesc();

        ReceiptQueryParam receiptQueryParam = builder.build();
        Hyperwallet.getDefault().listReceipts(receiptQueryParam, mListener);
        mAwait.await(50, TimeUnit.MILLISECONDS);

        RecordedRequest recordedRequest = mServer.getRequest();
        assertThat(recordedRequest.getPath(),
                containsString("/rest/v3/users/usr-fbfd5848-60d0-43c5-8462-099c959b49c7/receipts?"));
        assertThat(recordedRequest.getPath(), containsString("limit=10"));
        assertThat(recordedRequest.getPath(), containsString("offset=0"));
        assertThat(recordedRequest.getPath(), containsString("sortBy=-currency"));
    }

}
