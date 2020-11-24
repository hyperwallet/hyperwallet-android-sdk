package com.hyperwallet.android.transfermethod;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import static com.hyperwallet.android.util.HttpMethod.GET;

import com.hyperwallet.android.Hyperwallet;
import com.hyperwallet.android.exception.HyperwalletException;
import com.hyperwallet.android.listener.HyperwalletListener;
import com.hyperwallet.android.model.Error;
import com.hyperwallet.android.model.Errors;
import com.hyperwallet.android.model.transfermethod.PrepaidCard;
import com.hyperwallet.android.model.transfermethod.TransferMethod;
import com.hyperwallet.android.rule.ExternalResourceManager;
import com.hyperwallet.android.rule.HyperwalletMockWebServer;
import com.hyperwallet.android.rule.HyperwalletSdkMock;
import com.hyperwallet.android.util.DateUtil;

import org.hamcrest.Matchers;
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
public class GetPrepaidCardTest {

    @Rule
    public HyperwalletMockWebServer mServer = new HyperwalletMockWebServer();
    @Rule
    public HyperwalletSdkMock mHyperwalletSdkMock = new HyperwalletSdkMock(mServer);
    @Rule
    public ExternalResourceManager mExternalResourceManager = new ExternalResourceManager();
    @Rule
    public MockitoRule mMockito = MockitoJUnit.rule();

    @Mock
    private HyperwalletListener<PrepaidCard> mListener;
    @Captor
    private ArgumentCaptor<PrepaidCard> mPrepaidCardArgumentCaptor;
    @Captor
    private ArgumentCaptor<HyperwalletException> mExceptionCaptor;

    private CountDownLatch mAwait = new CountDownLatch(1);

    @Test
    public void testGetPrepaidCard_returnsCard() throws InterruptedException {

        String responseBody = mExternalResourceManager.getResourceContent("prepaid_card_response.json");
        mServer.mockResponse().withHttpResponseCode(HttpURLConnection.HTTP_OK).withBody(responseBody).mock();

        Hyperwallet.getDefault().getPrepaidCard("trm-fake-token", mListener);
        mAwait.await(500, TimeUnit.MILLISECONDS);

        RecordedRequest recordedRequest = mServer.getRequest();
        assertThat(recordedRequest.getPath(),
                is("/rest/v3/users/test-user-token/prepaid-cards/trm-fake-token"));
        assertThat(recordedRequest.getMethod(), is(GET.name()));

        verify(mListener).onSuccess(mPrepaidCardArgumentCaptor.capture());
        verify(mListener, never()).onFailure(any(HyperwalletException.class));

        PrepaidCard prepaidCardResponse = mPrepaidCardArgumentCaptor.getValue();

        assertThat(prepaidCardResponse, is(Matchers.notNullValue()));
        assertThat(prepaidCardResponse.getField(TransferMethod.TransferMethodFields.TOKEN),
                is("trm-fake-token"));
        assertThat(prepaidCardResponse.getType(), is("PREPAID_CARD"));
        assertThat(prepaidCardResponse.getStatus(), is("ACTIVATED"));
        assertThat(DateUtil.toDateTimeFormat(prepaidCardResponse.getCreatedOn()), is("2019-06-20T22:49:12"));
        assertThat(prepaidCardResponse.getTransferMethodCountry(), is("US"));
        assertThat(prepaidCardResponse.getTransferMethodCurrency(), is("USD"));
        assertThat(prepaidCardResponse.getCardType(), is("VIRTUAL"));
        assertThat(prepaidCardResponse.getCardPackage(), is("L1"));
        assertThat(prepaidCardResponse.getCardNumber(), is("************8766"));
        assertThat(prepaidCardResponse.getCardBrand(), is("VISA"));
        assertThat(prepaidCardResponse.getDateOfExpiry(), is("2023-06"));
    }

    @Test
    public void testGetPrepaidCard_returnsNoCard() throws InterruptedException {

        String responseBody = "";
        mServer.mockResponse().withHttpResponseCode(HttpURLConnection.HTTP_NO_CONTENT).withBody(responseBody).mock();

        Hyperwallet.getDefault().getPrepaidCard("trm-fake-token", mListener);
        mAwait.await(500, TimeUnit.MILLISECONDS);

        RecordedRequest recordedRequest = mServer.getRequest();
        assertThat(recordedRequest.getPath(),
                is("/rest/v3/users/test-user-token/prepaid-cards/trm-fake-token"));
        assertThat(recordedRequest.getMethod(), is(GET.name()));

        verify(mListener).onSuccess(mPrepaidCardArgumentCaptor.capture());
        verify(mListener, never()).onFailure(any(HyperwalletException.class));

        PrepaidCard prepaidCard = mPrepaidCardArgumentCaptor.getValue();
        assertThat(prepaidCard, is(nullValue()));
    }

    @Test
    public void testGetPrepaidCard_returnsError() throws InterruptedException {

        String responseBody = mExternalResourceManager.getResourceContentError("system_error_response.json");
        mServer.mockResponse().withHttpResponseCode(HttpURLConnection.HTTP_BAD_REQUEST).withBody(responseBody).mock();

        Hyperwallet.getDefault().getPrepaidCard("trm-fake-token", mListener);
        mAwait.await(500, TimeUnit.MILLISECONDS);

        RecordedRequest recordedRequest = mServer.getRequest();
        assertThat(recordedRequest.getPath(),
                is("/rest/v3/users/test-user-token/prepaid-cards/trm-fake-token"));
        assertThat(recordedRequest.getMethod(), is(GET.name()));

        verify(mListener, never()).onSuccess(any(PrepaidCard.class));
        verify(mListener).onFailure(mExceptionCaptor.capture());

        HyperwalletException hyperwalletException = mExceptionCaptor.getValue();
        assertThat(hyperwalletException, is(notNullValue()));
        Errors errors = hyperwalletException.getErrors();
        assertThat(errors, is(notNullValue()));
        assertThat(errors.getErrors(), is(notNullValue()));
        assertThat(errors.getErrors().size(), is(1));

        Error error = errors.getErrors().get(0);
        assertThat(error.getCode(), is("SYSTEM_ERROR"));
        assertThat(error.getMessage(),
                is("A system error has occurred. Please try again. If you continue to receive this error, please "
                        + "contact customer support for assistance (Ref ID: 99b4ad5c-4aac-4cc2-aa9b-4b4f4844ac9b)."));

    }
}
