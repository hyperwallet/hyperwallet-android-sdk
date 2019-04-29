package com.hyperwallet.android.transfermethod;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.CARD_BRAND;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.CARD_NUMBER;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.CARD_TYPE;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.CREATED_ON;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.DATE_OF_EXPIRY;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.STATUS;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.TOKEN;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.TRANSFER_METHOD_COUNTRY;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.TRANSFER_METHOD_CURRENCY;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.TYPE;

import com.hyperwallet.android.Hyperwallet;
import com.hyperwallet.android.exception.HyperwalletException;
import com.hyperwallet.android.exception.HyperwalletRestException;
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
public class HyperwalletCreateBankCardTest {

    @Rule
    public HyperwalletExternalResourceManager mExternalResourceManager = new HyperwalletExternalResourceManager();
    @Rule
    public HyperwalletMockWebServer mServer = new HyperwalletMockWebServer();
    @Rule
    public HyperwalletSdkMock mSdkMock = new HyperwalletSdkMock(mServer);
    @Rule
    public MockitoRule mMockito = MockitoJUnit.rule();
    @Mock
    private HyperwalletListener<HyperwalletBankCard> mListener;
    @Captor
    private ArgumentCaptor<HyperwalletBankCard> mListTransferMethodCaptor;
    @Captor
    private ArgumentCaptor<HyperwalletException> mExceptionCaptor;


    private CountDownLatch mAwait = new CountDownLatch(1);

    @Test
    public void testCreateBankCard_withSuccess() throws InterruptedException {

        final HyperwalletBankCard bankCard = new HyperwalletBankCard.Builder()
                .cardBrand("VISA")
                .cardNumber("4216701111111114")
                .cardType("DEBIT")
                .dateOfExpiry("2019-11")
                .token("trm-7e915660-8c97-47bf-8a4f-0c1bc890d46f")
                .transferMethodCountry("US")
                .transferMethodCurrency("USD")
                .build();

        String responseBody = mExternalResourceManager.getResourceContent("add_card_response.json");
        mServer.mockResponse().withHttpResponseCode(HttpURLConnection.HTTP_CREATED).withBody(responseBody).mock();


        Hyperwallet.getDefault().createBankCard(bankCard, mListener);
        mAwait.await(500, TimeUnit.MILLISECONDS);

        verify(mListener).onSuccess(mListTransferMethodCaptor.capture());
        verify(mListener, never()).onFailure(any(HyperwalletException.class));

        HyperwalletBankCard hyperwalletBankCardResponse =
                mListTransferMethodCaptor.getValue();
        RecordedRequest recordedRequest = mServer.getRequest();
        assertThat(recordedRequest.getPath(),
                is("/rest/v3/users/usr-fbfd5848-60d0-43c5-8462-099c959b49c7/bank-cards"));

        assertThat(hyperwalletBankCardResponse.getField(CARD_BRAND), is("VISA"));
        assertThat(hyperwalletBankCardResponse.getField(CARD_NUMBER), is(equalTo("************0114")));
        assertThat(hyperwalletBankCardResponse.getField(CARD_TYPE), is("DEBIT"));
        assertThat(hyperwalletBankCardResponse.getField(DATE_OF_EXPIRY), is("2019-11"));
        assertThat(hyperwalletBankCardResponse.getField(TOKEN), is("trm-7e915660-8c97-47bf-8a4f-0c1bc890d46f"));
        assertThat(hyperwalletBankCardResponse.getField(TRANSFER_METHOD_COUNTRY), is(equalTo("US")));
        assertThat(hyperwalletBankCardResponse.getField(TRANSFER_METHOD_CURRENCY), is(equalTo("USD")));
        assertThat(hyperwalletBankCardResponse.getField(TYPE), is("BANK_CARD"));
        assertThat(hyperwalletBankCardResponse.getField(CREATED_ON), is(equalTo("2019-01-08T00:56:15")));
        assertThat(hyperwalletBankCardResponse.getField(STATUS), is(equalTo("ACTIVATED")));
    }

    @Test
    public void testCreateBankCard_withValidationError() throws InterruptedException {

        final HyperwalletBankCard bankCard = new HyperwalletBankCard.Builder()
                .cardBrand("AMEX")
                .cardNumber("1216701111111114")
                .cardType("DEBIT")
                .dateOfExpiry("2018-11")
                .token("trm-7e915660-8c97-47bf-8a4f-0c1bc890d46f")
                .transferMethodCountry("US")
                .transferMethodCurrency("USD")
                .build();

        String responseBody = mExternalResourceManager.getResourceContentError("add_card_error_response.json");
        mServer.mockResponse().withHttpResponseCode(HttpURLConnection.HTTP_BAD_REQUEST).withBody(responseBody).mock();

        Hyperwallet.getDefault().createBankCard(bankCard, mListener);
        mAwait.await(500, TimeUnit.MILLISECONDS);

        verify(mListener, never()).onSuccess(any(HyperwalletBankCard.class));
        verify(mListener).onFailure(mExceptionCaptor.capture());
        RecordedRequest recordedRequest = mServer.getRequest();
        assertThat(recordedRequest.getPath(),
                is("/rest/v3/users/usr-fbfd5848-60d0-43c5-8462-099c959b49c7/bank-cards"));

        HyperwalletException hyperwalletException = mExceptionCaptor.getValue();
        assertThat(hyperwalletException, is(notNullValue()));
        assertThat(((HyperwalletRestException) hyperwalletException).getHttpCode(),
                is(HttpURLConnection.HTTP_BAD_REQUEST));
        HyperwalletErrors hyperwalletErrors = hyperwalletException.getHyperwalletErrors();
        assertThat(hyperwalletErrors, is(notNullValue()));
        assertThat(hyperwalletErrors.getErrors(), is(notNullValue()));
        assertThat(hyperwalletErrors.getErrors().size(), is(2));

        HyperwalletError hyperwalletError1 = hyperwalletErrors.getErrors().get(0);
        assertThat(hyperwalletError1.getCode(), is("CARD_NOT_SUPPORTED"));
        assertThat(hyperwalletError1.getMessage(), is("The card account supplied is not currently supported."));
        assertThat(hyperwalletError1.getFieldName(), is("cardNumber"));

        HyperwalletError hyperwalletError2 = hyperwalletErrors.getErrors().get(1);
        assertThat(hyperwalletError2.getCode(), is("CARD_EXPIRATION_DATE"));
        assertThat(hyperwalletError2.getMessage(), is("Expiration date"));
        assertThat(hyperwalletError2.getFieldName(), is("dateOfExpiry"));


    }
}

