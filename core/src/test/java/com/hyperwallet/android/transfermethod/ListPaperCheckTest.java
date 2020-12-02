package com.hyperwallet.android.transfermethod;

import com.hyperwallet.android.Hyperwallet;
import com.hyperwallet.android.exception.HyperwalletException;
import com.hyperwallet.android.exception.HyperwalletRestException;
import com.hyperwallet.android.listener.HyperwalletListener;
import com.hyperwallet.android.model.Error;
import com.hyperwallet.android.model.Errors;
import com.hyperwallet.android.model.paging.PageList;
import com.hyperwallet.android.model.transfermethod.PaperCheck;
import com.hyperwallet.android.model.transfermethod.PaperCheckQueryParam;
import com.hyperwallet.android.rule.ExternalResourceManager;
import com.hyperwallet.android.rule.HyperwalletMockWebServer;
import com.hyperwallet.android.rule.HyperwalletSdkMock;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RobolectricTestRunner;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import okhttp3.mockwebserver.RecordedRequest;

import static com.hyperwallet.android.model.StatusTransition.StatusDefinition.ACTIVATED;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.CREATED_ON;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.PROFILE_TYPE;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.SHIPPING_METHOD;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.STATUS;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.TOKEN;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.TRANSFER_METHOD_COUNTRY;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.TRANSFER_METHOD_CURRENCY;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.TYPE;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodTypes.PAPER_CHECK;
import static com.hyperwallet.android.util.HttpMethod.GET;
import static java.net.HttpURLConnection.HTTP_INTERNAL_ERROR;
import static java.net.HttpURLConnection.HTTP_NO_CONTENT;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
public class ListPaperCheckTest {
    @Rule
    public ExternalResourceManager mExternalResourceManager = new ExternalResourceManager();
    @Rule
    public HyperwalletMockWebServer mServer = new HyperwalletMockWebServer();
    @Rule
    public HyperwalletSdkMock mHyperwalletSdkMock = new HyperwalletSdkMock(mServer);
    @Rule
    public MockitoRule mMockito = MockitoJUnit.rule();
    @Mock
    private HyperwalletListener<PageList<PaperCheck>> mListener;
    @Captor
    private ArgumentCaptor<PageList<PaperCheck>> mListPaperCheckCaptor;
    @Captor
    private ArgumentCaptor<HyperwalletException> mExceptionCaptor;

    private CountDownLatch mAwait = new CountDownLatch(1);


    @Test
    public void testListPaperCheck_returnsActivatedAccounts() throws InterruptedException {

        String responseBody = mExternalResourceManager.getResourceContent("paper_checks_response.json");
        mServer.mockResponse().withHttpResponseCode(HTTP_OK).withBody(responseBody).mock();

        PaperCheckQueryParam queryParam = new PaperCheckQueryParam.Builder()
                .status(ACTIVATED)
                .build();

        assertThat(queryParam, is(notNullValue()));
        Hyperwallet.getDefault().listPaperChecks(queryParam, mListener);

        mAwait.await(500, TimeUnit.MILLISECONDS);

        RecordedRequest recordedRequest = mServer.getRequest();
        assertThat(recordedRequest.getMethod(), is(GET.name()));
        verify(mListener).onSuccess(mListPaperCheckCaptor.capture());
        verify(mListener, never()).onFailure(any(HyperwalletException.class));

        PageList<PaperCheck> paperCheckResponse = mListPaperCheckCaptor.getValue();

        assertThat(paperCheckResponse.getCount(), is(1));
        assertThat(paperCheckResponse.getDataList(), hasSize(1));
        assertThat(paperCheckResponse.getOffset(), is(0));
        assertThat(paperCheckResponse.getLimit(), is(10));

        assertThat(recordedRequest.getPath(),
                is("/rest/v3/users/test-user-token/paper-checks?limit=10&offset=0&type"
                        + "=PAPER_CHECK&status=ACTIVATED"));

        PaperCheck paperCheck = paperCheckResponse.getDataList().get(0);
        assertThat(paperCheck.getField(TOKEN), is("trm-fake-token"));
        assertThat(paperCheck.getField(TYPE), is(PAPER_CHECK));
        assertThat(paperCheck.getField(STATUS), is(ACTIVATED));
        assertThat(paperCheck.getField(CREATED_ON), is("2020-11-29T13:41:56"));
        assertThat(paperCheck.getField(TRANSFER_METHOD_COUNTRY), is("US"));
        assertThat(paperCheck.getField(TRANSFER_METHOD_CURRENCY), is("USD"));
        assertThat(paperCheck.getField(PROFILE_TYPE), is("PUBLIC_COMPANY"));
        assertThat(paperCheck.getField(SHIPPING_METHOD), is("EXPEDITED"));
    }

