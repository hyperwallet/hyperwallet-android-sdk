package com.hyperwallet.android.transfermethod;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.hyperwallet.android.Hyperwallet;
import com.hyperwallet.android.exception.HyperwalletException;
import com.hyperwallet.android.listener.HyperwalletListener;
import com.hyperwallet.android.model.HyperwalletError;
import com.hyperwallet.android.model.HyperwalletErrors;
import com.hyperwallet.android.model.HyperwalletStatusTransition;
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

@RunWith(RobolectricTestRunner.class)
public class HyperwalletDeactivateBankCardTest {

    @Rule
    public HyperwalletMockWebServer server = new HyperwalletMockWebServer();
    @Rule
    public HyperwalletSdkMock hyperwalletSdkMock = new HyperwalletSdkMock(server);
    @Rule
    public HyperwalletExternalResourceManager externalResourceManager = new HyperwalletExternalResourceManager();
    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();
    @Mock
    private HyperwalletListener<HyperwalletStatusTransition> mockStatusTransitionListener;
    @Captor
    private ArgumentCaptor<HyperwalletStatusTransition> statusTransitionCaptor;
    @Captor
    private ArgumentCaptor<HyperwalletException> hyperwalletExceptionCaptor;

    private CountDownLatch await = new CountDownLatch(1);


    @Test
    public void testDeactivateBankCard_successfulStatusTransition() throws Exception {

        String responseBody = externalResourceManager.getResourceContent(
                "successfully_deactivated_bank_card_response.json");
        server.mockResponse().withHttpResponseCode(HttpURLConnection.HTTP_CREATED).withBody(responseBody).mock();

        Hyperwallet.getDefault().deactivateBankCard("trm-fake-token", null, mockStatusTransitionListener);
        await.await(1000, TimeUnit.MILLISECONDS);

        verify(mockStatusTransitionListener).onSuccess(statusTransitionCaptor.capture());
        verify(mockStatusTransitionListener, never()).onFailure(any(HyperwalletException.class));
        HyperwalletStatusTransition statusTransitionResponse = statusTransitionCaptor.getValue();
        assertNotNull(statusTransitionResponse);
        assertThat(statusTransitionResponse.getFromStatus(),
                is(HyperwalletStatusTransition.StatusDefinition.ACTIVATED));
        assertThat(statusTransitionResponse.getToStatus(),
                is(HyperwalletStatusTransition.StatusDefinition.DE_ACTIVATED));
        assertThat(statusTransitionResponse.getTransition(),
                is(HyperwalletStatusTransition.StatusDefinition.DE_ACTIVATED));
        assertThat(statusTransitionResponse.getToken(), is("sts-70ddc78a-0c14-4a72-8390-75d49ff376f2"));
        assertNotNull(statusTransitionResponse.getCreatedOn());
        assertThat(server.getRequest().getPath(),
                endsWith(
                        "users/usr-fbfd5848-60d0-43c5-8462-099c959b49c7/bank-cards/trm-fake-token/status-transitions"));

    }


    @Test
    public void testDeactivateBankCard_unsuccessfulStatusTransition() throws Exception {

        String responseBody = externalResourceManager.getResourceContentError("invalid_status_transition.json");
        server.mockResponse().withHttpResponseCode(HttpURLConnection.HTTP_BAD_REQUEST).withBody(responseBody).mock();

        Hyperwallet.getDefault().deactivateBankCard("trm-fake-token", null, mockStatusTransitionListener);
        await.await(1000, TimeUnit.MILLISECONDS);

        verify(mockStatusTransitionListener).onFailure(hyperwalletExceptionCaptor.capture());
        verify(mockStatusTransitionListener, never()).onSuccess(any(HyperwalletStatusTransition.class));
        HyperwalletException hyperwalletException = hyperwalletExceptionCaptor.getValue();
        assertNotNull(hyperwalletException);
        final HyperwalletErrors hyperwalletErrors = hyperwalletException.getHyperwalletErrors();
        assertNotNull(hyperwalletErrors);
        final List<HyperwalletError> statusTransitionErrorList = hyperwalletErrors.getErrors();
        assertNotNull(statusTransitionErrorList);
        assertThat(statusTransitionErrorList.size(), is(1));
        HyperwalletError statusTransitionError = statusTransitionErrorList.get(0);
        assertNotNull(statusTransitionError);
        assertThat(statusTransitionError.getCode(), is("INVALID_FIELD_VALUE"));
        assertThat(statusTransitionError.getFieldName(), is("transition"));
        assertThat(statusTransitionError.getMessage(), is("transition is invalid"));
        assertThat(server.getRequest().getPath(),
                endsWith(
                        "users/usr-fbfd5848-60d0-43c5-8462-099c959b49c7/bank-cards/trm-fake-token/status-transitions"));
    }


}
