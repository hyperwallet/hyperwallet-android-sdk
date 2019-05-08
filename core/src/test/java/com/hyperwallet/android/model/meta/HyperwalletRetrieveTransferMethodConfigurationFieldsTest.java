package com.hyperwallet.android.model.meta;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isEmptyString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.hyperwallet.android.Hyperwallet;
import com.hyperwallet.android.exception.HyperwalletException;
import com.hyperwallet.android.listener.HyperwalletListener;
import com.hyperwallet.android.model.HyperwalletError;
import com.hyperwallet.android.model.HyperwalletErrors;
import com.hyperwallet.android.model.meta.query.HyperwalletTransferMethodConfigurationFieldQuery;
import com.hyperwallet.android.rule.HyperwalletExternalResourceManager;
import com.hyperwallet.android.rule.HyperwalletMockWebServer;
import com.hyperwallet.android.rule.HyperwalletSdkMock;

import org.json.JSONException;
import org.json.JSONObject;
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

@RunWith(RobolectricTestRunner.class)
public class HyperwalletRetrieveTransferMethodConfigurationFieldsTest {

    @Rule
    public HyperwalletMockWebServer mServer = new HyperwalletMockWebServer();
    @Rule
    public HyperwalletSdkMock mSdkMock = new HyperwalletSdkMock(mServer);
    @Rule
    public final HyperwalletExternalResourceManager mHyperwalletResourceManager =
            new HyperwalletExternalResourceManager();
    @Rule
    public MockitoRule mMockito = MockitoJUnit.rule();

    @Mock
    private HyperwalletListener<HyperwalletTransferMethodConfigurationFieldResult> mListener;
    @Captor
    private ArgumentCaptor<HyperwalletException> mExceptionCaptor;

    private CountDownLatch mAwait = new CountDownLatch(1);

    @Test
    public void testRetrieveTransferMethodConfigurationFields_returnsFields()
            throws ReflectiveOperationException, JSONException {
        JSONObject jsonObject = new JSONObject(
                mHyperwalletResourceManager.getResourceContent("tmc_configuration_connection_response.json"));
        TransferMethodConfigurationResult transferMethodConfigurationResult = new TransferMethodConfigurationResult(
                jsonObject);

        assertThat(transferMethodConfigurationResult.getFields().size(), is(2));

        HyperwalletField field1 = transferMethodConfigurationResult.getFields().get(0);
        assertThat(field1.getCategory(), is("ACCOUNT"));
        assertThat(field1.getDataType(), is(EDataType.getDataType("SELECTION")));
        assertThat(field1.isRequired(), is(false));
        assertThat(field1.getLabel(), is("Shipping Method"));
        assertThat(field1.getMinLength(), is(0));
        assertThat(field1.getMaxLength(), is(Integer.MAX_VALUE));
        assertThat(field1.getName(), is("shippingMethod"));
        assertThat(field1.getPlaceholder(), isEmptyString());
        assertThat(field1.getRegularExpression(), isEmptyString());

        HyperwalletField field2 = transferMethodConfigurationResult.getFields().get(1);
        assertThat(field2.getCategory(), is("ADDRESS"));
        assertThat(field2.getDataType(), is(EDataType.getDataType("SELECTION")));
        assertThat(field2.isRequired(), is(true));
        assertThat(field2.getLabel(), is("Country"));
        assertThat(field2.getMinLength(), is(2));
        assertThat(field2.getMaxLength(), is(30));
        assertThat(field2.getName(), is("country"));
        assertThat(field2.getPlaceholder(), isEmptyString());
        assertThat(field2.getRegularExpression(), isEmptyString());

    }

    @Test
    public void testRetrieveTransferMethodConfigurationFields_withErrorReturningFields()
            throws InterruptedException {
        String responseBody = mHyperwalletResourceManager.getResourceContentError(
                "gql_error_response.json");

        mServer.mockResponse().withHttpResponseCode(HttpURLConnection.HTTP_BAD_REQUEST).withBody(responseBody).mock();
        HyperwalletTransferMethodConfigurationFieldQuery transferMethodConfigurationResult =
                new HyperwalletTransferMethodConfigurationFieldQuery("US",
                        "USD",
                        "BANK_ACCOUNT",
                        "INDIVIDUAL");

        Hyperwallet.getDefault().retrieveTransferMethodConfigurationFields(transferMethodConfigurationResult,
                mListener);

        mAwait.await(500, TimeUnit.MILLISECONDS);

        verify(mListener, never()).onSuccess(any(HyperwalletTransferMethodConfigurationFieldResult.class));
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
