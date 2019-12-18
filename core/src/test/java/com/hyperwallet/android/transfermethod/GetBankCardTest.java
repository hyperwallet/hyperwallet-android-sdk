package com.hyperwallet.android.transfermethod;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import static com.hyperwallet.android.model.StatusTransition.StatusDefinition.ACTIVATED;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.CARD_BRAND;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.CARD_NUMBER;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.CARD_TYPE;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.CREATED_ON;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.CVV;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.DATE_OF_EXPIRY;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.STATUS;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.TOKEN;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.TRANSFER_METHOD_COUNTRY;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.TRANSFER_METHOD_CURRENCY;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.TYPE;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodTypes.BANK_CARD;
import static com.hyperwallet.android.util.HttpMethod.GET;

import com.hyperwallet.android.Hyperwallet;
import com.hyperwallet.android.exception.HyperwalletException;
import com.hyperwallet.android.listener.HyperwalletListener;
import com.hyperwallet.android.model.Error;
import com.hyperwallet.android.model.Errors;
import com.hyperwallet.android.model.transfermethod.BankCard;
import com.hyperwallet.android.rule.ExternalResourceManager;
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
public class GetBankCardTest {

    @Rule
    public HyperwalletMockWebServer mServer = new HyperwalletMockWebServer();
    @Rule
    public HyperwalletSdkMock mHyperwalletSdkMock = new HyperwalletSdkMock(mServer);
    @Rule
    public ExternalResourceManager mExternalResourceManager = new ExternalResourceManager();
    @Rule
    public MockitoRule mMockito = MockitoJUnit.rule();

    @Mock
    private HyperwalletListener<BankCard> mListener;
    @Captor
    private ArgumentCaptor<BankCard> mBankCardArgumentCaptor;
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

        BankCard bankCardResponse = mBankCardArgumentCaptor.getValue();
        assertThat(bankCardResponse, is(notNullValue()));
        assertThat(bankCardResponse.getField(TYPE), is(BANK_CARD));

        assertThat(bankCardResponse.getField(CARD_BRAND), is("VISA"));
        assertThat(bankCardResponse.getField(CARD_NUMBER), is("************1114"));
        assertThat(bankCardResponse.getField(CARD_TYPE), is("DEBIT"));
        assertThat(bankCardResponse.getField(CVV), is(nullValue()));
        assertThat(bankCardResponse.getField(CREATED_ON), is("2019-01-08T00:56:15"));
        assertThat(bankCardResponse.getField(DATE_OF_EXPIRY), is("2019-11"));
        assertThat(bankCardResponse.getField(TOKEN), is("trm-d8c65e1e-b3e5-460d-8b24-bee7cdae1636"));
        assertThat(bankCardResponse.getField(TRANSFER_METHOD_COUNTRY), is("US"));
        assertThat(bankCardResponse.getField(TRANSFER_METHOD_CURRENCY), is("USD"));
        assertThat(bankCardResponse.getField(STATUS), is(ACTIVATED));
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

        BankCard bankCard = mBankCardArgumentCaptor.getValue();
        assertThat(bankCard, is(nullValue()));
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

        verify(mListener, never()).onSuccess(any(BankCard.class));
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
