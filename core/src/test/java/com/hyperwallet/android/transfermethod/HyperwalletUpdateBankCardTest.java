package com.hyperwallet.android.transfermethod;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import static com.hyperwallet.android.model.HyperwalletStatusTransition.CREATED_ON;
import static com.hyperwallet.android.model.HyperwalletStatusTransition.StatusDefinition.ACTIVATED;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.CARD_BRAND;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.CARD_NUMBER;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.CARD_TYPE;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.CVV;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.DATE_OF_EXPIRY;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.STATUS;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.TOKEN;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.TRANSFER_METHOD_COUNTRY;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.TRANSFER_METHOD_CURRENCY;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.TYPE;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodTypes.BANK_CARD;
import static com.hyperwallet.android.util.HttpMethod.PUT;

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
public class HyperwalletUpdateBankCardTest {

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
    public void testUpdateBankCard_withSuccess() throws InterruptedException {

        String responseBody = mExternalResourceManager.getResourceContent("bank_card_response.json");
        mServer.mockResponse().withHttpResponseCode(HttpURLConnection.HTTP_OK).withBody(responseBody).mock();

        HyperwalletBankCard bankCard = new HyperwalletBankCard
                .Builder("US", "USD", "************1114", "2019-11", "someCvv")
                .cardBrand("VISA")
                .cardType("DEBIT")
                .dateOfExpiry("2019-11")
                .token("trm-d8c65e1e-b3e5-460d-8b24-bee7cdae1636")
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

        RecordedRequest recordedRequest = mServer.getRequest();
        assertThat(recordedRequest.getPath(),
                is("/rest/v3/users/usr-fbfd5848-60d0-43c5-8462-099c959b49c7/bank-cards/trm-d8c65e1e-b3e5-460d-8b24"
                        + "-bee7cdae1636"));
        assertThat(recordedRequest.getMethod(), is(PUT.name()));

    }

    @Test
    public void testUpdateBankCard_withValidationError() throws InterruptedException {

        String responseBody = mExternalResourceManager.getResourceContentError("update_card_error_response.json");
        mServer.mockResponse().withHttpResponseCode(HttpURLConnection.HTTP_BAD_REQUEST).withBody(responseBody).mock();

        HyperwalletBankCard bankCard = new HyperwalletBankCard
                .Builder("US", "US", "************1114", "2019-11", "someCvv")
                .cardBrand("VISA")
                .cardType("DEBIT")
                .cvv("someCvv")
                .dateOfExpiry("2019-11")
                .token("trm-d8c65e1e-b3e5-460d-8b24-bee7cdae1636")
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
                is("/rest/v3/users/usr-fbfd5848-60d0-43c5-8462-099c959b49c7/bank-cards/trm-d8c65e1e-b3e5-460d-8b24"
                        + "-bee7cdae1636"));
        assertThat(recordedRequest.getMethod(), is(PUT.name()));

        verify(mListener, never()).onSuccess(any(HyperwalletBankCard.class));
        verify(mListener).onFailure(mExceptionCaptor.capture());

        HyperwalletException hyperwalletException = mExceptionCaptor.getValue();
        assertThat(hyperwalletException, is(notNullValue()));
        HyperwalletErrors hyperwalletErrors = hyperwalletException.getHyperwalletErrors();
        assertThat(hyperwalletErrors, is(notNullValue()));
        assertThat(hyperwalletErrors.getErrors(), is(notNullValue()));
        assertThat(hyperwalletErrors.getErrors().size(), is(1));

        HyperwalletError hyperwalletError = hyperwalletErrors.getErrors().get(0);
        assertThat(hyperwalletError.getCode(), is("VISADIRECT_UI_008"));
        assertThat(hyperwalletError.getFieldName(), is("cardNumber"));
        assertThat(hyperwalletError.getMessage(),
                is("The card cannot be registered - Please contact your issuer or the bank for further information."));

    }
}