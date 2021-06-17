package com.hyperwallet.android.transfermethod;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodTypes.BANK_ACCOUNT;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodTypes.BANK_CARD;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodTypes.PAPER_CHECK;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodTypes.PAYPAL_ACCOUNT;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodTypes.VENMO_ACCOUNT;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodTypes.WIRE_ACCOUNT;
import static com.hyperwallet.android.util.HttpMethod.POST;

import com.hyperwallet.android.Hyperwallet;
import com.hyperwallet.android.exception.HyperwalletException;
import com.hyperwallet.android.listener.HyperwalletListener;
import com.hyperwallet.android.model.Error;
import com.hyperwallet.android.model.Errors;
import com.hyperwallet.android.model.graphql.Fee;
import com.hyperwallet.android.model.graphql.GqlResponse;
import com.hyperwallet.android.model.graphql.HyperwalletTransferMethodConfigurationKey;
import com.hyperwallet.android.model.graphql.ProcessingTime;
import com.hyperwallet.android.model.graphql.TransferMethodConfigurationKey;
import com.hyperwallet.android.model.graphql.error.GqlError;
import com.hyperwallet.android.model.graphql.error.GqlErrors;
import com.hyperwallet.android.model.graphql.keyed.Country;
import com.hyperwallet.android.model.graphql.keyed.Currency;
import com.hyperwallet.android.model.graphql.keyed.TransferMethodConfigurationKeyResult;
import com.hyperwallet.android.model.graphql.keyed.TransferMethodType;
import com.hyperwallet.android.model.graphql.query.TransferMethodTypesFeeAndProcessingTimesQuery;
import com.hyperwallet.android.model.graphql.query.TransferMethodConfigurationKeysQuery;
import com.hyperwallet.android.rule.ExternalResourceManager;
import com.hyperwallet.android.rule.HyperwalletMockWebServer;
import com.hyperwallet.android.rule.HyperwalletSdkMock;

import org.hamcrest.CoreMatchers;
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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import okhttp3.mockwebserver.RecordedRequest;

@RunWith(RobolectricTestRunner.class)
public class RetrieveTransferMethodTypesFeeAndProcessingTimeConfigurationKeysTest {

    @Rule
    public HyperwalletMockWebServer mServer = new HyperwalletMockWebServer();
    @Rule
    public HyperwalletSdkMock mHyperwalletSdkMock = new HyperwalletSdkMock(mServer);
    @Rule
    public ExternalResourceManager mExternalResourceManager = new ExternalResourceManager();
    @Rule
    public MockitoRule mMockito = MockitoJUnit.rule();
    @Mock
    private HyperwalletListener<HyperwalletTransferMethodConfigurationKey> mListener;
    @Captor
    private ArgumentCaptor<HyperwalletTransferMethodConfigurationKey> mKeyResultCaptor;
    @Captor
    private ArgumentCaptor<HyperwalletException> mExceptionCaptor;

    private CountDownLatch mAwait = new CountDownLatch(1);