    @Test
    public void testListPaperCheck_returnsNoAccounts() throws InterruptedException {
        mServer.mockResponse().withHttpResponseCode(HTTP_NO_CONTENT).withBody("").mock();

        PaperCheckQueryParam queryParam = new PaperCheckQueryParam.Builder()
                .status(ACTIVATED)
                .build();

        assertThat(queryParam, is(notNullValue()));
        Hyperwallet.getDefault().listPaperChecks(queryParam, mListener);

        mAwait.await(500, TimeUnit.MILLISECONDS);

        RecordedRequest recordedRequest = mServer.getRequest();
        assertThat(recordedRequest.getMethod(), is(GET.name()));
        assertThat(recordedRequest.getPath(),
                containsString("/rest/v3/users/test-user-token/paper-checks?"));
        assertThat(recordedRequest.getPath(), containsString("type=PAPER_CHECK"));
        assertThat(recordedRequest.getPath(), containsString("limit=10"));
        assertThat(recordedRequest.getPath(), containsString("offset=0"));
        assertThat(recordedRequest.getPath(), containsString("status=ACTIVATED"));

        verify(mListener).onSuccess(mListPaperCheckCaptor.capture());
        verify(mListener, never()).onFailure(any(HyperwalletException.class));

        PageList<PaperCheck> paperCheckResponse = mListPaperCheckCaptor.getValue();
        assertThat(paperCheckResponse, is(nullValue()));
    }

    @Test
    public void testListPaperCheck_returnsError() throws InterruptedException {
        String responseBody = mExternalResourceManager.getResourceContentError("system_error_response.json");
        mServer.mockResponse().withHttpResponseCode(HTTP_INTERNAL_ERROR).withBody(responseBody).mock();

        PaperCheckQueryParam queryParam = new PaperCheckQueryParam.Builder()
                .status(ACTIVATED)
                .build();

        Hyperwallet.getDefault().listPaperChecks(queryParam, mListener);
        mAwait.await(500, TimeUnit.MILLISECONDS);

        verify(mListener, never()).onSuccess(ArgumentMatchers.<PageList<PaperCheck>>any());
        verify(mListener).onFailure(mExceptionCaptor.capture());

        HyperwalletException hyperwalletException = mExceptionCaptor.getValue();
        assertThat(hyperwalletException, is(notNullValue()));
        assertThat(((HyperwalletRestException) hyperwalletException).getHttpCode(),
                is(HTTP_INTERNAL_ERROR));

        Errors errors = hyperwalletException.getErrors();
        assertThat(errors, is(notNullValue()));
        assertThat(errors.getErrors(), is(notNullValue()));
        assertThat(errors.getErrors(), Matchers.<Error>hasSize(1));

        Error error = errors.getErrors().get(0);
        assertThat(error.getCode(), is("SYSTEM_ERROR"));
        assertThat(error.getMessage(),
                is("A system error has occurred. Please try again. If you continue to receive this error, please "
                        + "contact customer support for assistance (Ref ID: 99b4ad5c-4aac-4cc2-aa9b-4b4f4844ac9b)."));
        assertThat(error.getFieldName(), is(nullValue()));
        RecordedRequest recordedRequest = mServer.getRequest();
        assertThat(recordedRequest.getMethod(), is(GET.name()));
        assertThat(recordedRequest.getPath(),
                containsString("/rest/v3/users/test-user-token/paper-checks?"));
        assertThat(recordedRequest.getPath(), containsString("type=PAPER_CHECK"));
        assertThat(recordedRequest.getPath(), containsString("limit=10"));
        assertThat(recordedRequest.getPath(), containsString("offset=0"));
        assertThat(recordedRequest.getPath(), containsString("status=ACTIVATED"));
    }
}
