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

import static com.hyperwallet.android.model.StatusTransition.StatusDefinition.ACTIVATED;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.TOKEN;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodTypes.PREPAID_CARD;
import static com.hyperwallet.android.util.HttpMethod.GET;

import com.hyperwallet.android.Hyperwallet;
import com.hyperwallet.android.exception.HyperwalletException;
import com.hyperwallet.android.exception.HyperwalletRestException;
import com.hyperwallet.android.listener.HyperwalletListener;
import com.hyperwallet.android.model.Error;
import com.hyperwallet.android.model.Errors;
import com.hyperwallet.android.model.paging.PageList;
import com.hyperwallet.android.model.transfermethod.PrepaidCard;
import com.hyperwallet.android.model.transfermethod.PrepaidCardQueryParam;
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

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import okhttp3.mockwebserver.RecordedRequest;

@RunWith(RobolectricTestRunner.class)
public class ListPrepaidCardsTest {
    @Rule
    public ExternalResourceManager mExternalResourceManager = new ExternalResourceManager();
    @Rule
    public HyperwalletMockWebServer mServer = new HyperwalletMockWebServer();
    @Rule
    public HyperwalletSdkMock mHyperwalletSdkMock = new HyperwalletSdkMock(mServer);
    @Rule
    public MockitoRule mMockito = MockitoJUnit.rule();
    @Mock
    private HyperwalletListener<PageList<PrepaidCard>> mListener;
    @Captor
    private ArgumentCaptor<PageList<PrepaidCard>> mListTransferMethodCaptor;
    @Captor
    private ArgumentCaptor<HyperwalletException> mExceptionCaptor;

    private CountDownLatch mAwait = new CountDownLatch(1);

    @Test
    public void testListPrepaidCards_returnsActivatedCards() throws InterruptedException {

        String responseBody = mExternalResourceManager.getResourceContent("prepaid_cards_response.json");
        mServer.mockResponse().withHttpResponseCode(HTTP_OK).withBody(responseBody).mock();

        PrepaidCardQueryParam queryParam = new PrepaidCardQueryParam.Builder()
                .status(ACTIVATED)
                .build();

        assertThat(queryParam, is(notNullValue()));
        assertThat(queryParam.getType(), is(PREPAID_CARD));
        Hyperwallet.getDefault().listPrepaidCards(queryParam, mListener);

        mAwait.await(500, TimeUnit.MILLISECONDS);

        RecordedRequest recordedRequest = mServer.getRequest();
        verify(mListener).onSuccess(mListTransferMethodCaptor.capture());
        verify(mListener, never()).onFailure(any(HyperwalletException.class));
        assertThat(recordedRequest.getMethod(), is(GET.name()));

        PageList<PrepaidCard> prepaidCardsResponse = mListTransferMethodCaptor.getValue();

        assertThat(prepaidCardsResponse.getCount(), is(2));
        assertThat(prepaidCardsResponse.getDataList(), hasSize(2));
        assertThat(prepaidCardsResponse.getOffset(), is(0));
        assertThat(prepaidCardsResponse.getLimit(), is(10));

        assertThat(recordedRequest.getPath(),
                is("/rest/v3/users/test-user-token/prepaid-cards?limit=10&offset=0&type"
                        + "=PREPAID_CARD&status=ACTIVATED"));

        PrepaidCard prepaidCard = prepaidCardsResponse.getDataList().get(0);
        assertThat(prepaidCard.getField(TOKEN), is("trm-fake-token"));
        assertThat(prepaidCard.getType(), is(PREPAID_CARD));
        assertThat(prepaidCard.getStatus(), is(ACTIVATED));
        assertThat(DateUtil.toDateTimeFormat(prepaidCard.getCreatedOn()), is("2019-06-20T22:49:12"));
        assertThat(prepaidCard.getTransferMethodCountry(), is("US"));
        assertThat(prepaidCard.getTransferMethodCurrency(), is("USD"));
        assertThat(prepaidCard.getCardType(), is("VIRTUAL"));
        assertThat(prepaidCard.getCardPackage(), is("L1"));
        assertThat(prepaidCard.getCardNumber(), is("************8766"));
        assertThat(prepaidCard.getCardBrand(), is("VISA"));
        assertThat(prepaidCard.getDateOfExpiry(), is("2023-06"));
        assertThat(prepaidCard.getField("verificationStatus"), is("VERIFIED"));
    }

