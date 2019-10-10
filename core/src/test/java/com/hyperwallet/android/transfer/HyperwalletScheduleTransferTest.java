package com.hyperwallet.android.transfer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_CREATED;

import static com.hyperwallet.android.model.StatusTransition.StatusDefinition.QUOTED;
import static com.hyperwallet.android.model.StatusTransition.StatusDefinition.SCHEDULED;
import static com.hyperwallet.android.util.HttpMethod.POST;

import com.hyperwallet.android.Hyperwallet;
import com.hyperwallet.android.exception.HyperwalletException;
import com.hyperwallet.android.listener.HyperwalletListener;
import com.hyperwallet.android.model.HyperwalletError;
import com.hyperwallet.android.model.StatusTransition;
import com.hyperwallet.android.rule.HyperwalletExternalResourceManager;
import com.hyperwallet.android.rule.HyperwalletMockWebServer;
import com.hyperwallet.android.rule.HyperwalletSdkMock;

import org.hamcrest.Matchers;
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

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import okhttp3.mockwebserver.RecordedRequest;

@RunWith(RobolectricTestRunner.class)
public class HyperwalletScheduleTransferTest {

    @Rule
    public HyperwalletMockWebServer mHwPlatform = new HyperwalletMockWebServer();
    @Rule
    public HyperwalletSdkMock mSdkMock = new HyperwalletSdkMock(mHwPlatform);
    @Rule
    public HyperwalletExternalResourceManager mResourceManager = new HyperwalletExternalResourceManager();
    @Rule
    public MockitoRule mMockitoRule = MockitoJUnit.rule();
    @Mock
    private HyperwalletListener<StatusTransition> mMockedStatusTransitionListener;
    @Captor
    private ArgumentCaptor<StatusTransition> mTransferCommitCaptor;
    @Captor
    private ArgumentCaptor<HyperwalletException> mExceptionArgumentCaptor;

    private CountDownLatch mCountDownLatch = new CountDownLatch(1);

    @Test
    public void testScheduleTransfer_successfulCommit() throws Exception {
        // prepare mock
        String response = mResourceManager.getResourceContent("transfer_commit_successful.json");
        mHwPlatform.mockResponse().withHttpResponseCode(HTTP_CREATED).withBody(response).mock();

        // test
        Hyperwallet.getDefault().scheduleTransfer("trf-recently-created-token",
                "commit transfer test notes", mMockedStatusTransitionListener);
        mCountDownLatch.await(100, TimeUnit.MILLISECONDS);

        // verify
        verify(mMockedStatusTransitionListener).onSuccess(mTransferCommitCaptor.capture());
        verify(mMockedStatusTransitionListener, never()).onFailure(any(HyperwalletException.class));

        // assert
        RecordedRequest recordedRequest = mHwPlatform.getRequest();
        String requestBody = recordedRequest.getBody().readUtf8();
        JSONObject bodyJson = new JSONObject(requestBody);
        assertThat(bodyJson.getString("notes"), is("commit transfer test notes"));
        assertThat(bodyJson.getString("transition"), is(SCHEDULED));

        String path = recordedRequest.getPath();
        assertThat(path, is("/rest/v3/transfers/trf-recently-created-token/status-transitions"));

        assertThat(recordedRequest.getMethod(), is(POST.name()));

        StatusTransition transferStatus = mTransferCommitCaptor.getValue();
        assertThat(transferStatus, is(notNullValue()));
        assertThat(transferStatus.getCreatedOn(), is("2019-07-08T08:08:39"));
        assertThat(transferStatus.getTransition(), is(SCHEDULED));
        assertThat(transferStatus.getFromStatus(), is(QUOTED));
        assertThat(transferStatus.getToStatus(), is(SCHEDULED));
        assertThat(transferStatus.getNotes(), is("Completing the Partial-Balance Transfer"));
    }

    @Test
    public void testScheduleTransfer_unsuccessfulCommit() throws Exception {
        // prepare mock
        String response = mResourceManager.getResourceContent("transfer_commit_already_committed.json");
        mHwPlatform.mockResponse().withHttpResponseCode(HTTP_BAD_REQUEST).withBody(response).mock();

        // test
        Hyperwallet.getDefault().scheduleTransfer("trf-will-fail-created-token",
                "commit transfer test notes that fails", mMockedStatusTransitionListener);
        mCountDownLatch.await(100, TimeUnit.MILLISECONDS);

        // verify
        verify(mMockedStatusTransitionListener, never()).onSuccess(any(StatusTransition.class));
        verify(mMockedStatusTransitionListener).onFailure(mExceptionArgumentCaptor.capture());

        // assert
        RecordedRequest recordedRequest = mHwPlatform.getRequest();
        String requestBody = recordedRequest.getBody().readUtf8();
        JSONObject bodyJson = new JSONObject(requestBody);
        assertThat(bodyJson.getString("notes"), is("commit transfer test notes that fails"));
        assertThat(bodyJson.getString("transition"), is(SCHEDULED));

        String path = recordedRequest.getPath();
        assertThat(path, is("/rest/v3/transfers/trf-will-fail-created-token/status-transitions"));

        assertThat(recordedRequest.getMethod(), is(POST.name()));

        HyperwalletException exception = mExceptionArgumentCaptor.getValue();
        assertThat(exception, is(notNullValue()));
        assertThat(exception.getHyperwalletErrors().getErrors(), Matchers.<HyperwalletError>hasSize(1));
        assertThat(exception.getHyperwalletErrors().getErrors().get(0).getMessage(),
                is("This status transition is not allowed."));
        assertThat(exception.getHyperwalletErrors().getErrors().get(0).getCode(), is("INVALID_STATUS_TRANSITION"));
    }
}
