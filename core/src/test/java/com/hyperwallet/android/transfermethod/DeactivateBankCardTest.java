package com.hyperwallet.android.transfermethod;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import static com.hyperwallet.android.util.HttpMethod.POST;

import com.hyperwallet.android.Hyperwallet;
import com.hyperwallet.android.exception.HyperwalletException;
import com.hyperwallet.android.listener.HyperwalletListener;
import com.hyperwallet.android.model.Error;
import com.hyperwallet.android.model.Errors;
import com.hyperwallet.android.model.StatusTransition;
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
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import okhttp3.mockwebserver.RecordedRequest;

@RunWith(RobolectricTestRunner.class)
public class DeactivateBankCardTest {

    @Rule
    public HyperwalletMockWebServer mServer = new HyperwalletMockWebServer();
    @Rule
    public HyperwalletSdkMock mHyperwalletSdkMock = new HyperwalletSdkMock(mServer);
    @Rule
    public ExternalResourceManager mExternalResourceManager = new ExternalResourceManager();
    @Rule
    public MockitoRule mMockito = MockitoJUnit.rule();
    @Mock
    private HyperwalletListener<StatusTransition> mMockStatusTransitionListener;
    @Captor
    private ArgumentCaptor<StatusTransition> mStatusTransitionCaptor;
    @Captor
    private ArgumentCaptor<HyperwalletException> mHyperwalletExceptionCaptor;

    private CountDownLatch mAwait = new CountDownLatch(1);


    @Test
    public void testDeactivateBankCard_successfulStatusTransition() throws Exception {

        String responseBody = mExternalResourceManager.getResourceContent(
                "successfully_deactivated_bank_card_response.json");
        mServer.mockResponse().withHttpResponseCode(HttpURLConnection.HTTP_CREATED).withBody(responseBody).mock();

        Hyperwallet.getDefault().deactivateBankCard("trm-fake-token", null, mMockStatusTransitionListener);
        mAwait.await(1000, TimeUnit.MILLISECONDS);

        verify(mMockStatusTransitionListener).onSuccess(mStatusTransitionCaptor.capture());
        verify(mMockStatusTransitionListener, never()).onFailure(any(HyperwalletException.class));
        StatusTransition statusTransitionResponse = mStatusTransitionCaptor.getValue();
        assertNotNull(statusTransitionResponse);
        assertThat(statusTransitionResponse.getFromStatus(),
                is(StatusTransition.StatusDefinition.ACTIVATED));
        assertThat(statusTransitionResponse.getToStatus(),
                is(StatusTransition.StatusDefinition.DE_ACTIVATED));
        assertThat(statusTransitionResponse.getTransition(),
                is(StatusTransition.StatusDefinition.DE_ACTIVATED));
        assertThat(statusTransitionResponse.getToken(), is("sts-fake-token"));
        assertNotNull(statusTransitionResponse.getCreatedOn());

        RecordedRequest recordedRequest = mServer.getRequest();
        assertThat(recordedRequest.getPath(),
                endsWith(
                        "users/test-user-token/bank-cards/trm-fake-token/status-transitions"));
        assertThat(recordedRequest.getMethod(), is(POST.name()));
    }


    @Test
    public void testDeactivateBankCard_unsuccessfulStatusTransition() throws Exception {

        String responseBody = mExternalResourceManager.getResourceContentError("invalid_status_transition.json");
        mServer.mockResponse().withHttpResponseCode(HttpURLConnection.HTTP_BAD_REQUEST).withBody(responseBody).mock();

        Hyperwallet.getDefault().deactivateBankCard("trm-fake-token", null, mMockStatusTransitionListener);
        mAwait.await(1000, TimeUnit.MILLISECONDS);

        verify(mMockStatusTransitionListener).onFailure(mHyperwalletExceptionCaptor.capture());
        verify(mMockStatusTransitionListener, never()).onSuccess(any(StatusTransition.class));
        HyperwalletException hyperwalletException = mHyperwalletExceptionCaptor.getValue();
        assertNotNull(hyperwalletException);
        final Errors errors = hyperwalletException.getErrors();
        assertNotNull(errors);
        final List<Error> statusTransitionErrorList = errors.getErrors();
        assertNotNull(statusTransitionErrorList);
        assertThat(statusTransitionErrorList.size(), is(1));
        Error statusTransitionError = statusTransitionErrorList.get(0);
        assertNotNull(statusTransitionError);
        assertThat(statusTransitionError.getCode(), is("INVALID_FIELD_VALUE"));
        assertThat(statusTransitionError.getFieldName(), is("transition"));
        assertThat(statusTransitionError.getMessage(), is("transition is invalid"));

        RecordedRequest recordedRequest = mServer.getRequest();
        assertThat(recordedRequest.getPath(),
                endsWith(
                        "users/test-user-token/bank-cards/trm-fake-token/status-transitions"));
        assertThat(recordedRequest.getMethod(), is(POST.name()));
    }


}