    @Test
    public void testListPrepaidCards_returnsNoCards() throws InterruptedException {
        String responseBody = mExternalResourceManager.getResourceContent("prepaid_no_cards_response.json");
        mServer.mockResponse().withHttpResponseCode(HTTP_NO_CONTENT).withBody(responseBody).mock();

        PrepaidCardQueryParam queryParam = new PrepaidCardQueryParam.Builder()
                .status(ACTIVATED)
                .build();

        assertThat(queryParam, is(notNullValue()));
        Hyperwallet.getDefault().listPrepaidCards(queryParam, mListener);

        mAwait.await(500, TimeUnit.MILLISECONDS);

        RecordedRequest recordedRequest = mServer.getRequest();
        assertThat(recordedRequest.getPath(),
                containsString("/rest/v3/users/test-user-token/prepaid-cards?"));
        assertThat(recordedRequest.getMethod(), is(GET.name()));
        assertThat(recordedRequest.getPath(), containsString("type=PREPAID_CARD"));
        assertThat(recordedRequest.getPath(), containsString("limit=10"));
        assertThat(recordedRequest.getPath(), containsString("offset=0"));
        assertThat(recordedRequest.getPath(), containsString("status=ACTIVATED"));

        verify(mListener).onSuccess(mListTransferMethodCaptor.capture());
        verify(mListener, never()).onFailure(any(HyperwalletException.class));

        PageList<PrepaidCard> prepaidCardsResponse = mListTransferMethodCaptor.getValue();
        assertThat(prepaidCardsResponse, is(nullValue()));
    }

    @Test
    public void testListPrepaidCards_returnsError() throws InterruptedException {
        String responseBody = mExternalResourceManager.getResourceContentError("system_error_response.json");
        mServer.mockResponse().withHttpResponseCode(HTTP_INTERNAL_ERROR).withBody(responseBody).mock();

        PrepaidCardQueryParam actualPrepaidCard = new PrepaidCardQueryParam.Builder()
                .status(ACTIVATED)
                .build();

        Hyperwallet.getDefault().listPrepaidCards(actualPrepaidCard, mListener);
        mAwait.await(500, TimeUnit.MILLISECONDS);

        verify(mListener, never()).onSuccess(any(PageList.class));
        verify(mListener).onFailure(mExceptionCaptor.capture());

        HyperwalletException hyperwalletException = mExceptionCaptor.getValue();
        assertThat(hyperwalletException, is(notNullValue()));
        assertThat(((HyperwalletRestException) hyperwalletException).getHttpCode(),
                is(HTTP_INTERNAL_ERROR));

        Errors errors = hyperwalletException.getErrors();
        assertThat(errors, is(notNullValue()));
        assertThat(errors.getErrors(), is(notNullValue()));
        assertThat(errors.getErrors().size(), is(1));

        Error error = errors.getErrors().get(0);
        assertThat(error.getCode(), is("SYSTEM_ERROR"));
        assertThat(error.getMessage(),
                is("A system error has occurred. Please try again. If you continue to receive this error, please "
                        + "contact customer support for assistance (Ref ID: 99b4ad5c-4aac-4cc2-aa9b-4b4f4844ac9b)."));
        assertThat(error.getFieldName(), is(nullValue()));

        RecordedRequest recordedRequest = mServer.getRequest();
        assertThat(recordedRequest.getPath(),
                containsString("/rest/v3/users/test-user-token/prepaid-cards?"));
        assertThat(recordedRequest.getMethod(), is(GET.name()));
        assertThat(recordedRequest.getPath(), containsString("type=PREPAID_CARD"));
        assertThat(recordedRequest.getPath(), containsString("limit=10"));
        assertThat(recordedRequest.getPath(), containsString("offset=0"));
        assertThat(recordedRequest.getPath(), containsString("status=ACTIVATED"));
    }
}