package com.hyperwallet.android.transfermethod;

import com.hyperwallet.android.Hyperwallet;
import com.hyperwallet.android.exception.HyperwalletException;
import com.hyperwallet.android.exception.HyperwalletRestException;
import com.hyperwallet.android.listener.HyperwalletListener;
import com.hyperwallet.android.model.Error;
import com.hyperwallet.android.model.Errors;
import com.hyperwallet.android.model.transfermethod.PaperCheck;
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

import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.CITY;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.CREATED_ON;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.SHIPPING_METHOD;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.STATUS;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.TOKEN;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.TRANSFER_METHOD_COUNTRY;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.TRANSFER_METHOD_CURRENCY;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.TYPE;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodTypes.PAPER_CHECK;
import static com.hyperwallet.android.util.HttpMethod.PUT;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
public class UpdatePaperCheckTest {
    private final CountDownLatch mAwait = new CountDownLatch(1);
    @Rule
    public HyperwalletMockWebServer mServer = new HyperwalletMockWebServer();
    @Rule
    public HyperwalletSdkMock mHyperwalletSdkMock = new HyperwalletSdkMock(mServer);
    @Rule
    public ExternalResourceManager mExternalResourceManager = new ExternalResourceManager();
    @Rule
    public MockitoRule mMockito = MockitoJUnit.rule();
    @Mock
    private HyperwalletListener<PaperCheck> mListener;
    @Captor
    private ArgumentCaptor<PaperCheck> mPaperCheckAccountCaptor;
    @Captor
    private ArgumentCaptor<HyperwalletException> mExceptionCaptor;

    @Test
    public void testUpdatePaperCheck_withSuccess() throws InterruptedException {
        String responseBody = mExternalResourceManager.getResourceContent("paper_check_update_response.json");
        mServer.mockResponse().withHttpResponseCode(HttpURLConnection.HTTP_OK).withBody(responseBody).mock();

        final PaperCheck paperCheck = new PaperCheck
                .Builder()
                .token("trm-fake-token")
                .shippingMethod("EXPEDITED")
                .city("Victoria")
                .build();

        assertThat(paperCheck.getField(TOKEN), is("trm-fake-token"));

        Hyperwallet.getDefault().updatePaperCheck(paperCheck, mListener);
        mAwait.await(50, TimeUnit.MILLISECONDS);

        RecordedRequest recordedRequest = mServer.getRequest();
        assertThat(recordedRequest.getMethod(), is(PUT.name()));
        verify(mListener).onSuccess(mPaperCheckAccountCaptor.capture());
        verify(mListener, never()).onFailure(any(HyperwalletException.class));

        PaperCheck paperCheckResponse = mPaperCheckAccountCaptor.getValue();
        assertThat(paperCheckResponse, is(notNullValue()));

        assertThat(recordedRequest.getPath(),
                is("/rest/v3/users/test-user-token/paper-checks/trm-fake-token"));

        assertThat(paperCheckResponse.getField(TOKEN), is("trm-fake-token"));
        assertThat(paperCheckResponse.getField(STATUS), is("ACTIVATED"));
        assertThat(paperCheckResponse.getField(TYPE), is(PAPER_CHECK));
        assertThat(paperCheckResponse.getField(TRANSFER_METHOD_CURRENCY), is("USD"));
        assertThat(paperCheckResponse.getField(TRANSFER_METHOD_COUNTRY), is("US"));
        assertThat(paperCheckResponse.getField(CREATED_ON), is("2020-11-29T15:13:55"));
        assertThat(paperCheckResponse.getField(SHIPPING_METHOD), is("EXPEDITED"));
        assertThat(paperCheckResponse.getField(CITY), is("Victoria"));
    }

    @Test
    public void testUpdatePaperCheck_withValidationError() throws InterruptedException {

        String responseBody = mExternalResourceManager.getResourceContentError(
                "paper_check_update_error_response.json");
        mServer.mockResponse().withHttpResponseCode(HttpURLConnection.HTTP_BAD_REQUEST).withBody(responseBody).mock();

        final PaperCheck paperCheck = new PaperCheck
                .Builder()
                .token("trm-fake-token")
                .postalCode("1234")
                .build();

        assertThat(paperCheck.getField(TOKEN),
                is("trm-fake-token"));

        Hyperwallet.getDefault().updatePaperCheck(paperCheck, mListener);
        mAwait.await(50, TimeUnit.MILLISECONDS);

        RecordedRequest recordedRequest = mServer.getRequest();
        assertThat(recordedRequest.getMethod(), is(PUT.name()));
        verify(mListener, never()).onSuccess(any(PaperCheck.class));
        verify(mListener).onFailure(mExceptionCaptor.capture());

        HyperwalletException hyperwalletException = mExceptionCaptor.getValue();
        assertThat(hyperwalletException, is(notNullValue()));
        assertThat(((HyperwalletRestException) hyperwalletException).getHttpCode(),
                is(HttpURLConnection.HTTP_BAD_REQUEST));

        assertThat(recordedRequest.getPath(),
                is("/rest/v3/users/test-user-token/paper-checks/trm-fake-token"));

        Errors errors = hyperwalletException.getErrors();
        assertThat(errors, is(notNullValue()));
        assertThat(errors.getErrors(), hasSize(1));

        Error error1 = errors.getErrors().get(0);
        assertThat(error1.getCode(), is("CONSTRAINT_VIOLATIONS"));
        assertThat(error1.getFieldName(), is("postalCode"));
        assertThat(error1.getMessage(), is("Invalid Postal Code"));
    }
}
