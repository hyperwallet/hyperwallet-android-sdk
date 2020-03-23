package com.hyperwallet.android.transfermethod;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
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
import static com.hyperwallet.android.util.HttpMethod.PUT;

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
public class UpdateBankCardTest {

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
    public void testUpdateBankCard_withSuccess() throws InterruptedException {

        String responseBody = mExternalResourceManager.getResourceContent("bank_card_response.json");
        mServer.mockResponse().withHttpResponseCode(HttpURLConnection.HTTP_OK).withBody(responseBody).mock();

        BankCard bankCard = new BankCard
                .Builder("US", "USD", "************1114", "2019-11", "someCvv")
                .cardBrand("VISA")
                .cardType("DEBIT")
                .dateOfExpiry("2019-11")
                .token("trm-fake-token")
                .build();
        assertThat(bankCard.getField(CARD_BRAND), is("VISA"));
        assertThat(bankCard.getField(CARD_NUMBER), is("************1114"));
        assertThat(bankCard.getField(CARD_TYPE), is("DEBIT"));
        assertThat(bankCard.getField(CVV), is("someCvv"));
        assertThat(bankCard.getField(DATE_OF_EXPIRY), is("2019-11"));
        assertThat(bankCard.getField(TRANSFER_METHOD_COUNTRY), is("US"));
        assertThat(bankCard.getField(TRANSFER_METHOD_CURRENCY), is("USD"));

        Hyperwallet.getDefault().updateBankCard(bankCard, mListener);
        mAwait.await(500, TimeUnit.MILLISECONDS);

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
        assertThat(bankCardResponse.getField(TOKEN), is("trm-fake-token"));
        assertThat(bankCardResponse.getField(TRANSFER_METHOD_COUNTRY), is("US"));
        assertThat(bankCardResponse.getField(TRANSFER_METHOD_CURRENCY), is("USD"));
        assertThat(bankCardResponse.getField(STATUS), is(ACTIVATED));

        RecordedRequest recordedRequest = mServer.getRequest();
        assertThat(recordedRequest.getPath(),
                is("/rest/v3/users/test-user-token/bank-cards/trm-fake-token"));
        assertThat(recordedRequest.getMethod(), is(PUT.name()));

    }

    @Test
    public void testUpdateBankCard_withValidationError() throws InterruptedException {

        String responseBody = mExternalResourceManager.getResourceContentError("update_card_error_response.json");
        mServer.mockResponse().withHttpResponseCode(HttpURLConnection.HTTP_BAD_REQUEST).withBody(responseBody).mock();

        BankCard bankCard = new BankCard
                .Builder("US", "US", "************1114", "2019-11", "someCvv")
                .cardBrand("VISA")
                .cardType("DEBIT")
                .cvv("someCvv")
                .dateOfExpiry("2019-11")
                .token("trm-fake-token")
                .build();

        assertThat(bankCard.getField(CARD_BRAND), is("VISA"));
        assertThat(bankCard.getField(CARD_NUMBER), is("************1114"));
        assertThat(bankCard.getField(CARD_TYPE), is("DEBIT"));
        assertThat(bankCard.getField(CVV), is("someCvv"));
        assertThat(bankCard.getField(DATE_OF_EXPIRY), is("2019-11"));
        assertThat(bankCard.getField(TRANSFER_METHOD_COUNTRY), is("US"));
        assertThat(bankCard.getField(TRANSFER_METHOD_CURRENCY), is("US"));

        Hyperwallet.getDefault().updateBankCard(bankCard, mListener);
        mAwait.await(500, TimeUnit.MILLISECONDS);

        RecordedRequest recordedRequest = mServer.getRequest();
        assertThat(recordedRequest.getPath(),
                is("/rest/v3/users/test-user-token/bank-cards/trm-fake-token"));
        assertThat(recordedRequest.getMethod(), is(PUT.name()));

        verify(mListener, never()).onSuccess(any(BankCard.class));
        verify(mListener).onFailure(mExceptionCaptor.capture());

        HyperwalletException hyperwalletException = mExceptionCaptor.getValue();
        assertThat(hyperwalletException, is(notNullValue()));
        Errors errors = hyperwalletException.getErrors();
        assertThat(errors, is(notNullValue()));
        assertThat(errors.getErrors(), is(notNullValue()));
        assertThat(errors.getErrors().size(), is(1));

        Error error = errors.getErrors().get(0);
        assertThat(error.getCode(), is("VISADIRECT_UI_008"));
        assertThat(error.getFieldName(), is("cardNumber"));
        assertThat(error.getMessage(),
                is("The card cannot be registered - Please contact your issuer or the bank for further information."));

    }
}