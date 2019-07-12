package com.hyperwallet.android.transfermethod;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import static com.hyperwallet.android.util.HttpMethod.POST;

import com.hyperwallet.android.Hyperwallet;
import com.hyperwallet.android.exception.HyperwalletException;
import com.hyperwallet.android.listener.HyperwalletListener;
import com.hyperwallet.android.model.HyperwalletError;
import com.hyperwallet.android.model.HyperwalletErrors;
import com.hyperwallet.android.model.StatusTransition;
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
public class HyperwalletDeactivatePayPalAccountTest {

    @Rule
    public HyperwalletMockWebServer mServer = new HyperwalletMockWebServer();
    @Rule
    public HyperwalletSdkMock mSdkMock = new HyperwalletSdkMock(mServer);
    @Rule
    public HyperwalletExternalResourceManager mExternalResourceManager = new HyperwalletExternalResourceManager();
    @Rule
    public MockitoRule mMockitoRule = MockitoJUnit.rule();
    @Mock
    private HyperwalletListener<StatusTransition> mListener;
    @Captor
    private ArgumentCaptor<StatusTransition> mStatusTransitionCaptor;
    @Captor
    private ArgumentCaptor<HyperwalletException> mExceptionArgumentCaptor;

    private final CountDownLatch mAwait = new CountDownLatch(1);

    @Test
    public void testDeactivatePayPalAccount_successfulStatusTransition() throws Exception {

        String responseBody = mExternalResourceManager.getResourceContent(
                "deactivated_paypal_account_response.json");
        mServer.mockResponse().withHttpResponseCode(HttpURLConnection.HTTP_CREATED).withBody(responseBody).mock();

        Hyperwallet.getDefault().deactivatePayPalAccount("trm-da21954a-3910-4d70-b83d-0c2d96104394", null,
                mListener);
        mAwait.await(50, TimeUnit.MILLISECONDS);

        verify(mListener).onSuccess(mStatusTransitionCaptor.capture());
        verify(mListener, never()).onFailure(any(HyperwalletException.class));
        StatusTransition statusTransitionResponse = mStatusTransitionCaptor.getValue();
        assertNotNull(statusTransitionResponse);
        assertThat(statusTransitionResponse.getFromStatus(),
                is(StatusTransition.StatusDefinition.ACTIVATED));
        assertThat(statusTransitionResponse.getToStatus(),
                is(StatusTransition.StatusDefinition.DE_ACTIVATED));
        assertThat(statusTransitionResponse.getTransition(),
                is(StatusTransition.StatusDefinition.DE_ACTIVATED));
        assertThat(statusTransitionResponse.getToken(), is("sts-70ddc78a-0c14-4a72-8390-75d49ff376f2"));
        assertNotNull(statusTransitionResponse.getCreatedOn());
        final RecordedRequest request = mServer.getRequest();
        assertThat(request.getPath(), endsWith(
                "users/usr-fbfd5848-60d0-43c5-8462-099c959b49c7/paypal-accounts/trm-da21954a-3910-4d70-b83d"
                        + "-0c2d96104394/status-transitions"));
        assertThat(request.getMethod(), is(POST.name()));
    }

    @Test
    public void testDeactivatePayPalAccount_unsuccessfulStatusTransition() throws Exception {

        String responseBody = mExternalResourceManager.getResourceContentError("invalid_status_transition.json");
        mServer.mockResponse().withHttpResponseCode(HttpURLConnection.HTTP_BAD_REQUEST).withBody(responseBody).mock();

        Hyperwallet.getDefault().deactivatePayPalAccount("trm-fake-token", null, mListener);
        mAwait.await(100, TimeUnit.MILLISECONDS);

        verify(mListener).onFailure(mExceptionArgumentCaptor.capture());
        verify(mListener, never()).onSuccess(any(StatusTransition.class));
        HyperwalletException hyperwalletException = mExceptionArgumentCaptor.getValue();
        assertNotNull(hyperwalletException);
        final HyperwalletErrors hyperwalletErrors = hyperwalletException.getHyperwalletErrors();
        assertNotNull(hyperwalletErrors);
        final List<HyperwalletError> statusTransitionErrorList = hyperwalletErrors.getErrors();
        assertNotNull(statusTransitionErrorList);
        assertThat(statusTransitionErrorList, hasSize(1));
        HyperwalletError statusTransitionError = statusTransitionErrorList.get(0);
        assertNotNull(statusTransitionError);
        assertThat(statusTransitionError.getCode(), is("INVALID_FIELD_VALUE"));
        assertThat(statusTransitionError.getFieldName(), is("transition"));
        assertThat(statusTransitionError.getMessage(), is("transition is invalid"));
        final RecordedRequest request = mServer.getRequest();
        assertThat(request.getPath(), endsWith(
                "users/usr-fbfd5848-60d0-43c5-8462-099c959b49c7/paypal-accounts/trm-fake-token/status"
                        + "-transitions"));
        assertThat(request.getMethod(), is(POST.name()));
    }
}
