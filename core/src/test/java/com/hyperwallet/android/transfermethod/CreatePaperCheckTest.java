package com.hyperwallet.android.transfermethod;

import com.hyperwallet.android.Hyperwallet;
import com.hyperwallet.android.exception.HyperwalletException;
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

import static com.hyperwallet.android.model.StatusTransition.StatusDefinition.ACTIVATED;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.ADDRESS_LINE_1;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.CREATED_ON;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.SHIPPING_METHOD;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.STATUS;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.TOKEN;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.TRANSFER_METHOD_COUNTRY;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.TRANSFER_METHOD_CURRENCY;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.TYPE;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodTypes.PAPER_CHECK;
import static com.hyperwallet.android.util.HttpMethod.POST;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
public class CreatePaperCheckTest {

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
    private ArgumentCaptor<PaperCheck> mPaperCheckCaptor;
    @Captor
    private ArgumentCaptor<HyperwalletException> mExceptionCaptor;

    @Test
    public void testCreatePaperCheck_withSuccess() throws InterruptedException {

        String responseBody = mExternalResourceManager.getResourceContent("paper_check_response.json");
        mServer.mockResponse().withHttpResponseCode(HttpURLConnection.HTTP_CREATED).withBody(responseBody).mock();

        final PaperCheck paperCheck = new PaperCheck
                .Builder("US", "USD")
                .build();

        assertThat(paperCheck.getField(TRANSFER_METHOD_COUNTRY), is("US"));
        assertThat(paperCheck.getField(TRANSFER_METHOD_CURRENCY), is("USD"));

        Hyperwallet.getDefault().createPaperCheck(paperCheck, mListener);
        mAwait.await(50, TimeUnit.MILLISECONDS);

        RecordedRequest recordedRequest = mServer.getRequest();
        assertThat(recordedRequest.getMethod(), is(POST.name()));
        verify(mListener).onSuccess(mPaperCheckCaptor.capture());
        verify(mListener, never()).onFailure(any(HyperwalletException.class));

        PaperCheck paperCheckResponse = mPaperCheckCaptor.getValue();
        assertThat(paperCheckResponse, is(notNullValue()));

        //PaperCheck info
        assertThat(recordedRequest.getPath(),
                is("/rest/v3/users/test-user-token/paper-checks"));

        assertThat(paperCheckResponse.getField(STATUS), is(ACTIVATED));
        assertThat(paperCheckResponse.getField(TOKEN), is("trm-fake-token"));
        assertThat(paperCheckResponse.getField(TRANSFER_METHOD_COUNTRY), is("US"));
        assertThat(paperCheckResponse.getField(TRANSFER_METHOD_CURRENCY), is("USD"));
        assertThat(paperCheckResponse.getField(TYPE), is(PAPER_CHECK));
        assertThat(paperCheckResponse.getField(CREATED_ON), is(notNullValue()));
        assertThat(paperCheckResponse.getField(ADDRESS_LINE_1), is("1234 IndividualAddress St"));
        assertThat(paperCheckResponse.getField(SHIPPING_METHOD), is("STANDARD"));
    }

    @Test
    public void testCreatePaperCheck_withValidationError() throws InterruptedException {
        String responseBody = mExternalResourceManager.getResourceContentError("transfer_method_error_response.json");
        mServer.mockResponse().withHttpResponseCode(HttpURLConnection.HTTP_BAD_REQUEST).withBody(responseBody).mock();

        final PaperCheck paperCheck = new PaperCheck
                .Builder("", "USD")
                .build();

        Hyperwallet.getDefault().createPaperCheck(paperCheck, mListener);
        mAwait.await(50, TimeUnit.MILLISECONDS);

        RecordedRequest recordedRequest = mServer.getRequest();
        assertThat(recordedRequest.getMethod(), is(POST.name()));
        verify(mListener, never()).onSuccess(any(PaperCheck.class));
        verify(mListener).onFailure(mExceptionCaptor.capture());

        HyperwalletException hyperwalletException = mExceptionCaptor.getValue();
        assertThat(hyperwalletException, is(notNullValue()));

        assertThat(recordedRequest.getPath(),
                is("/rest/v3/users/test-user-token/paper-checks"));

        Errors errors = hyperwalletException.getErrors();
        assertThat(errors.getErrors(), hasSize(1));

        Error error = errors.getErrors().get(0);
        assertThat(error.getCode(), is("CONSTRAINT_VIOLATIONS"));
        assertThat(error.getFieldName(), is("transferMethodCountry"));
        assertThat(error.getMessage(), is("You must provide a value for this field"));
    }
}