    @Test
    public void testRetrieveTransferMethodConfigurationKeys_returnsFields() throws InterruptedException {
        String responseBody = mExternalResourceManager.getResourceContent("tmc_get_keys_fee_processing_time_response.json");
        mServer.mockResponse().withHttpResponseCode(HttpURLConnection.HTTP_OK).withBody(responseBody).mock();

        Hyperwallet.getDefault().retrieveTransferMethodTypesFeesAndProcessingTimes(
                new TransferMethodTypesFeeAndProcessingTimesQuery("US","USD"), mListener);
        mAwait.await(100, TimeUnit.MILLISECONDS);

        RecordedRequest recordedRequest = mServer.getRequest();
        assertThat(recordedRequest.getMethod(), is(POST.name()));
        assertThat(recordedRequest.getPath(), is("/graphql/"));

        verify(mListener).onSuccess(mKeyResultCaptor.capture());
        verify(mListener, never()).onFailure(any(HyperwalletException.class));

        HyperwalletTransferMethodConfigurationKey keyResultCaptorValue = mKeyResultCaptor.getValue();

        assertThat(keyResultCaptorValue, is(notNullValue()));

        assertThat(keyResultCaptorValue.getCountries(), is(not(empty())));
        assertThat(keyResultCaptorValue.getCountries(), hasSize(1));

        List<Country> countryList = new ArrayList<>(keyResultCaptorValue.getCountries());

        // US transfer method data
        Country countryUS = countryList.get(0);
        assertThat(countryUS.getCode(), is("US"));
        assertThat(countryUS.getName(), is("United States"));
        assertThat(countryUS.getCurrencies(), Matchers.<Currency>hasSize(1));

        // assert chaining US
        TransferMethodType transferMethodType = keyResultCaptorValue.getCountry("US")
                .getCurrency("USD")
                .getTransferMethodType(BANK_ACCOUNT);
        assertThat(transferMethodType, is(notNullValue()));
        assertThat(transferMethodType.getCode(), is(BANK_ACCOUNT));
        assertThat(transferMethodType.getName(), is("Bank Account"));

        assertThat(countryUS.getCurrency("USD").getTransferMethodType(BANK_ACCOUNT), is(notNullValue()));
        assertThat(countryUS.getCurrency("USD").getTransferMethodType(BANK_CARD), is(notNullValue()));
        assertThat(countryUS.getCurrency("USD").getTransferMethodType(PAYPAL_ACCOUNT), is(notNullValue()));

        // assert elements are filled and in order
        List<Currency> currencies = new ArrayList<>(keyResultCaptorValue.getCurrencies(countryUS.getCode()));
        assertThat(currencies, Matchers.<Currency>hasSize(1));
        assertThat(currencies.get(0).getCode(), is("USD"));
        assertThat(currencies.get(0).getName(), is("United States Dollar"));
        assertThat(countryUS.getCurrencies(), hasItem(currencies.get(0)));
        assertThat(keyResultCaptorValue.getCurrencies("PH"), is(nullValue()));

        List<TransferMethodType> transferMethodTypes = new ArrayList<>(
                keyResultCaptorValue.getTransferMethodType(countryUS.getCode(), currencies.get(0).getCode()));
        assertThat(transferMethodTypes, Matchers.<TransferMethodType>hasSize(6));
        assertThat(transferMethodTypes.get(0).getCode(), is(BANK_ACCOUNT));
        assertThat(transferMethodTypes.get(0).getName(), is("Bank Account"));
        assertThat(keyResultCaptorValue.getTransferMethodType("PH", "PHP"), is(nullValue()));
        assertThat(keyResultCaptorValue.getTransferMethodType("PH", "PHP"), is(nullValue()));

        List<Fee> fees = new ArrayList<>(transferMethodTypes.get(0).getFees());
        assertThat(fees, Matchers.<Fee>hasSize(1));
        assertThat(fees.get(0).getFeeRateType(), is("FLAT"));
        assertThat(fees.get(0).getValue(), is("2.00"));

        ProcessingTime processingTime = transferMethodTypes.get(0).getProcessingTime();
        assertThat(processingTime.getValue(), is("1 - 2 Business days"));
        assertThat(processingTime.getCountry(), is("US"));
        assertThat(processingTime.getCurrency(), is("USD"));
        assertThat(processingTime.getTransferMethodType(), is(BANK_ACCOUNT));

        List<TransferMethodType> transferMethodTypesUSD = new ArrayList<>(
                keyResultCaptorValue.getTransferMethodType(countryUS.getCode(), currencies.get(0).getCode()));
        assertThat(transferMethodTypesUSD, Matchers.<TransferMethodType>hasSize(6));
        assertThat(transferMethodTypesUSD.get(0).getCode(), is(BANK_ACCOUNT));
        assertThat(transferMethodTypesUSD.get(0).getName(), is("Bank Account"));
        assertThat(transferMethodTypesUSD.get(1).getCode(), is(BANK_CARD));
        assertThat(transferMethodTypesUSD.get(1).getName(), is("Debit Card"));
        assertThat(transferMethodTypesUSD.get(2).getCode(), is(PAPER_CHECK));
        assertThat(transferMethodTypesUSD.get(2).getName(), is("Paper Check"));
        assertThat(transferMethodTypesUSD.get(3).getCode(), is(PAYPAL_ACCOUNT));
        assertThat(transferMethodTypesUSD.get(3).getName(), is("PayPal"));
        assertThat(transferMethodTypesUSD.get(4).getCode(), is(VENMO_ACCOUNT));
        assertThat(transferMethodTypesUSD.get(4).getName(), is("Venmo"));
        assertThat(transferMethodTypesUSD.get(5).getCode(), is(WIRE_ACCOUNT));
        assertThat(transferMethodTypesUSD.get(5).getName(), is("Wire Transfer"));

        List<Fee> feesBankCard = new ArrayList<>(transferMethodTypesUSD.get(1).getFees()); //BANK_CARD
        assertThat(feesBankCard, Matchers.<Fee>hasSize(1));
        assertThat(feesBankCard.get(0).getFeeRateType(), is("FLAT"));
        assertThat(feesBankCard.get(0).getValue(), is("1.75"));

        ProcessingTime processingTimeBankCard = transferMethodTypes.get(1).getProcessingTime();
        assertThat(processingTimeBankCard.getValue(), is("Typically within 1 hour"));
        assertThat(processingTimeBankCard.getCountry(), is("US"));
        assertThat(processingTimeBankCard.getCurrency(), is("USD"));
        assertThat(processingTimeBankCard.getTransferMethodType(), is(BANK_CARD));


        List<Fee> feesPaperCheck = new ArrayList<>(transferMethodTypesUSD.get(2).getFees()); //PAPER_CHECK
        assertThat(feesPaperCheck, Matchers.<Fee>hasSize(1));
        assertThat(feesPaperCheck.get(0).getFeeRateType(), is("FLAT"));
        assertThat(feesPaperCheck.get(0).getValue(), is("0.25"));

        ProcessingTime processingTimePaperCheck = transferMethodTypes.get(2).getProcessingTime();
        assertThat(processingTimePaperCheck.getValue(), is("5 - 7 Business days"));
        assertThat(processingTimePaperCheck.getCountry(), is("US"));
        assertThat(processingTimePaperCheck.getCurrency(), is("USD"));
        assertThat(processingTimePaperCheck.getTransferMethodType(), is(PAPER_CHECK));

    }

