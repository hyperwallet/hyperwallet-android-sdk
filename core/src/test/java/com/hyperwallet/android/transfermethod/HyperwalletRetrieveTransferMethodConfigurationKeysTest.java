package com.hyperwallet.android.transfermethod;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import static com.hyperwallet.android.util.HttpMethod.POST;

import com.hyperwallet.android.Hyperwallet;
import com.hyperwallet.android.exception.HyperwalletException;
import com.hyperwallet.android.listener.HyperwalletListener;
import com.hyperwallet.android.model.HyperwalletError;
import com.hyperwallet.android.model.HyperwalletErrors;
import com.hyperwallet.android.model.meta.Fee;
import com.hyperwallet.android.model.meta.HyperwalletTransferMethodConfigurationKeyResult;
import com.hyperwallet.android.model.meta.query.HyperwalletTransferMethodConfigurationKeysQuery;
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
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import okhttp3.mockwebserver.RecordedRequest;

@RunWith(RobolectricTestRunner.class)
public class HyperwalletRetrieveTransferMethodConfigurationKeysTest {
    @Rule
    public HyperwalletMockWebServer mServer = new HyperwalletMockWebServer();
    @Rule
    public HyperwalletSdkMock mSdkMock = new HyperwalletSdkMock(mServer);
    @Rule
    public HyperwalletExternalResourceManager mExternalResourceManager = new HyperwalletExternalResourceManager();
    @Rule
    public MockitoRule mMockito = MockitoJUnit.rule();

    @Mock
    private HyperwalletListener<HyperwalletTransferMethodConfigurationKeyResult> mListener;
    @Captor
    private ArgumentCaptor<HyperwalletTransferMethodConfigurationKeyResult> mKeyResultCaptor;
    @Captor
    private ArgumentCaptor<HyperwalletException> mExceptionCaptor;

    private CountDownLatch mAwait = new CountDownLatch(1);

    @Test
    public void testRetrieveTransferMethodConfigurationKeys_returnsFields() throws InterruptedException {
        String responseBody = mExternalResourceManager.getResourceContent("tmc_get_keys_response.json");
        mServer.mockResponse().withHttpResponseCode(HttpURLConnection.HTTP_OK).withBody(responseBody).mock();

        Hyperwallet.getDefault().retrieveTransferMethodConfigurationKeys(
                new HyperwalletTransferMethodConfigurationKeysQuery(), mListener);
        mAwait.await(500, TimeUnit.MILLISECONDS);

        RecordedRequest recordedRequest = mServer.getRequest();
        assertThat(recordedRequest.getPath(), is("/graphql/"));
        assertThat(recordedRequest.getMethod(), is(POST.name()));

        verify(mListener).onSuccess(mKeyResultCaptor.capture());
        verify(mListener, never()).onFailure(any(HyperwalletException.class));

        HyperwalletTransferMethodConfigurationKeyResult transferMethodConfigurationKeyResult =
                mKeyResultCaptor.getValue();

        assertThat(transferMethodConfigurationKeyResult, is(notNullValue()));

        assertThat(transferMethodConfigurationKeyResult.getCountries(), is(not(empty())));
        assertThat(transferMethodConfigurationKeyResult.getCountries(), hasSize(2));
        assertThat(transferMethodConfigurationKeyResult.getCountries(), hasItems("US", "TW"));
        assertThat(transferMethodConfigurationKeyResult.getCurrencies("US"), hasItems("USD"));
        assertThat(transferMethodConfigurationKeyResult.getTransferMethods("US", "USD", "INDIVIDUAL"),
                hasItems("BANK_CARD"));
        assertThat(transferMethodConfigurationKeyResult.getFees("US", "USD", "BANK_ACCOUNT", "BUSINESS"),
                is(not(empty())));

        List<Fee> fees = transferMethodConfigurationKeyResult.getFees("TW", "USD", "WIRE_ACCOUNT", "INDIVIDUAL");
        assertThat(fees, hasSize(1));

        Fee fee = fees.get(0);
        assertThat(fee.getCountry(), is("TW"));
        assertThat(fee.getCurrency(), is("USD"));
        assertThat(fee.getFeeRateType(), is("FLAT"));
        assertThat(fee.getTransferMethodType(), is("WIRE_ACCOUNT"));
        assertThat(fee.getValue(), is("1500.00"));
    }

    @Test
    public void testRetrieveTransferMethodConfigurationKeys_withErrorReturningFields() throws InterruptedException {
        String responseBody = mExternalResourceManager.getResourceContentError("tmc_gql_error_response.json");
        mServer.mockResponse().withHttpResponseCode(HttpURLConnection.HTTP_BAD_REQUEST).withBody(responseBody).mock();

        Hyperwallet.getDefault().retrieveTransferMethodConfigurationKeys(
                new HyperwalletTransferMethodConfigurationKeysQuery(), mListener);
        mAwait.await(500, TimeUnit.MILLISECONDS);

        RecordedRequest recordedRequest = mServer.getRequest();
        assertThat(recordedRequest.getPath(), is("/graphql/"));
        assertThat(recordedRequest.getMethod(), is(POST.name()));

        verify(mListener, never()).onSuccess(any(HyperwalletTransferMethodConfigurationKeyResult.class));
        verify(mListener).onFailure(mExceptionCaptor.capture());

        HyperwalletException hyperwalletException = mExceptionCaptor.getValue();
        assertThat(hyperwalletException, is(notNullValue()));
        HyperwalletErrors hyperwalletErrors = hyperwalletException.getHyperwalletErrors();
        assertThat(hyperwalletErrors, is(notNullValue()));
        assertThat(hyperwalletErrors.getErrors(), is(notNullValue()));
        assertThat(hyperwalletErrors.getErrors().size(), is(1));

        HyperwalletError hyperwalletError = hyperwalletErrors.getErrors().get(0);
        assertThat(hyperwalletError.getCode(), is("DataFetchingException"));
        assertThat(hyperwalletError.getMessage(), is("Could not find any currency."));
    }
}
