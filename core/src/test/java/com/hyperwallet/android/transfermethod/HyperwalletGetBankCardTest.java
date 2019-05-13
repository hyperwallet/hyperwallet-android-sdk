package com.hyperwallet.android.transfermethod;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import static com.hyperwallet.android.model.HyperwalletStatusTransition.StatusDefinition.ACTIVATED;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.CARD_BRAND;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.CARD_NUMBER;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.CARD_TYPE;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.CREATED_ON;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.CVV;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.DATE_OF_EXPIRY;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.STATUS;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.TOKEN;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.TRANSFER_METHOD_COUNTRY;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.TRANSFER_METHOD_CURRENCY;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.TYPE;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodTypes.BANK_CARD;
import static com.hyperwallet.android.util.HttpMethod.GET;

import com.hyperwallet.android.Hyperwallet;
import com.hyperwallet.android.exception.HyperwalletException;
import com.hyperwallet.android.listener.HyperwalletListener;
import com.hyperwallet.android.model.HyperwalletBankCard;
import com.hyperwallet.android.model.HyperwalletError;
import com.hyperwallet.android.model.HyperwalletErrors;
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
public class HyperwalletGetBankCardTest {

    @Rule
    public HyperwalletMockWebServer mServer = new HyperwalletMockWebServer();
    @Rule
    public HyperwalletSdkMock mSdkMock = new HyperwalletSdkMock(mServer);
    @Rule
    public HyperwalletExternalResourceManager mExternalResourceManager = new HyperwalletExternalResourceManager();
    @Rule
    public MockitoRule mMockito = MockitoJUnit.rule();

    @Mock
    private HyperwalletListener<HyperwalletBankCard> mListener;
    @Captor
    private ArgumentCaptor<HyperwalletBankCard> mBankCardArgumentCaptor;
    @Captor
    private ArgumentCaptor<HyperwalletException> mExceptionCaptor;

    private CountDownLatch mAwait = new CountDownLatch(1);

    @Test
    public void testGetBankCard_returnsCard() throws InterruptedException {

        String responseBody = mExternalResourceManager.getResourceContent("bank_card_response.json");
        mServer.mockResponse().withHttpResponseCode(HttpURLConnection.HTTP_OK).withBody(responseBody).mock();

        Hyperwallet.getDefault().getBankCard("trm-d8c65e1e-b3e5-460d-8b24-bee7cdae1636", mListener);
        mAwait.await(500, TimeUnit.MILLISECONDS);

        RecordedRequest recordedRequest = mServer.getRequest();
        assertThat(recordedRequest.getPath(),
                is("/rest/v3/users/usr-fbfd5848-60d0-43c5-8462-099c959b49c7/bank-cards/trm-d8c65e1e-b3e5-460d-8b24"
                        + "-bee7cdae1636"));
        assertThat(recordedRequest.getMethod(), is(GET.name()));

        verify(mListener).onSuccess(mBankCardArgumentCaptor.capture());
        verify(mListener, never()).onFailure(any(HyperwalletException.class));

        HyperwalletBankCard hyperwalletBankCardResponse = mBankCardArgumentCaptor.getValue();
        assertThat(hyperwalletBankCardResponse, is(notNullValue()));
        assertThat(hyperwalletBankCardResponse.getField(TYPE), is(BANK_CARD));

        assertThat(hyperwalletBankCardResponse.getField(CARD_BRAND), is("VISA"));
        assertThat(hyperwalletBankCardResponse.getField(CARD_NUMBER), is("************1114"));
        assertThat(hyperwalletBankCardResponse.getField(CARD_TYPE), is("DEBIT"));
        assertThat(hyperwalletBankCardResponse.getField(CVV), is(nullValue()));
        assertThat(hyperwalletBankCardResponse.getField(CREATED_ON), is("2019-01-08T00:56:15"));
        assertThat(hyperwalletBankCardResponse.getField(DATE_OF_EXPIRY), is("2019-11"));
        assertThat(hyperwalletBankCardResponse.getField(TOKEN), is("trm-d8c65e1e-b3e5-460d-8b24-bee7cdae1636"));
        assertThat(hyperwalletBankCardResponse.getField(TRANSFER_METHOD_COUNTRY), is("US"));
        assertThat(hyperwalletBankCardResponse.getField(TRANSFER_METHOD_CURRENCY), is("USD"));
        assertThat(hyperwalletBankCardResponse.getField(STATUS), is(ACTIVATED));
    }

    @Test
    public void testGetBankCard_returnsNoCard() throws InterruptedException {

        String responseBody = "";
        mServer.mockResponse().withHttpResponseCode(HttpURLConnection.HTTP_NO_CONTENT).withBody(responseBody).mock();

        Hyperwallet.getDefault().getBankCard("trm-d8c65e1e-b3e5-460d-8b24-bee7cdae1636", mListener);
        mAwait.await(500, TimeUnit.MILLISECONDS);

        RecordedRequest recordedRequest = mServer.getRequest();
        assertThat(recordedRequest.getPath(),
                is("/rest/v3/users/usr-fbfd5848-60d0-43c5-8462-099c959b49c7/bank-cards/trm-d8c65e1e-b3e5-460d-8b24"
                        + "-bee7cdae1636"));
        assertThat(recordedRequest.getMethod(), is(GET.name()));

        verify(mListener).onSuccess(mBankCardArgumentCaptor.capture());
        verify(mListener, never()).onFailure(any(HyperwalletException.class));

        HyperwalletBankCard hyperwalletBankCard = mBankCardArgumentCaptor.getValue();
        assertThat(hyperwalletBankCard, is(nullValue()));
    }

    @Test
    public void testListBankAccounts_returnsError() throws InterruptedException {

        String responseBody = mExternalResourceManager.getResourceContentError("system_error_response.json");
        mServer.mockResponse().withHttpResponseCode(HttpURLConnection.HTTP_BAD_REQUEST).withBody(responseBody).mock();

        Hyperwallet.getDefault().getBankCard("trm-d8c65e1e-b3e5-460d-8b24-bee7cdae1636", mListener);
        mAwait.await(500, TimeUnit.MILLISECONDS);

        RecordedRequest recordedRequest = mServer.getRequest();
        assertThat(recordedRequest.getPath(),
                is("/rest/v3/users/usr-fbfd5848-60d0-43c5-8462-099c959b49c7/bank-cards/trm-d8c65e1e-b3e5-460d-8b24"
                        + "-bee7cdae1636"));
        assertThat(recordedRequest.getMethod(), is(GET.name()));

        verify(mListener, never()).onSuccess(any(HyperwalletBankCard.class));
        verify(mListener).onFailure(mExceptionCaptor.capture());

        HyperwalletException hyperwalletException = mExceptionCaptor.getValue();
        assertThat(hyperwalletException, is(notNullValue()));
        HyperwalletErrors hyperwalletErrors = hyperwalletException.getHyperwalletErrors();
        assertThat(hyperwalletErrors, is(notNullValue()));
        assertThat(hyperwalletErrors.getErrors(), is(notNullValue()));
        assertThat(hyperwalletErrors.getErrors().size(), is(1));

        HyperwalletError hyperwalletError = hyperwalletErrors.getErrors().get(0);
        assertThat(hyperwalletError.getCode(), is("SYSTEM_ERROR"));
        assertThat(hyperwalletError.getMessage(),
                is("A system error has occurred. Please try again. If you continue to receive this error, please "
                        + "contact customer support for assistance (Ref ID: 99b4ad5c-4aac-4cc2-aa9b-4b4f4844ac9b)."));

    }
}
