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

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import okhttp3.mockwebserver.RecordedRequest;

@RunWith(RobolectricTestRunner.class)
public class HyperwalletListPrepaidCardReceiptsTest {
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
    public void testListPrepaidCardReceipts_returnsReceipts() throws InterruptedException {

        String responseBody = mExternalResourceManager.getResourceContent("prepaid_card_receipts_response.json");
        mServer.mockResponse().withHttpResponseCode(HTTP_OK).withBody(responseBody).mock();


        final ReceiptQueryParam.Builder builder = new ReceiptQueryParam.Builder();
        ReceiptQueryParam receiptQueryParam = builder.build();

        assertThat(receiptQueryParam, is(notNullValue()));
        final String prepaidCardToken = "trm-2345";
        Hyperwallet.getDefault().listPrepaidCardReceipts(prepaidCardToken, receiptQueryParam, mListener);

        mAwait.await(150, TimeUnit.MILLISECONDS);

        RecordedRequest recordedRequest = mServer.getRequest();
        assertThat(recordedRequest.getMethod(), is(GET.name()));
        verify(mListener).onSuccess(mCaptor.capture());
        verify(mListener, never()).onFailure(any(HyperwalletException.class));

        HyperwalletPageList<Receipt> receiptResponse = mCaptor.getValue();

        assertThat(receiptResponse.getDataList(), hasSize(2));

        assertThat(recordedRequest.getPath(),
                containsString(
                        "/rest/v3/users/usr-fbfd5848-60d0-43c5-8462-099c959b49c7/prepaid-cards/trm-2345/receipts?"));

        Receipt receipt = receiptResponse.getDataList().get(0);
        assertThat(receipt.getJournalId(), is("ABCDE_CC002212334"));
        assertThat(receipt.getType(), is("DEPOSIT"));
        assertThat(receipt.getCreatedOn(), is("2019-06-01T17:12:19"));
        assertThat(receipt.getEntry(), is(Receipt.Entries.CREDIT));
        assertThat(receipt.getDestinationToken(), is("trm-2345"));
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
        final String prepaidCardToken = "trm-2345";
        Hyperwallet.getDefault().listPrepaidCardReceipts(prepaidCardToken, receiptQueryParam, mListener);

        mAwait.await(150, TimeUnit.MILLISECONDS);

        RecordedRequest recordedRequest = mServer.getRequest();
        assertThat(recordedRequest.getMethod(), is(GET.name()));
        verify(mListener).onSuccess(mCaptor.capture());
        verify(mListener, never()).onFailure(any(HyperwalletException.class));

        HyperwalletPageList<Receipt> receiptResponse = mCaptor.getValue();

        assertThat(receiptResponse.getDataList(), hasSize(1));

        assertThat(recordedRequest.getPath(),
                containsString(
                        "/rest/v3/users/usr-fbfd5848-60d0-43c5-8462-099c959b49c7/prepaid-cards/trm-2345/receipts?"));

        Receipt receipt = receiptResponse.getDataList().get(0);
        assertThat(receipt.getEntry(), is(Receipt.Entries.DEBIT));
    }

    @Test
    public void testListPrepaidCardReceipts_returnsNoReceipts() throws InterruptedException {
        mServer.mockResponse().withHttpResponseCode(HTTP_NO_CONTENT).withBody("").mock();

        final ReceiptQueryParam.Builder builder = new ReceiptQueryParam.Builder();
        ReceiptQueryParam receiptQueryParam = builder.build();

        assertThat(receiptQueryParam, is(notNullValue()));
        final String prepaidCardToken = "trm-2345";
        Hyperwallet.getDefault().listPrepaidCardReceipts(prepaidCardToken, receiptQueryParam, mListener);

        mAwait.await(100, TimeUnit.MILLISECONDS);

        RecordedRequest recordedRequest = mServer.getRequest();
        assertThat(recordedRequest.getMethod(), is(GET.name()));
        assertThat(recordedRequest.getPath(),
                containsString(
                        "/rest/v3/users/usr-fbfd5848-60d0-43c5-8462-099c959b49c7/prepaid-cards/trm-2345/receipts?"));

        verify(mListener).onSuccess(mCaptor.capture());
        verify(mListener, never()).onFailure(any(HyperwalletException.class));

        HyperwalletPageList<Receipt> receiptsResponse = mCaptor.getValue();
        assertThat(receiptsResponse, is(nullValue()));
    }

    @Test
    public void testListPrepaidCardReceipts_returnsError() throws InterruptedException {
        String responseBody = mExternalResourceManager.getResourceContentError("system_error_response.json");
        mServer.mockResponse().withHttpResponseCode(HTTP_INTERNAL_ERROR).withBody(responseBody).mock();

        final ReceiptQueryParam.Builder builder = new ReceiptQueryParam.Builder();
        ReceiptQueryParam receiptQueryParam = builder.build();

        final String prepaidCardToken = "trm-2345";
        Hyperwallet.getDefault().listPrepaidCardReceipts(prepaidCardToken, receiptQueryParam, mListener);
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
                containsString(
                        "/rest/v3/users/usr-fbfd5848-60d0-43c5-8462-099c959b49c7/prepaid-cards/trm-2345/receipts?"));
    }
}