    @Test
    public void testRetrieveTransferMethodConfigurationKeys_withErrorReturningFields()
            throws InterruptedException {
        String responseBody = mExternalResourceManager.getResourceContentError("tmc_gql_error_response.json");
        mServer.mockResponse().withHttpResponseCode(HttpURLConnection.HTTP_BAD_REQUEST).withBody(responseBody).mock();

        Hyperwallet.getDefault().retrieveTransferMethodConfigurationKeys(
                new TransferMethodConfigurationKeysQuery(), mListener);
        mAwait.await(100, TimeUnit.MILLISECONDS);

        RecordedRequest recordedRequest = mServer.getRequest();
        assertThat(recordedRequest.getMethod(), is(POST.name()));
        assertThat(recordedRequest.getPath(), is("/graphql/"));

        verify(mListener, never()).onSuccess(any(TransferMethodConfigurationKeyResult.class));
        verify(mListener).onFailure(mExceptionCaptor.capture());

        HyperwalletException hyperwalletException = mExceptionCaptor.getValue();
        assertThat(hyperwalletException, is(notNullValue()));
        Errors errors = hyperwalletException.getErrors();
        assertThat(errors, is(notNullValue()));
        assertThat(errors.getErrors(), is(notNullValue()));
        assertThat(errors.getErrors().size(), is(1));

        Error error = errors.getErrors().get(0);
        assertThat(error.getCode(), is("DataFetchingException"));
        assertThat(error.getMessage(), is("Could not find any currency."));
    }

    @Test
    public void testRetrieveTransferMethodConfigurationKeys_withErrorAndData() throws InterruptedException {
        String responseBody = mExternalResourceManager.getResourceContent("tmc_get_keys_response.json");
        mServer.mockResponse().withHttpResponseCode(HttpURLConnection.HTTP_OK).withBody(responseBody).mock();

        Hyperwallet.getDefault().retrieveTransferMethodConfigurationKeys(
                new TransferMethodConfigurationKeysQuery(), mListener);
        mAwait.await(100, TimeUnit.MILLISECONDS);

        RecordedRequest recordedRequest = mServer.getRequest();
        assertThat(recordedRequest.getMethod(), is(POST.name()));
        assertThat(recordedRequest.getPath(), is("/graphql/"));

        verify(mListener).onSuccess(mKeyResultCaptor.capture());
        verify(mListener, never()).onFailure(any(HyperwalletException.class));

        HyperwalletTransferMethodConfigurationKey keyResultCaptorValue = mKeyResultCaptor.getValue();
        assertThat(keyResultCaptorValue, is(notNullValue()));

        // this is not exposed yet to integrators because there's no way a user can correct itself from the
        // use cases that we don't expose yet but this is possible for users to know when things get sour
        // in GQL side, say if data exist together with errors in https://graphql.github
        // .io/graphql-spec/June2018/#sec-Errors
        // Example No.: 185 Partial success
        GqlResponse response = (GqlResponse) keyResultCaptorValue;
        assertThat(response, is(notNullValue()));
        assertThat(response.getData(), is(notNullValue()));
        assertThat(response.getData(), CoreMatchers.instanceOf(TransferMethodConfigurationKey.class));

        GqlErrors gqlErrors = response.getGqlErrors();
        assertThat(gqlErrors.getGQLErrors(), Matchers.<GqlError>hasSize(1));
    }
}
