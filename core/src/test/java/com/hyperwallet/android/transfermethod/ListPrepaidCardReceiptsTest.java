package com.hyperwallet.android.transfermethod;

import android.content.res.Resources;

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
import static org.mockito.Mockito.when;

import com.hyperwallet.android.Hyperwallet;
import com.hyperwallet.android.exception.HyperwalletException;
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
import com.hyperwallet.android.sdk.R;

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
public class ListPrepaidCardReceiptsTest {
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
    @Mock
    private Resources mResources;

    @Test
    public void testListPrepaidCardReceipts_returnsReceipts() throws InterruptedException {

        String responseBody = mExternalResourceManager.getResourceContent("prepaid_card_receipts_response.json");
        mServer.mockResponse().withHttpResponseCode(HTTP_OK).withBody(responseBody).mock();


        final ReceiptQueryParam.Builder builder = new ReceiptQueryParam.Builder();
        ReceiptQueryParam receiptQueryParam = builder.build();

        assertThat(receiptQueryParam, is(notNullValue()));
        final String prepaidCardToken = "trm-fake-token";
        Hyperwallet.getDefault().listPrepaidCardReceipts(prepaidCardToken, receiptQueryParam, mListener);

        mAwait.await(150, TimeUnit.MILLISECONDS);

        RecordedRequest recordedRequest = mServer.getRequest();
        assertThat(recordedRequest.getMethod(), is(GET.name()));
        verify(mListener).onSuccess(mCaptor.capture());
        verify(mListener, never()).onFailure(any(HyperwalletException.class));

        PageList<Receipt> receiptResponse = mCaptor.getValue();

        assertThat(receiptResponse.getDataList(), hasSize(2));

        assertThat(recordedRequest.getPath(),
                containsString(
                        "/rest/v3/users/test-user-token/prepaid-cards/trm-fake-token/receipts?"));
        assertThat(recordedRequest.getPath(), containsString("limit"));
        assertThat(recordedRequest.getPath(), containsString("offset"));

        Receipt receipt = receiptResponse.getDataList().get(0);
        assertThat(receipt.getJournalId(), is("ABCDE_CC002212334"));
        assertThat(receipt.getType(), is("DEPOSIT"));
        assertThat(receipt.getCreatedOn(), is("2019-06-01T17:12:19"));
        assertThat(receipt.getEntry(), is(Receipt.Entries.CREDIT));
        assertThat(receipt.getDestinationToken(), is("trm-fake-token"));
        assertThat(receipt.getAmount(), is("18.05"));
        assertThat(receipt.getFee(), is(nullValue()));
        assertThat(receipt.getDetails(), is(notNullValue()));
        final ReceiptDetails receiptDetail = receipt.getDetails();
        assertThat(receiptDetail.getCardNumber(), is("************7917"));

    }

    @Test
    public void testListPrepaidCardReceipts_returnsDebitReceipt() throws InterruptedException {

        String responseBody = mExternalResourceManager.getResourceContent("receipt_debit_response.json");
        mServer.mockResponse().withHttpResponseCode(HTTP_OK).withBody(responseBody).mock();

        final ReceiptQueryParam.Builder builder = new ReceiptQueryParam.Builder();
        ReceiptQueryParam receiptQueryParam = builder.build();

        assertThat(receiptQueryParam, is(notNullValue()));
        final String prepaidCardToken = "trm-fake-token";
        Hyperwallet.getDefault().listPrepaidCardReceipts(prepaidCardToken, receiptQueryParam, mListener);

        mAwait.await(150, TimeUnit.MILLISECONDS);

        RecordedRequest recordedRequest = mServer.getRequest();
        assertThat(recordedRequest.getMethod(), is(GET.name()));
        verify(mListener).onSuccess(mCaptor.capture());
        verify(mListener, never()).onFailure(any(HyperwalletException.class));

        PageList<Receipt> receiptResponse = mCaptor.getValue();

        assertThat(receiptResponse.getDataList(), hasSize(1));

        assertThat(recordedRequest.getPath(),
                containsString(
                        "/rest/v3/users/test-user-token/prepaid-cards/trm-fake-token/receipts?"));

        Receipt receipt = receiptResponse.getDataList().get(0);
        assertThat(receipt.getEntry(), is(Receipt.Entries.DEBIT));
    }

    @Test
    public void testListPrepaidCardReceipts_returnsNoReceipts() throws InterruptedException {
        mServer.mockResponse().withHttpResponseCode(HTTP_NO_CONTENT).withBody("").mock();

        final ReceiptQueryParam.Builder builder = new ReceiptQueryParam.Builder();
        ReceiptQueryParam receiptQueryParam = builder.build();

        assertThat(receiptQueryParam, is(notNullValue()));
        final String prepaidCardToken = "trm-fake-token";
        Hyperwallet.getDefault().listPrepaidCardReceipts(prepaidCardToken, receiptQueryParam, mListener);

        mAwait.await(100, TimeUnit.MILLISECONDS);

        RecordedRequest recordedRequest = mServer.getRequest();
        assertThat(recordedRequest.getMethod(), is(GET.name()));
        assertThat(recordedRequest.getPath(),
                containsString(
                        "/rest/v3/users/test-user-token/prepaid-cards/trm-fake-token/receipts?"));

        verify(mListener).onSuccess(mCaptor.capture());
        verify(mListener, never()).onFailure(any(HyperwalletException.class));

        PageList<Receipt> receiptsResponse = mCaptor.getValue();
        assertThat(receiptsResponse, is(nullValue()));
    }

    @Test
    public void testListPrepaidCardReceipts_returnsError() throws InterruptedException {
        when(mResources.getString(R.string.unexpected_exception)).thenReturn(
                "An unexpected error has occurred, please try again");
        String responseBody = mExternalResourceManager.getResourceContentError("system_error_response.json");
        mServer.mockResponse().withHttpResponseCode(HTTP_INTERNAL_ERROR).withBody(responseBody).mock();

        final ReceiptQueryParam.Builder builder = new ReceiptQueryParam.Builder();
        ReceiptQueryParam receiptQueryParam = builder.build();

        final String prepaidCardToken = "trm-fake-token";
        Hyperwallet.getDefault().listPrepaidCardReceipts(prepaidCardToken, receiptQueryParam, mListener);
        mAwait.await(500, TimeUnit.MILLISECONDS);

        verify(mListener, never()).onSuccess(ArgumentMatchers.<PageList<Receipt>>any());
        verify(mListener).onFailure(mExceptionCaptor.capture());

        HyperwalletException hyperwalletException = mExceptionCaptor.getValue();
        assertThat(hyperwalletException, is(notNullValue()));

        Errors errors = hyperwalletException.getErrors();
        assertThat(errors, is(notNullValue()));
        assertThat(errors.getErrors(), is(notNullValue()));
        assertThat(errors.getErrors(), Matchers.<Error>hasSize(1));

        Error error = errors.getErrors().get(0);
        assertThat(error.getCode(), is("EC_UNEXPECTED_EXCEPTION"));
        assertThat(error.getMessageFromResourceWhenAvailable(mResources),
                is("An unexpected error has occurred, please try again"));
        assertThat(error.getFieldName(), is(nullValue()));
        RecordedRequest recordedRequest = mServer.getRequest();
        assertThat(recordedRequest.getPath(),
                containsString(
                        "/rest/v3/users/test-user-token/prepaid-cards/trm-fake-token/receipts?"));
    }
}
